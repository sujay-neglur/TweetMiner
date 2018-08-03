name := """TweetMiner_Play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

//libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.4"


//libraryDependencies += "com.ning" % "async-http-client" % "1.9.29"

//libraryDependencies ++= Seq(
//  ws
//)

//javaOptions in Test += "-Dlogger.file=conf/logback.xml"
//testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

//javacOptions in (Compile,doc) ++= Seq("-notimestamp", "-linksource")






// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19"
libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "org.twitter4j" % "twitter4j-async" % "4.0.4",
  "org.twitter4j" % "twitter4j-stream" % "4.0.4",
  "org.twitter4j" % "twitter4j-media-support" % "4.0.4"
)
