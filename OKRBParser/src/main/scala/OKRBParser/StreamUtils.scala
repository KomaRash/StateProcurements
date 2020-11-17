package OKRBParser

import cats.effect.Sync
import fs2.Stream

trait StreamUtils[F[_]] {
  def evalF[A](thunk: => A)(implicit F: Sync[F]): Stream[F, A] = Stream.eval(F.delay(thunk))
  def fromIterator[A](iterator:Iterator[A])(implicit F: Sync[F]):Stream[F,A]=Stream.fromIterator[F](iterator)
  def error[A](error:OKRBError)(implicit F: Sync[F]): Stream[F, A] =
    Stream.raiseError(error).covary[F]
  def emits[A](seq:Seq[A]):Stream[F,A]=Stream.emits[F,A](seq)
}

object StreamUtils {
  implicit def syncInstance[F[_]: Sync]: StreamUtils[F] = new StreamUtils[F] {}
}
