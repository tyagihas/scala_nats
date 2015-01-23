import sbtrelease._
import ReleaseStateTransformations._

releaseSettings
sonatypeSettings
scalaVersion := "2.10.4"
version := "0.1"
crossScalaVersions := List("2.9.3", "2.10.4")
name := "scala_nats"
organization := "com.github.tyagihas"
description := "scala_nats"

publishMavenStyle := true
libraryDependencies += "com.github.tyagihas" % "java_nats" % "0.5.1"
publishArtifact in Test := false

pomExtra := (
<url>https://github.com/tyagihas/scala_nats</url>
<licenses>
	<license>
                <name>The MIT License</name>
                <url>http://www.opensource.org/licenses/mit-license.php</url>
                <distribution>repo</distribution>
            </license>
</licenses>
<scm>
	<connection>scm:git:git@github.com:tyagihas/scala_nats.git</connection>
	<developerConnection>scm:git:git@github.com:tyagihas/scala_nats.git</developerConnection>
	<url>git@github.com:tyagihas/scala_nats.git</url>
</scm>
<developers>
	<developer>
		<id>tyagihas</id>
		<name>Teppei Yagihashi</name>
		<url>https://github.com/tyagihas</url>
	</developer>
</developers>
)
scalacOptions ++= Seq("-deprecation", "-Xlint", "-unchecked")
ReleaseKeys.releaseProcess := Seq[ReleaseStep](
checkSnapshotDependencies,
inquireVersions,
runClean,
runTest,
setReleaseVersion,
commitReleaseVersion,
tagRelease,
ReleaseStep(
action = state => Project.extract(state).runTask(PgpKeys.publishSigned, state)._1,
enableCrossBuild = true
),
setNextVersion,
commitNextVersion,
ReleaseStep(state => Project.extract(state).runTask(SonatypeKeys.sonatypeReleaseAll, state)._1),
pushChanges
)