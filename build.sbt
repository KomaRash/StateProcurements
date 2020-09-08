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
  libraryDependencies ++= doobieDependency,
  libraryDependencies ++= http4sDependency
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

lazy val http4sVersion = "0.21.0"
lazy val doobieVersion = "0.8.8"
lazy val flywayVersion = "6.3.1"
lazy val doobieDependency = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2" % doobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test",
  "org.flywaydb" % "flyway-core" % flywayVersion,
  "org.postgresql" % "postgresql" % "42.2.5",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.9")
lazy val parserDependency = Seq(
  "org.apache.poi" % "poi" % "3.9",
  "org.apache.poi" % "poi-ooxml" % "3.9")
lazy val http4sDependency = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-scala-xml" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-core" % "0.13.0",
  "io.circe" %% "circe-generic" % "0.13.0"

)
