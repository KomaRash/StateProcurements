package OKRBParser.excelParser.Algebra
import OKRBParser.{ParseError, StreamUtils}
import OKRBParser.excelParser.ParseResult
import cats.data.Validated.{Invalid, Valid}
import cats.effect.Sync
import fs2.Stream
class ExcelParseErrorAlgebra[F[_]:Sync](implicit S:StreamUtils[F]) extends ParseErrorAlgebra [F] {
  override def validValue[A](parseResult: ParseResult[A]): Stream[F, A] =
    parseResult match {
      case Invalid(e) => S.error[A](ParseError(e))
      case Valid(a) => S.evalF(a)
    }

}