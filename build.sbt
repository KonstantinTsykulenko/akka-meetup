name := "akka-meetup-crawler"

version := "1.0"

scalaVersion := "2.11.5"

val sprayVersion = "1.3.2"
//val akkaVersion = "2.3.9"
val akkaVersion = "2.4.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "org.jsoup" % "jsoup" % "1.8.1",
  "io.spray" %% "spray-client" % sprayVersion,
  "io.spray" %% "spray-can" % sprayVersion,
  "io.spray" %% "spray-routing" % sprayVersion,
  "io.spray" %% "spray-json" % "1.3.1",
  "io.spray" %% "spray-testkit" % sprayVersion % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.specs2" %% "specs2-core" % "2.3.11" % "test",
  "com.typesafe.akka" % "akka-distributed-data-experimental_2.11" % "2.4.1"
)
    