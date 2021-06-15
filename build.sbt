import sbt.Keys.{isSnapshot, publishTo}


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
  updateOptions := updateOptions.value.withGigahorse(false)
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
  }
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


//credentials += Credentials("maven_evaluation Realm", "bizonedev.pkgs.visualstudio.com", "BizOneDev", "hjgonlgdt37jyhaf6hhrydvqft5qoxjbzfmga7rry5sv52m725vq")

//publishTo := {
//  if (isSnapshot.value)
//    Some(MavenCache("Sonatype OSS Snapshots", file(Path.userHome.absolutePath + "/.m2/repository/snapshots")))
//  else
//    Some(MavenCache("local-maven", file(Path.userHome.absolutePath + "/.m2/repository")))
//}
resolvers += "Artifact Maven2 Repository" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/"
credentials += Credentials("https://pkgsprodsu3weu.app.pkgs.visualstudio.com/", "bizonedev.pkgs.visualstudio.com", "BizOneDev", "hjgonlgdt37jyhaf6hhrydvqft5qoxjbzfmga7rry5sv52m725vq")
//credentials += Credentials(Path.userHome / ".sbt/.credentials")
publishTo := {
  if (isSnapshot.value)
    Some("snapshots" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/snapshots")
  else
    Some("release" at "https://bizonedev.pkgs.visualstudio.com/Demo/_packaging/maven_evaluation/maven/v1/")

  artifact in (Compile, assembly) := {
    val art = (artifact in (Compile, assembly)).value
    art.withClassifier(Some("assembly"))
  }

  addArtifact(artifact in (Compile, assembly), assembly)

  val packAnsible = taskKey[File]("Pack ansible files.")
  val ansibleArtifactName = settingKey[String]("Ansible artifact name")

  packAnsible := {
    val ansibleZip =
      target.value / s"scala-${scalaBinaryVersion.value}" / s"${name.value}.zip"
    IO.zip(
      IO.listFiles(Path("ansible").asFile).map(f => (f, f.name)),
      ansibleZip,
      None
    )
    ansibleZip
  }
  artifact in packAnsible := Artifact(name.value, "zip", "zip").withClassifier(Some("ansible"))

  addArtifact(artifact in packAnsible, packAnsible)
