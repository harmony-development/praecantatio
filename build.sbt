import org.typelevel.sbt.tpolecat.*

ThisBuild / organization := "com.github.harmony-development"
ThisBuild / scalaVersion := "3.4.0"

val http4sVersion = "1.0.0-M41"
val circeVersion = "0.14.8"
val log4catsVersion = "2.7.0"

lazy val root = (project in file(".")).settings(
  name := "Praecantatio",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % "3.5.3",
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % "3.5.3",
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % "3.5.3",
    "org.typelevel" %% "cats-effect-testing-specs2" % "1.5.0" % Test,
    "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test,
    "org.http4s" %% "http4s-ember-client" % http4sVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,
    "org.tpolecat" %% "skunk-core" % "0.6.4",
  )
)
