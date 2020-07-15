scalaVersion := "2.13.3"

// library name
name := "scala_nats"

// library version
version := "0.4.0"

// groupId, SCM, license information
organization := "com.github.tyagihas"
homepage := Some(url("https://github.com/tyagihas"))
scmInfo := Some(ScmInfo(url("https://github.com/tyagihas/scala_nats.git"), "git@github.com:tyagihas/scala_nats.git"))
developers := List(Developer("tyagihas", "Teppei Yagihashi", "tyagihas@gmail.com", url("https://github.com/tyagihas")))
licenses += ("MIT", url("http://www.opensource.org/licenses/mit-license.php"))
publishMavenStyle := true

crossPaths := false

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

libraryDependencies ++= {
  Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "com.github.tyagihas"  % "java_nats"   % "0.7.2"
  )
}