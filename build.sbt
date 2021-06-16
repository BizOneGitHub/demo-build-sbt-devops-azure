import sbt.Keys.{isSnapshot, publishTo}


crossScalaVersions := Seq("2.11.11", "2.12.3")
//ThisBuild / versionScheme := Some("early-semver")
lazy val commonSettings = Seq(
  version := "0.0.1",
  scalaVersion := "2.12.10",
  organization := "com.bizone",
  name := "velocity",
  crossPaths := false,
  autoScalaLibrary := false,
  packageBin in Compile     := baseDirectory.value /"target/sbt-1.0"/ s"${name.value}-${version.value}.jar",
//  packageDoc in Compile     := baseDirectory.value / s"${name.value}-javadoc.jar",
//   disable publishing the main API jar
  Compile / packageDoc / publishArtifact := false,

  // disable publishing the main sources jar
  Compile / packageSrc / publishArtifact := false,

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
  updateOptions := updateOptions.value.withGigahorse(false),
  crossVersion := CrossVersion.Disabled
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

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

lazy val Prod = config("prod") extend(Compile) describedAs("scope to build production packages")
lazy val Dev = config("dev") extend(Compile) describedAs("scope to build dev packages")
// the application
lazy val app = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .configs(Prod, Dev)
  .settings(commonSettings: _*)
  .settings(inConfig(Dev)(Classpaths.configSettings ++ Defaults.configTasks ++ baseAssemblySettings ++Seq(
  assemblyJarName := s"${name.value}-${version.value}.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("application.json") => MergeStrategy.discard
    case PathList("dev.json") => new MyMergeStrategy()
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
))).settings(inConfig(Prod)(Classpaths.configSettings ++ Defaults.configTasks ++ baseAssemblySettings ++ Seq(
  assemblyJarName := s"${name.value}-${version.value}.jar",
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

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

//publishTo := {
//  if (isSnapshot.value)
//    Some(MavenCache("Sonatype OSS Snapshots", file(Path.userHome.absolutePath + "/.m2/repository/snapshots")))
//  else
//    Some(MavenCache("local-maven", file(Path.userHome.absolutePath + "/.m2/repository")))
//}
//resolvers += "maven_evaluation Maven3 Repository" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/"

fullResolvers := Seq("org-artifacts" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/") // Force only private repository

credentials += Credentials(Path.userHome / ".sbt"/".credentials")
publishTo := {
  if (isSnapshot.value)
    Some("snapshots" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/snapshots")
  else
    Some("release" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/")
}
