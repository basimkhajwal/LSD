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


main :: IO ()
main = putStrLn "In development :P"
