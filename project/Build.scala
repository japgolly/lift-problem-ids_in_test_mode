import sbt._, Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin, ScalaJSPlugin.autoImport._

object Experiment {

  object Ver {
    val Scala         = "2.12.1"
    val ScalaJsReact  = "1.0.0-RC1"
    val ReactJs       = "15.4.2"
    val MTest         = "0.4.5"
  }

  type PE = Project => Project

  def commonSettings: PE =
    _.enablePlugins(ScalaJSPlugin)
      .settings(
        scalaVersion       := Ver.Scala,
        scalacOptions     ++= Seq("-deprecation", "-unchecked", "-feature",
                                "-language:postfixOps", "-language:implicitConversions",
                                "-language:higherKinds", "-language:existentials"),
        updateOptions      := updateOptions.value.withCachedResolution(true),
        incOptions         := incOptions.value.withNameHashing(true).withLogRecompileOnMacro(false),
        triggeredMessage   := Watched.clearWhenTriggered)

  def utestSettings: PE =
    _.settings(
      scalacOptions in Test += "-language:reflectiveCalls",
      libraryDependencies   += "com.lihaoyi" %%% "utest" % Ver.MTest % Test,
      testFrameworks        += new TestFramework("utest.runner.Framework"),
      requiresDOM           := true)

  def addCommandAliases(m: (String, String)*): PE = {
    val s = m.map(p => addCommandAlias(p._1, p._2)).reduce(_ ++ _)
    _.settings(s: _*)
  }

  // ==============================================================================================
  lazy val root = Project("root", file("."))
    .settings(name := "experiment-webpack")
    .aggregate(webapp)
    .configure(commonSettings, addCommandAliases(
      "/"   -> "project root",
      "L"   -> "root/publishLocal",
      "C"   -> "root/clean",
      "T"   -> ";root/clean;root/test",
      "c"   -> "compile",
      "tc"  -> "test:compile",
      "t"   -> "test",
      "tq"  -> "testQuick",
      "to"  -> "test-only",
      "cc"  -> ";clean;compile",
      "ctc" -> ";clean;test:compile",
      "ct"  -> ";clean;test"))

  // ==============================================================================================
  lazy val webapp = project
    .configure(commonSettings, utestSettings)
    .settings(
      name := "webapp",
      libraryDependencies ++= Seq(
        "com.github.japgolly.scalajs-react" %%% "core"         % Ver.ScalaJsReact))
     // "com.github.japgolly.scalajs-react" %%% "extra"        % Ver.ScalaJsReact,
     // "com.github.japgolly.scalajs-react" %%% "ext-scalaz72" % Ver.ScalaJsReact,
     // "com.github.japgolly.scalajs-react" %%% "ext-monocle"  % Ver.ScalaJsReact,
}
