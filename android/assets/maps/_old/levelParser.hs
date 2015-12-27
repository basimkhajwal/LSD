import Control.Applicative
import Control.Monad

import Data.Char (isSpace, isDigit)

generateTmxMap :: String -> String
generateTmxMap str =
    case runParse parseLevel str of
        Left err        -> err
        Right (l, _)    -> (generateXML . createLevelXML) l

convertLevel :: String -> IO ()
convertLevel name = do
    content <- readFile $ "original/" ++ name ++ ".lua"
    writeFile (name ++ ".tmx") (generateTmxMap content)

main :: IO ()
main = do
    putStrLn "Parsing levels...."
    mapM_ (convertLevel . ("level" ++) . show) ([1..16] :: [Int])


------------------ Level Type Definitions --------------------

type Vector2 = (Int, Int)

type Wall = (Vector2, Vector2)
type Sensor = (Vector2, Vector2)

data Level = Level  Vector2 [Wall] [Sensor] deriving (Show)

------------------ Level File Specific Parsers ---------------

parseArray :: Parser a -> Parser [a]
parseArray parse = parseChar '{' >> parseConsecutive parse

parseConsecutive :: Parser a -> Parser [a]
parseConsecutive parse = skipSpaces >> (objectParse <|> commaParse <|> endParse <|> bail "array parse failure")
    where objectParse = (:) <$> parse <*> next
          commaParse = parseChar ',' >> next
          endParse = parseChar '}' >> return []

          next = parseConsecutive parse

parseVariable :: String -> Parser a -> Parser a
parseVariable name objectParse = do
    n <- parseWord
    assert (n == name) $ "variable " ++ name ++ " not found -- instead " ++ n

    skipSpaces >> parseChar '=' >> skipSpaces >> objectParse

parseLevel :: Parser Level
parseLevel = do
    -- Prefab
    _ <- parseString "return {"

    -- Parse the player coordinates
    ps <- parseVariable "player" (parseArray parseInt)
    assert (length ps == 2) "player position not valid"
    let playerPos = (head ps, ps !! 1)

    -- Parse but ignore map bounds (auto-generated in the Java version)
    mapM_ (\n -> parseChar ',' >> parseVariable n parseInt) ["minX", "maxX", "minY", "maxY"]
    _ <- parseChar ',' >> skipSpaces

    -- Check whether walls or sensors come first then parse accordingly
    wallsFirst <- (== Nothing) <$> try (parseChar 's')

    let fl a _ b = (a,b)

    parsing <- if wallsFirst then fl <$> parseWalls <*> parseChar ',' <*> parseSensors
               else fl <$> parseSensors <*> parseChar ',' <*> parseWalls

    let walls = (if wallsFirst then fst else snd) parsing
        sensors = (if wallsFirst then snd else fst) parsing

    return $ Level playerPos walls sensors

parseWalls :: Parser [Wall]
parseWalls = do
    ws <- parseVariable "walls" (parseArray (parseArray parseInt))
    assert (all ((==4) . length) ws) "wall definitions not valid"
    return $ map (\[x,y,w,h] -> ((x,y - h), (w, h))) ws

parseSensors :: Parser [Sensor]
parseSensors = do
    ss <- parseVariable "sensors" (parseArray (parseArray parseInt))
    assert (all ((==4) . length) ss) "sensor definitions not valid"
    return $ map (\[x,y,w,h]-> ((x,y - h), (w, h))) ss

------------------ Parsers & Utility functions ---------------

type Error = String
newtype Parser a = Parser { runParse :: String -> Either Error (a, String) }

parserError :: Error -> Either Error a
parserError msg = Left ("Error: " ++ msg)

bail :: Error -> Parser a
bail msg = Parser (\_ -> parserError msg )

satisfy :: (Char -> Bool) -> Parser Char
satisfy f = Parser $ \str ->
    case str of
        []                  -> parserError "end of stream"
        (c:cs) | f c        -> Right (c, cs)
               | otherwise  -> parserError "condition not satisfied"

parseByte :: Parser Char
parseByte = satisfy (const True)

parseChar :: Char -> Parser Char
parseChar c = satisfy (==c)

parseString :: String -> Parser String
parseString = foldr (liftM2 (:) . parseChar) (return [])

try :: Parser a -> Parser (Maybe a)
try p = Parser $ \str ->
    case runParse p str of
        Left _       -> Right (Nothing, str)
        Right (a, _) -> Right (Just a, str)

peekChar :: Parser (Maybe Char)
peekChar = try parseByte

parseWhile :: (Char -> Bool) -> Parser String
parseWhile f = do
    result <- peekChar
    case result of
        Just c | f c    -> (:) <$> parseByte <*> parseWhile f
        _               -> pure []

skipSpaces :: Parser String
skipSpaces = parseWhile isSpace

assert :: Bool -> String -> Parser ()
assert True  _   = pure ()
assert False err = bail err

parseWord :: Parser String
parseWord = skipSpaces >> parseWhile (not . isSpace)

parseInt :: Parser Int
parseInt = do
    positive <- (parseChar '-' >> return False) <|> return True
    i <- parseWhile isDigit
    assert (not $ null i) "Integer not found"
    return $ read i * (if positive then 1 else -1)
---------------- Parser Instances ----------------------------

instance Functor Parser where

    fmap f parser = Parser $ \str ->
        case runParse parser str of
            Left err        -> Left err
            Right (a, str2) -> Right (f a, str2)

instance Applicative Parser where

    pure x = Parser (\str -> Right (x, str))

    first <*> second = Parser $ \str0 ->
        case runParse first str0 of
            Left err        -> Left err
            Right (f, str1) ->
                case runParse second str1 of
                    Left err        -> Left err
                    Right (a, str2) -> Right (f a, str2)

instance Monad Parser where
    return = pure

    parser >>= f = Parser $ \str0 ->
        case runParse parser str0 of
            Left err        -> Left err
            Right (a, str1) -> runParse (f a) str1

instance Alternative Parser where

    empty = Parser $ \_ -> Left "empty"
    (<|>) (Parser a) (Parser b) = Parser $ \str ->
        case a str of
            Left _          -> b str
            Right (x, str2) -> Right (x, str2)

----------------- Level XML Generators  -----------------------

defaultMapAttributes :: [Attr]
defaultMapAttributes = attrFromList
    [
        ("version", "1.0"),
        ("orientation", "orthogonal"),
        ("renderorder", "right-up"),

        ("width", "200"), ("height", "200"),
        ("tilewidth", "10"), ("tileheight", "10")
    ]

createMapTag :: Int -> [XMLTag] -> XMLTag
createMapTag objectNum = Dual "map" attrs ""
    where attrs = defaultMapAttributes ++ [Attr "nextobjectid" (show $ objectNum + 1)]

createObjectGroup :: String -> [XMLTag] -> XMLTag
createObjectGroup name = Dual "objectgroup" attrs ""
    where attrs = [Attr "name" name]

createObject :: String -> Int -> Vector2 -> Vector2 -> XMLTag
createObject name objectId (x,y) (width, height) = Single "object" attrs
    where attrs = attrFromList
            [
                ("id", show objectId),
                ("name", name),
                ("type", name),

                ("x", show x), ("y", show y),
                ("width", show width), ("height", show height)
            ]

createLevelXML :: Level -> XMLTag
createLevelXML (Level playerPos walls sensors) = createMapTag numObjects groups
    where numWalls = length walls
          numSensors = length sensors
          numObjects = numWalls + numSensors + 1

          wallIds = [1..numWalls]
          sensorIds = [(numWalls + 1)..(numWalls + numSensors)]

          wallObjects = zipWith (\n (pos, sz) -> createObject "wall" n pos sz) wallIds walls
          sensorObjects = zipWith (\n (pos, sz) -> createObject "sensor" n pos sz) sensorIds sensors
          metaObjects = [createObject "player-position" numObjects playerPos (20, 20)]

          groups = zipWith createObjectGroup ["walls", "sensors", "meta"] [wallObjects, sensorObjects, metaObjects]

----------------- Generic XML Generators ----------------------

-- Specifies the xml tag type either with an end and a start or
-- all in one tag
data XMLTag =
    -- Name , Attributes
    Single  String [Attr] |

    -- Name, Attributes, Content, Children
    Dual String [Attr] String  [XMLTag]

-- Specifies an attribute within the tag (name, value)
data Attr = Attr String String

attrFromList :: [(String, String)] -> [Attr]
attrFromList = map (uncurry Attr)

tagAttrString :: String -> [Attr] -> String
tagAttrString name []    = name
tagAttrString name attrs = name ++ " " ++ unwords attrStrings
    where attrStrings = map attrToString attrs
          attrToString (Attr key val) = key ++ "=" ++ show val

prettyPrintTag :: Int -> XMLTag -> String
prettyPrintTag indentLevel (Single name attrs) = indent ++ "<" ++ tagAttrString name attrs ++ "/>\n"
    where indent = replicate indentLevel '\t'
prettyPrintTag indentLevel (Dual name attrs content children) = beginning ++ middle ++ ending
    where indent = replicate indentLevel '\t'

          tagString = tagAttrString name attrs
          beginning = indent ++ "<" ++ tagString ++ ">\n"

          contentString = if null content then "" else indent ++ "\t" ++ content ++ "\n"
          childrenString = concatMap (prettyPrintTag (indentLevel + 1)) children
          middle = contentString ++ childrenString

          ending = indent ++ "</" ++ name ++ ">\n"

generateXML :: XMLTag -> String
generateXML = (xmlPrefab++) . prettyPrintTag 0
    where xmlPrefab = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
