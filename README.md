An example. I believe these text is not correct.# Scala Pb Buf Examples.

More documentation to come...

## What is Buf
Great tool for protobuf dependency management - a better explanation to come

## Prerequisites

This guide assumes you have Scala/SBT.

## Getting Started

Follow the instructions of the [Buf install guide](https://buf.build/docs/installation)

Run the following command to see your code get generated:
“`
buf generate
“`


## My Scala Plugin doesn't exist; what do?

Plugins that are live:

[Base Scala Pb Compiler](https://buf.build/community/scalapb-scala)
[ZIO GRPC](https://buf.build/community/scalapb-zio-grpc)

The following are buf plugins that can be easily added, but require demand so the Buf team will support them. If you want to see them, PLEASE leave a thumbs up. If your company would use that plugin heavily drop a comment!
[FS2 GRPC](https://github.com/bufbuild/plugins/issues/305)
[Akka GRPC pre/post license change](https://github.com/bufbuild/plugins/issues/306)

[Scalapb Validate](https://github.com/bufbuild/plugins/issues/304) plugin would require some help from the Buf team as their plugin test bench doesn't (at the time of writing) support chained plugins.