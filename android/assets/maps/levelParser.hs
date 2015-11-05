import Control.Applicative (Applicative, (<*>), pure)
import Control.Monad

type Vector2 = (Int, Int)

type Wall = (Vector2, Vector2)
type Sensor = (Vector2, Vector2)

data Level =
    Level {
        getPlayerPosition :: Vector2,
        getWalls :: [Wall],
        getSensors :: [Sensor]
    }

generateTmxMap :: Level -> String
generateTmxMap level = undefined


main :: IO ()
main = putStrLn "In development :P"

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

parseChar :: Char -> Parser Char
parseChar c = satisfy (==c)

parseString :: String -> Parser String
parseString = foldr (liftM2 (:) . parseChar) (return [])

oneOf :: String -> Parser Char
oneOf cs = satisfy (`elem` cs)

peekChar :: Parser (Maybe Char)
peekChar = Parser $ \str ->
    case str of
        (c:cs)   -> Right (Just c, cs)
        []      -> Right (Nothing, str)

parseWhileWith :: (Char -> a -> a) -> Parser a
parseWhileWith f = do
    result <- peekChar
    case result of
        Just

parseWhile :: (Char -> Bool) -> Parser String
parseWhile f = do
    result <- peekChar
    case result of
        Just c | f c    -> liftM (c:) (parseWhile f)
        _               -> return []

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

------------------ XML Generators -----------------------------

-- Specifies the xml tag type either with an end and a start or
-- all in one tag
data XMLTag =
    Single {
        singTagName :: String,
        singTagAttrs :: [Attr]
    } |
    Dual {
        tagName :: String,
        tagAttrs :: [Attr],

        tagContent :: String,
        tagChildren :: [XMLTag]
    }

-- Specifies an attribute within the tag
data Attr =
    Attr {
        attrName :: String,
        attrValue :: String
    }

attrFromList :: [(String, String)] -> [Attr]
attrFromList = map (\(k, v) -> Attr k v)

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

          contentString = indent ++ "\t" ++ content ++ "\n"
          childrenString = concatMap (prettyPrintTag (indentLevel + 1)) children
          middle = contentString ++ childrenString

          ending = indent ++ "<" ++ name ++ "/>\n"

generateXML :: XMLTag -> String
generateXML = (xmlPrefab++) . prettyPrintTag 0
    where xmlPrefab = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
