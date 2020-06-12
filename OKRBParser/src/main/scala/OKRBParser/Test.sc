val a=fs2.Stream(123,-2,123,31)
a.drop(2).compile.toVector
val c=for{
  b<-a.drop(2)
} yield b
c.compile.toVector
