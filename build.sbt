name := "sbt-multi-project-example"
organization in ThisBuild := "com.github.KomaRash"
scalaVersion in ThisBuild := "2.13.2"
lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + "-assembly.jar",
)
lazy val root = project
  .in(file("."))
  .settings(settings, assemblySettings).aggregate(
  okrbParser
)

lazy val okrbParser = project.in(file("OKRBParser")).settings(
  name := "OKRBParser",
  assemblySettings, settings
)



lazy val settings = Seq(
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


/*
name := "State procurements"

version := "0.1"

scalaVersion := "2.13.1"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "unchecked",
  "-language:postfixOps",
  "-language:higherKinds"
)
val enumeratumVersion="1.5.15"
val catsVersion="2.0.0"
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

/**
 * org.typelevel.cats Dependency
 */
libraryDependencies++=Seq(
  "org.typelevel" %% "cats-core" % catsVersion
)
libraryDependencies++=Seq(
  "com.beachape" %% "enumeratum" % enumeratumVersion)*/
