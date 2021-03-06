val commonsVersion = "0.7.6"
val provVersion    = "1.0.0"
val kgVersion      = "0.9.1"

lazy val prov           = "ch.epfl.bluebrain.nexus" %% "nexus-prov"      % provVersion
lazy val commonsSchemas = "ch.epfl.bluebrain.nexus" %% "commons-schemas" % commonsVersion
lazy val kgSchemas      = "ch.epfl.bluebrain.nexus" %% "kg-schemas"      % kgVersion


lazy val core = project
  .in(file("modules/core"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(nsgcommons)
  .settings(common)
  .settings(
    name                := "nsg-core-schemas",
    moduleName          := "nsg-core-schemas"
  )

lazy val nexusschema = project
  .in(file("modules/nexus-schemas"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .settings(common, noPublish)
  .settings(
    noPublish,
    name       := "kg-nsg-schemas",
    moduleName := "kg-nsg-schemas",
    libraryDependencies ++= Seq(
      commonsSchemas,
      kgSchemas
    )
  )

lazy val experiment = project
  .in(file("modules/experiment"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(core)
  .settings(common)
  .settings(
    name       := "nsg-experiment-schemas",
    moduleName := "nsg-experiment-schemas"
  )

lazy val nsgcommons = project
  .in(file("modules/commons"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(nexusschema)
  .settings(common)
  .settings(
    name       := "nsg-commons-schemas",
    moduleName := "nsg-commons-schemas",
    libraryDependencies += prov
  )

lazy val atlas = project
  .in(file("modules/atlas"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(experiment)
  .settings(common)
  .settings(
    name       := "nsg-atlas-schemas",
    moduleName := "nsg-atlas-schemas"
  )

lazy val electrophysiology = project
  .in(file("modules/electrophysiology"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(experiment)
  .settings(common)
  .settings(
    name       := "nsg-electrophysiology-schemas",
    moduleName := "nsg-electrophysiology-schemas"
  )

lazy val morphology = project
  .in(file("modules/morphology"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(experiment)
  .settings(common)
  .settings(
    name       := "nsg-morphology-schemas",
    moduleName := "nsg-morphology-schemas"
  )

lazy val simulation = project
  .in(file("modules/simulation"))
  .enablePlugins(WorkbenchPlugin)
  .disablePlugins(ScapegoatSbtPlugin, DocumentationPlugin)
  .dependsOn(core)
  .settings(common)
  .settings(
    name       := "nsg-simulation-schemas",
    moduleName := "nsg-simulation-schemas"
  )

lazy val root = project
  .in(file("."))
  .settings(name := "nsg-schemas", moduleName := "nsg-schemas")
  .settings(common, noPublish)
  .aggregate(core, experiment, atlas, morphology, electrophysiology, simulation, nexusschema,nsgcommons)

lazy val common = Seq(
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Xfatal-warnings")),
  autoScalaLibrary   := false,
  workbenchVersion   := "0.3.0",
  bintrayOmitLicense := true,
  homepage           := Some(url("https://github.com/INCF/neuroshapes")),
  licenses           := Seq("CC-4.0" -> url("https://github.com/INCF/neuroshapes/blob/master/LICENSE")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/INCF/neuroshapes"), "scm:git:git@https://github.com/INCF/neuroshapes.git"))
)

lazy val noPublish = Seq(publishLocal := {}, publish := {})

addCommandAlias("review", ";clean;test")
addCommandAlias("rel", ";release with-defaults skip-tests")
