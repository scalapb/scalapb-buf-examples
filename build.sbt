
scalaVersion := "2.13.8"

name := "scalapb-buf-examples"
organization := "scalapb"
version := "1.0"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"
scalacOptions += "-Wunused"

libraryDependencies ++= 
    Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "io.grpc" % "grpc-netty-shaded" % "1.54.0",
        "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-core" % "0.5.3",
        // Uncomment the following line for the local valdiation plugin, this is needed
        // to get some of the types that the code generator references.
        // "com.thesamet.scalapb" %% "scalapb-validate-core" % "0.3.4",
    )
