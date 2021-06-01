import sbt.Keys.publishTo

crossScalaVersions := Seq("2.11.11", "2.12.3")
//ThisBuild / versionScheme := Some("early-semver")
lazy val commonSettings = Seq(
  version := "0.0.1",
  scalaVersion := "2.12.10",
  scalacOptions ++= Seq(
    "-encoding", "utf8",
    "-deprecation",
    "-feature",
    "-language:dynamics",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-unchecked",
    "-target:jvm-1.8"),
  fork := true,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
//  resolvers += Resolver.sonatypeRepo("snapshots")
)

// common dependencies
libraryDependencies  in ThisBuild ++= Seq(
  "com.typesafe" % "config" % "1.4.1",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.specs2" % "specs2-core_2.12" % "4.2.0",
  "org.specs2" % "specs2-junit_2.12" % "4.2.0",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  "junit" % "junit" % "4.11" % Test
)

lazy val Prod = config("prod") extend(Compile) describedAs("scope to build production packages")
lazy val Dev = config("dev") extend(Compile) describedAs("scope to build dev packages")
// the application
lazy val app = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .configs(Prod, Dev)
  .settings(commonSettings: _*).settings(
    name := "velocitysbt",
  )
  .settings(inConfig(Dev)(Classpaths.configSettings ++ Defaults.configTasks ++ baseAssemblySettings ++Seq(
  assemblyJarName := s"${name.value}_2.12-${version.value}.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("application.json") => MergeStrategy.discard
    case PathList("dev.json") => new MyMergeStrategy()
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
    resolvers += Resolver.mavenLocal
))).settings(inConfig(Prod)(Classpaths.configSettings ++ Defaults.configTasks ++ baseAssemblySettings ++ Seq(
  assemblyJarName := "prod.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("application.json") => MergeStrategy.discard
    case PathList("prod.json") => new MyMergeStrategy()
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)))



coverageMinimum := 70

coverageFailOnMinimum := false

coverageHighlighting := true

publishArtifact in Test := false

parallelExecution in Test := false

publishMavenStyle := true
publishTo := Some(MavenCache("local-maven", file(Path.userHome.absolutePath+"/.m2/repository")))
