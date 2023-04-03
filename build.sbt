
scalaVersion := "2.13.8"

name := "scalapb-buf-examples"
organization := "scalapb"
version := "1.0"

libraryDependencies ++= 
    Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-core" % "0.5.3"
        )
