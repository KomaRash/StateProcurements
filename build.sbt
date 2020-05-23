name := "State procurements"
organization in ThisBuild := "com.github.KomaRash"
scalaVersion in ThisBuild := "2.13.2"
lazy val root = project
  .in(file("."))
  .settings(parserSettings).aggregate(
  okrbParser)

lazy val okrbParser = project.in(file("OKRBParser")).settings(
  name := "OKRBParser",
  parserSettings,
  libraryDependencies++=parserDependency
)

lazy val parserSettings = Seq(
  scalaVersion := "2.12.0",
  scalacOptions ++= scalacOptionList)

lazy val scalacOptionList = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
val sparkVersion= "2.4.3"
lazy val parserDependency=Seq(
  // https://mvnrepository.com/artifact/com.crealytics/spark-excel
  "com.crealytics" %% "spark-excel" % "0.12.5" ,
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

