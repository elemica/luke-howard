name := "luke-howard"

version := "0.2.0"

organization := "com.elemica.cloudsearch"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies <++= scalaVersion {
  case version if version.startsWith("2.11") => Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2"
  )
  case _ => Seq()
}
