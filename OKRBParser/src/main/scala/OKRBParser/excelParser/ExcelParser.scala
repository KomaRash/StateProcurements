package OKRBParser.excelParser

import OKRBParser.OKRBProduct
import cats.effect.Sync
import cats.implicits._
import cats.{Foldable, Monad}
import fs2.Stream
import org.apache.poi.ss.usermodel.Sheet

import scala.jdk.CollectionConverters._
class ExcelParser[F[_]:Monad:Sync:Foldable] extends Parser [F]{
  def parseDocument(document: Sheet): F[ParseResult[List[OKRBProduct]]] = {
    val documentIterator=document.iterator().asScala
    Stream.fromIterator(documentIterator).covary[F].
      map(_.toOKRBProduct).compile.
      toList.
      flatMap{x=>x.sequence.pure[F]}
  }/*
  override def createDocument[A](okrbData: fs2.Stream[F, OKRBProduct]): F[A] = ???

  override def parseDocument[A](document: A): F[ParseResult[List[OKRBProduct]]] = ???*/
}
