package OKRBParser

sealed trait OKRBError extends Exception
case class ParseError(errorList:List[String]) extends OKRBError

