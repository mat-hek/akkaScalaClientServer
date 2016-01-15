name := """akka-sample-remote-scala"""

version := "2.4.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0",
  "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
)


fork in run := true