 import cats._, data._, implicits._
 val a: OptionT[Eval, Int] = 1.pure[OptionT[Eval, *]]
 val b: OptionT[EitherT[Eval, String, *], Int] =