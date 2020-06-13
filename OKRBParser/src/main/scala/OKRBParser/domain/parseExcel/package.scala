package OKRBParser.domain

import cats.data.Validated

package object parseExcel {
  type ErrorList = List[String]
  type FastExcelParseResult[A] = Either[ErrorList, A]
  type ParseResult[A] = Validated[ErrorList, A]

}
