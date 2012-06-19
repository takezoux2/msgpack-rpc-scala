name := "msgpack-rpc-scala"

organization := "org.msgpack"

version := "0.6.7-SNAPSHOT"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0","2.9.0-1","2.9.1","2.9.1-1")

resolvers += Resolver.mavenLocal

publishTo := Some(Resolver.file("localMaven",Path.userHome / ".m2" / "repository"))

libraryDependencies ++= Seq(
  "org.msgpack" %% "msgpack-scala" % "0.6.7-SNAPSHOT",
  "org.msgpack" % "msgpack-rpc" % "0.7.0-SNAPSHOT",
  "junit" % "junit" % "4.8" % "test",
  "org.slf4j" % "slf4j-log4j12" % "1.6.6" % "provided"
)


libraryDependencies <+= (scalaVersion) { v => { v match{
      case "2.10" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.1-1" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.1" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.0-1" => "org.specs2" %% "specs2" % "1.8.2" % "test"
      case "2.9.0" => "org.specs2" %% "specs2" % "1.7.1" % "test"
}}}