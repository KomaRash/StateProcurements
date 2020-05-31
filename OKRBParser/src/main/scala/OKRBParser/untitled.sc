val a=fs2.Stream("12","123","s2")
import cats.syntax.either._
a.map(x=>Either.catchNonFatal{x.toInt}).compile.toList
