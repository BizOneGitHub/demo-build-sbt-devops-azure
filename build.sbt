import sbt.Keys.{isSnapshot, publishTo}
import ReleaseTransformations._
import ReleasePlugin.autoImport._
import sbtrelease.{Git, Utilities}
import Utilities._

ThisBuild / organization := "com.bizone"
//ThisBuild / version := "0.0.4"
ThisBuild / scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.11.11", "2.12.3")

lazy val commonSettings = Seq(
  name := "velocity",
  crossPaths := false,
  autoScalaLibrary := false,
  packageBin in Compile     := baseDirectory.value /"target"/ s"${name.value}-${version.value}.jar",
//  packageDoc in Compile     := baseDirectory.value / s"${name.value}-javadoc.jar",
//   disable publishing the main API jar
  Compile / packageDoc / publishArtifact := false,

//   disable publishing the main sources jar
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
    "-target:jvm-1.8"
        ),
  fork := true,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
)
// common dependencies
ThisBuild / libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.1",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.specs2" % "specs2-core_2.12" % "4.2.0",
  "org.specs2" % "specs2-junit_2.12" % "4.2.0",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  "junit" % "junit" % "4.11" % Test,

)

lazy val Prod = config("prod") extend(Compile) describedAs("scope to build production packages")
lazy val Dev = config("dev") extend(Compile) describedAs("scope to build dev packages")
// the application
lazy val app = project
  .in(file("."))
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


publishMavenStyle := true


publishTo := {
  if (isSnapshot.value)
    Some(MavenCache("Sonatype OSS Snapshots", file(Path.userHome.absolutePath + "/.m2/repository/snapshots")))
  else
    Some(MavenCache("local-maven", file(Path.userHome.absolutePath + "/.m2/repository")))
}


//credentials += Credentials(Path.userHome / ".sbt"/".credentials")
//publishTo := {
//  if (isSnapshot.value)
//    Some("snapshots" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/snapshots")
//  else
//    Some("release" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_sbt_demo/maven/v1/")
//}


//val deployBranch = "master"
//def merge: (State) => State = { st: State =>
//  val git = st.extract.get(releaseVcs).get.asInstanceOf[Git]
//  val curBranch = (git.cmd("rev-parse", "--abbrev-ref", "HEAD") !!).trim
//  st.log.info(s"####### current branch: $curBranch")
//  git.cmd("checkout", deployBranch) ! st.log
//  st.log.info(s"####### pull $deployBranch")
//  git.cmd("pull") ! st.log
//  st.log.info(s"####### merge")
//  git.cmd("merge", curBranch, "--no-ff", "--no-edit") ! st.log
//  st.log.info(s"####### push")
//  git.cmd("push", "origin", s"$deployBranch:$deployBranch") ! st.log
//  st.log.info(s"####### checkout $curBranch")
//  git.cmd("checkout", curBranch) ! st.log
//  st
//}

//lazy val mergeReleaseVersionAction = { st: State =>
//  val newState = merge(st)
//  newState
//}
//
//val mergeReleaseVersion = ReleaseStep(mergeReleaseVersionAction)
publishConfiguration := publishConfiguration.value.withOverwrite(true)
releaseIgnoreUntrackedFiles := false

//val releaseTagComment        : TaskKey[String]
//val releaseCommitMessage     : TaskKey[String]
//val releaseNextCommitMessage : TaskKey[String]

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,
  commitReleaseVersion,
  pushChanges,                //to make sure develop branch is pulled && will merge into master and push
  tagRelease,
//  setNextVersion,
//  commitNextVersion,
//  pushChanges,
)

releaseUseGlobalVersion := false
/*

 */