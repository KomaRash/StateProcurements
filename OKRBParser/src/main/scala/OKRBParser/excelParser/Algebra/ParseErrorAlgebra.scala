package OKRBParser.excelParser.Algebra

import OKRBParser.excelParser.ParseResult
import fs2.Stream
trait ParseErrorAlgebra[F[_]] {
  def validValue[A](parseResult: ParseResult[A]):Stream[F,A]
}
