

type Error = String
newtype Parser a = Parser { runParse :: String -> Either Error (a, String)}
