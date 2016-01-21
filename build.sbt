name := """akka-sample-remote-scala"""

version := "2.4.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0",
  "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"



fork in run := true