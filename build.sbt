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
  libraryDependencies ++= parserDependency,
  libraryDependencies ++= catsDependency,
  libraryDependencies ++= doobieDependency
)

lazy val parserSettings = Seq(
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
lazy val catsDependency = Seq("org.typelevel" %% "cats-core" % "2.0.0",
  "co.fs2" %% "fs2-core" % "2.1.0")


lazy val doobieVersion = "0.8.8"
lazy val doobieDependency = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2" % doobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test",
  "mysql" % "mysql-connector-java" % "8.0.19",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.9")
lazy val parserDependency = Seq(
  "org.apache.poi" % "poi" % "3.9",
  "org.apache.poi" % "poi-ooxml" % "3.9")

