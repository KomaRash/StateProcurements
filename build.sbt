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
  libraryDependencies++=parserDependency,
  libraryDependencies++=catsDependency
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
lazy val catsDependency=Seq("org.typelevel" %% "cats-core" % "2.0.0")
lazy val parserDependency=Seq(
"org.apache.poi" % "poi" % "3.9",
  "org.apache.poi" % "poi-ooxml" % "3.9"
)

