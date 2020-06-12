package OKRBParser

sealed trait OKRBError extends Exception
case class ParseError(errorList:List[String]) extends OKRBError

/*
sealed trait ParseErrorMsg
case class CellParseMsg(cell:Int,row:Int) extends ParseErrorMsg{
  override def toString: String = s"невозможно распарсить ячейку $cell в строке $row"
}
case class EmptyStringError(cell:)
*/

