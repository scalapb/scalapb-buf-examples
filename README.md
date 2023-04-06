# Scala Pb Buf Examples.

This repository contains examples of [Buf](https://buf.build/)

## What is Buf

Buf is a command-line interface tool that provides advanced functionality for working with Protocol Buffers, a popular binary serialization format.It offers features such as linting, compatibility checking, code generation, and dependency management, which can streamline the development workflow for Protocol Buffers-based projects. Buf configuration is defined through yaml files.

## Prerequisites
This guide assumes that you have SBT installed

## Getting Started

To install Buf, follow these steps:

1. Download the appropriate version of Buf for your operating system from the official website (https://docs.buf.build/installation).
2. Choose your operating system
3. Follow the instructions on the website


## Usage

Once Buf is installed, you can use it to validate, lint, and generate code from your Protocol Buffer files. Here are a few basic commands to get you started:

### Lint your project
The example project does something that is suboptimal as it uses a datamodel object (`User`) as a request and response type.
Using separate request and response objects in Protocol Buffers for RPC is better as it leads to better encapsulation, and flexibility. It helps encapsulate the data being sent and received, allows for easier modifications without breaking backwards compatibility, and ensures type safety, catching errors at compile time.
Due to this running the following command will produce some errors

```
buf lint
```

which will produce the following output

```
src/main/protobuf/petstore/v1/petstore.proto:44:5:"petstore.v1.User" is used as the request or response type for multiple RPCs.
src/main/protobuf/petstore/v1/petstore.proto:44:54:RPC response type "User" should be named "ListUsersResponse" or "PetStoreServiceListUsersResponse".
src/main/protobuf/petstore/v1/petstore.proto:45:5:"petstore.v1.User" is used as the request or response type for multiple RPCs.
src/main/protobuf/petstore/v1/petstore.proto:45:28:RPC request type "User" should be named "StoreUsersRequest" or "PetStoreServiceStoreUsersRequest".
src/main/protobuf/petstore/v1/petstore.proto:46:5:"petstore.v1.User" is used as the request or response type for multiple RPCs.
src/main/protobuf/petstore/v1/petstore.proto:46:5:RPC "BulkUsers" has the same type "petstore.v1.User" for the request and response.
src/main/protobuf/petstore/v1/petstore.proto:46:27:RPC request type "User" should be named "BulkUsersRequest" or "PetStoreServiceBulkUsersRequest".
src/main/protobuf/petstore/v1/petstore.proto:46:49:RPC response type "User" should be named "BulkUsersResponse" or "PetStoreServiceBulkUsersResponse".
```

Note who Buf will suggest consistent naming, which also apply to other request response object. A win for consistency!



## My Scala Plugin doesn't exist; what do?

Plugins that are live:

[Base Scala Pb Compiler](https://buf.build/community/scalapb-scala)
[ZIO GRPC](https://buf.build/community/scalapb-zio-grpc)

The following are buf plugins that can be easily added, but require demand so the Buf team will support them. If you want to see them, PLEASE leave a thumbs up. If your company would use that plugin heavily drop a comment!
[FS2 GRPC](https://github.com/bufbuild/plugins/issues/305)
[Akka GRPC pre/post license change](https://github.com/bufbuild/plugins/issues/306)

[Scalapb Validate](https://github.com/bufbuild/plugins/issues/304) plugin would require some help from the Buf team as their plugin test bench doesn't (at the time of writing) support chained plugins.