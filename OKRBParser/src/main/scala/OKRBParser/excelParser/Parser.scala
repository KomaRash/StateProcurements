package OKRBParser.excelParser

import OKRBParser.OKRBProduct
import fs2.Stream
trait Parser[F[_]]{
 def parseDocument[A](document: A) = ???
 def createDocument[A](okrbData: Stream[F,OKRBProduct]):F[A] = ???
}
