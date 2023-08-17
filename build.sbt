import com.typesafe.sbt.packager.docker.DockerPermissionStrategy

enablePlugins(DockerPlugin, JavaAppPackaging, GitVersioning)

scalaVersion := "2.13.8"
val catsVersion = "2.9.0"

name := "regions-locations-task"
organization := "ch.epfl.scala"
version := git.gitHeadCommit.value.getOrElse("0.1").take(5)

Docker / packageName := packageName.value
Docker / version := version.value
dockerBaseImage := "openjdk:11"
dockerExposedPorts := Seq(9000, 9443)
dockerExposedVolumes := Seq("/opt/docker/logs")
Docker / defaultLinuxInstallLocation := "/opt/docker"
Docker / daemonUserUid := None
Docker / daemonUser := "daemon"

dockerRepository := Some("arnasbr")
dockerUpdateLatest := true
dockerPermissionStrategy := DockerPermissionStrategy.MultiStage
Docker / dockerGroupLayers := PartialFunction.empty

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)

libraryDependencies += "com.monovore" %% "decline" % "2.4.1"

libraryDependencies ++= Seq(
  "org.scalatestplus" %% "scalacheck-1-17" % "3.2.16.0" % "test",
  "org.scalatest" %% "scalatest" % "3.2.16" % "test",
  "org.scalactic" %% "scalactic" % "3.2.16",
  "org.typelevel" %% "cats-core" % catsVersion
)
