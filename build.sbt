name := "luke-howard"

version := "0.2.2-SNAPSHOT"

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

pomExtra :=
<url>http://github.com/elemica/luke-howard</url>
<licenses>
  <license>
    <name>Apache 2</name>
    <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    <distribution>repo</distribution>
  </license>
</licenses>
<scm>
  <url>https://github.com/elemica/luke-howard.git</url>
  <connection>https://github.com/elemica/luke-howard.git</connection>
</scm>
<developers>
  <developer>
    <id>farmdawgnation</id>
    <name>Matt Farmer</name>
    <email>matt@frmr.me</email>
  </developer>
  <developer>
    <id>pdyraga</id>
    <name>Piotr Dyraga</name>
    <email>piotr.dyraga@softwaremill.com</email>
  </developer>
</developers>

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".sonatype")
