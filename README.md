# ScalaPb Buf Examples.

This repository contains examples of [Buf](https://buf.build/) and it's use with Scala.

## What is Buf?

Buf is a command-line interface tool that provides advanced functionality for working with Protocol Buffers, a popular binary serialization format. It offers features such as linting, compatibility checking, code generation, and dependency management, which can streamline the development workflow for Protocol Buffers-based projects. Buf configuration is defined through YAML files.

## Prerequisites

This guide assumes that you have SBT installed if you wan to compile and run the example code.

## Getting Started

To install Buf, follow these steps:

1. Download the appropriate version of Buf for your operating system from the [official website](https://docs.buf.build/installation).
2. Choose your operating system.
3. Follow the instructions on the website.


## Usage

Once Buf is installed, you can use it to validate, lint, and generate code from your Protocol Buffer files. Here are a few basic commands to get you started:

### Lint your project

The example project uses a datamodel object (`User`) as a request and response type, which is suboptimal. It is recommended to use separate request and response objects in Protocol Buffers for RPC as it leads to better encapsulation, flexibility, and type safety. This approach helps encapsulate the data being sent and received, allows for easier modifications without breaking backwards compatibility, and ensures errors are caught at compile time.

Running the following command will produce some errors:

```
buf lint
```

This will produce the following output:

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

Note that Buf suggests consistent naming, which also applies to other request/response objects. A consistent naming scheme improves code readability and is easier to maintain.

If you don't like the default configuration, you can alter it to suit your preferences. See the [documentation](https://buf.build/docs/breaking/usage/#key-concepts) for configuration details.

## Format your project
No one likes messy code. That's why Buf comes with a handy feature to format your proto files and make them neat and tidy. Use the following command to format this project:

```
buf format -w
```
The `-w` option will write in place.

To see the difference in formatting run:
```
git diff
```

Your peers will thank you for it!

### Detect Breaking Changes
Let's shake things up by introducing a breaking change to our protocol buffer definition. To do this, we'll delete the id field from the User message. Once you've made the change, run the following command:
```
buf breaking --against '.git#branch=master'
```
This will check for any breaking changes in the codebase and report them. In our case, you should see an error that reads:
```
src/main/protobuf/petstore/v1/petstore.proto:11:1:Previously present field "1" with name "id" on message "User" was deleted.
```

Buf provides a powerful compatibility checking tool that can also catch other types of errors such as type changes. You can configure different levels of compatibility checks depending on your needs. More information and configuration options can be found in the breaking [breaking documentation](https://buf.build/docs/breaking/overview/#key-concepts).

It's worth noting that Buf doesn't currently check for breaking changes with extensions made by the `protoc-validate` plugin.

### Buf Schema Registry (BSR)
Buf Schema Registry (BSR) is the place to be for storing your Protocol Buffers schemas. It provides a centralized location for managing your schema files, allowing you to easily store, share, and version them. With BSR, there's no need to copy and paste your protos or publish them as jars - everything is taken care of for you!

With BSR, you can ensure consistency and compatibility across services, and simplify the process of integrating with external services. You can follow this [link](https://buf.build/explore)  to explore protobufs published to BSR. Published modules also include viewable documentation of the types published, making it even easier to integrate.

To publish your protos, follow the well-documented steps outlined in [Buf's documentation](https://buf.build/docs/bsr/quick-start/#step-1:-sign-up-for-a-buf-account).

To pull a dependency, simply add it to your `buf.yaml` file. For instance, if you want to use googleapis, add the following:
```
deps:
  - buf.build/googleapis/googleapis:<GIT_COMMIT_TAG>
```
Here, <GIT_COMMIT_TAG> is the version you want to use. If you don't include it, Buf will always import the latest version. Once you've added the dependency, execute the following command to begin the import process:
```
buf mod update
```
And that's it! Buf will resolve the types of the dependencies and understand their use in your local environment. Additionally, the protobuf types used will also be generated. And if your protobuf files aren't intended for public consumption, Buf also supports private packages.

An example can be found in the `buf.yaml` file found in the petstore directory.

In this example we are using the validate protos used by scalapb-validate:
```
deps:
  - buf.build/envoyproxy/protoc-gen-validate:728c81676f9e54d3571603b90b34c0e6419770c6
```
The other two dependencies are necessary for a demo of local plugins, they can be ignored for now. If you decide that a dependency is no longer needed you can remove it and run:
```
buf mod prune
```

### Code Generation
Oh no! Looks like this project is missing all the Scala types that are needed to compile and run the server. But with Buf we can easily fix this. So let's generate the code we need by running this command:
```
buf generate
```

After running this, you should see all the Scala code and be able to compile and run the server. The code generation is performed by reading the `buf.gen.yaml` directory and executing the remote plugins listed there. Buf supports a range of plugins, which can be found on the [plugins page](https://buf.build/plugins).

Plugins for Scala specifically that currently exist are
- [Base ScalaPb Compiler](https://buf.build/community/scalapb-scala)
- [ZIO GRPC](https://buf.build/community/scalapb-zio-grpc)

**There are a list of plugins, however, that can be added quite easily, but due to the maintenance cost by the Buf team, they have not yet been merged. Please drop a comment or +1 in the linked issues below if you want to see them! Bonus points if you feel like your organization would make use of them.**

Two popular GRPC implementations that can be merged fairly quickly are [FS2 GRPC](https://github.com/bufbuild/plugins/issues/305) and [Akka GRPC](https://github.com/bufbuild/plugins/issues/306). [Scalapb Validate](https://github.com/bufbuild/plugins/issues/304) is also missing, but would likely require more work than the other teams as Buf's test bench for added plugins does not support chained plugins.

Note that plugins do not take care of importing the runtime, so you will need to manually align the runtime dependency version with the plugin version you have defined. In case of the ZIO example, you can refer to the build.sbt file for the dependencies needed. For other runtimes, you will need to include equivalent dependencies.

### How to include a custom protoc plugin in Buf
When using a custom protoc plugin that is not available remotely, it is not as straightforward as adding a remote plugin. This is especially true if you want to ensure that your team's plugin version is kept in sync. Here are the general steps to follow (with full documentation and examples [here](https://buf.build/docs/generate/usage/#2.-define-a-module)):

1. Download your protoc plugin
2. Install the protoc plugin on your $PATH using protoc-gen-<PLUGIN_NAME>
3. In your buf.gen.yaml file, include your plugin in the list of plugins using the plugin: <PLUGIN_NAME> syntax.

Keep in mind that this approach requires more manual setup and maintenance compared to using a remote plugin. Additionally you may wish to include the version as a suffix to the <PLUGIN_NAME> to ensure consistent behavior across computers, or allow different projects to work with different versions of the same plugin.

This example repository uses the validate plugin. To be able to see it run locally

Some commands may require sudo

[Download scalapb-validate-0.3.4](https://repo1.maven.org/maven2/com/thesamet/scalapb/protoc-gen-scalapb-validate/0.3.4/protoc-gen-scalapb-validate-0.3.4-unix.sh)

mv protoc-gen-scalapb-validate-0.3.4-unix.sh /usr/local/bin/protoc-gen-scalapb-validate-0.3.4

Really important that it starts with protoc-gen-* buf assumes the plugins are named this.

chown +x /usr/local/bin/protoc-gen-scalapb-validate-0.3.4


Now uncomment sections in petstore.proto and buf.gen.yaml

note that need for "com.thesamet.scalapb" %% "scalapb-validate-core" % "0.3.4"

run `buf generate --include-imports`

Now run the server, and ask for a user with only one character


## Conclusion

And thats it! This guide only gives a high level overview of what can be achieved and configured with Buf. Buf's resources and documentation are comprehensive, so please review that for more details. If there is something this guide missed, or can be made more clear, please feel free to drop a PR.