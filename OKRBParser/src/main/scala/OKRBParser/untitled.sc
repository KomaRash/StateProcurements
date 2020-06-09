/*
import cats.effect.IO
case class Er() extends Exception
val eff = fs2.Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })
val eff2 = fs2.Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })
val eff3 = fs2.Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })
val eff31 = fs2.Stream.eval(IO {throw Er(); 1 + 1 })
val st=eff++eff2++eff31++eff3
st.compile.drain.unsafeRunSync()*/
import cats.implicits._
13.asRight[String].recover{
  case x=>123
}