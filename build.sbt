name := """sharePains"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test

// flyway slickがDB（mysql）と連携するために必要！
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"

libraryDependencies += "org.flywaydb" %% "flyway-play" % "3.2.0"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "com.typesafe.play" %% "play-slick" % "2.1.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.0"
)

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"

val elastic4sVersion = "6.5.1"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
