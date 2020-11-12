import cats.effect.IO
import scodec.bits.ByteVector

val a:Map[Int,Int]=Map()
val b=a+(1->2)
val c=b+(1->3)
fs2.Stream.emits(Array[Int](1,2,3,412,42,14)).covary[IO].compile.toVector.unsafeRunSync()