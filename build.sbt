val msgpackVersion = "0.6.10"

name := "msgpack-rpc-scala"

organization := "org.msgpack"

version := msgpackVersion

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.9.0","2.9.0-1","2.9.1","2.9.1-1","2.10.4")

resolvers += Resolver.mavenLocal

resolvers += "MessagePack Maven Repository" at "http://msgpack.org/maven2"

publishTo := Some(Resolver.file("localMaven",Path.userHome / ".m2" / "repository"))

libraryDependencies ++= Seq(
  "org.msgpack" %% "msgpack-scala" % msgpackVersion,
  "org.msgpack" % "msgpack-rpc" % "0.7.0",
  "junit" % "junit" % "4.8" % "test",
  "org.slf4j" % "slf4j-log4j12" % "1.6.6" % "provided"
)


libraryDependencies <+= (scalaVersion) { v => { v match{
      case "2.10" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.1-1" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.1" => "org.specs2" %% "specs2" % "1.11" % "test"
      case "2.9.0-1" => "org.specs2" %% "specs2" % "1.8.2" % "test"
      case "2.9.0" => "org.specs2" %% "specs2" % "1.7.1" % "test"
      case "2.10.4" => "org.specs2" %% "specs2" % "2.3.11" % "test"
}}}