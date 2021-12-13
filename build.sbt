ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

val AkkaVersion = "2.6.17"
val AkkaHttpVersion = "10.2.7"
// local publish version
val scalikeJdbcVersion = "4.1.0-SNAPSHOT"
//val scalikeJdbcVersion = "4.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "scalikejdbc-sandbox",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
    ) ++ Seq(
      "org.postgresql" % "postgresql" % "42.3.1",
      "mysql" % "mysql-connector-java" % "8.0.27",
      "org.scalikejdbc" %% "scalikejdbc" % scalikeJdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikeJdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-interpolation" % scalikeJdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-interpolation-macro" % scalikeJdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % "4.0.+" % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.+",
    ) ++ Seq(
      "com.typesafe" % "config" % "1.4.1"
    ) ++ Seq(
      // hikaricp
      "com.zaxxer" % "HikariCP" % "5.0.0",
      //
      "commons-dbcp" % "commons-dbcp" % "1.4" % "provided",
//      "org.apache.commons" % "commons-pool2" % "2.11.1",
//      "org.apache.commons" % "commons-dbcp2" % "2.9.0",
      "commons-pool" % "commons-pool" % "20030825.183949",
      "commons-collections" % "commons-collections" % "20040616",
      //
      "com.jolbox" % "bonecp" % "0.8.0.RELEASE"
    ),
    dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.32",
    dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
  )

run / fork := true
Test / fork := true
logLevel := Level.Debug
