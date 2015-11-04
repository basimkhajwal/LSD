import Control.Applicative
import Control.Monad
import Data.Functor

type Error = String
newtype Parser a = Parser { runParse :: String -> Either Error (a, String) }

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

err :: Error -> Either Error a
err msg = Left ("Error: " ++ msg)

bail :: Error -> Parser a
bail msg = Parser (\_ -> err msg )

satisfy :: (Char -> Bool) -> Parser Char
satisfy f = Parser $ \str ->
    case str of
        []                  -> err "end of stream"
        (c:cs) | f c        -> Right (c, cs)
               | otherwise  -> err "condition not satisfied"

parseChar :: Char -> Parser Char
parseChar c = satisfy (==c)

parseString :: String -> Parser String
parseString (c:cs) = liftM2 (:) (parseChar c) (parseString cs)
parseString []     = return []

oneOf :: String -> Parser Char
oneOf cs = satisfy (`elem` cs)

peekChar :: Parser (Maybe Char)
peekChar = Parser $ \str ->
    case str of
        (c:_)   -> Right (Just c, str)
        []      -> Right (Nothing, str)

type Vector2 = (Int, Int)

type Wall = (Vector2, Vector2)
type Sensor = (Vector2, Vector2)

data Level =
    Level {
        getPlayerPosition :: Vector2,
        getWalls :: [Wall],
        getSensors :: [Sensor]
    }

main :: IO ()
main = putStrLn "In development :P"
