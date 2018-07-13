# Akka gRPC Quickstart with Scala
 
Akka gRPC is a toolkit for building streaming gRPC servers and clients on top of Akka Streams. This guide will get you started building gRPC based systems with Scala. If you prefer to use Akka gRPC with Java, switch to the [Akka gRPC Quickstart with Java guide](https://developer.lightbend.com/guides/akka-grpc-quickstart-java/). 

After trying this example the [Akka gRPC documentation](https://developer.lightbend.com/docs/akka-grpc/current/index.html) is a good next step to continue learning more about Akka gRPC.

## Downloading the example 

The Hello World example for Scala is a zipped project that includes a distribution of sbt, Maven and Gradle. You can choose any of these build tools. You can run it on Linux, MacOS, or Windows. The only prerequisite is Java 8.

Download and unzip the example:

1. Download the zip file from [Lightbend Tech Hub](https://developer.lightbend.com/start/?group=akka&project=akka-grpc-quickstart-scala) by clicking `CREATE A PROJECT FOR ME`. 
1. Extract the zip file to a convenient location: 
  - On Linux and OSX systems, open a terminal and use the command `unzip akka-grpc-quickstart-scala.zip`. Note: On OSX, if you unzip using Archiver, you also have to make the build files executable:

sbt
:   ```
    $ chmod u+x ./sbt
    $ chmod u+x ./sbt-dist/bin/sbt
    ```
    
Maven
:   ```
    Local installation of mvn is required.
    ```

Gradle
:   ```
    $ chmod u+x ./gradlew
    ```

  - On Windows, use a tool such as File Explorer to extract the project. 

## Running the example

To run Hello World:

@sbt[On OSX/Linux use `./sbt` to start sbt in the instructions below, on Windows `sbt.bat`.]
@gradle[On OSX/Linux use `./gradlew` to start Gradle in the instructions below, on Windows `./gradlew.bat`.]

1. In a console, change directories to the top level of the unzipped project.
 
    For example, if you used the default project name, akka-grpc-quickstart-scala, and extracted the project to your root directory,
    from the root directory, enter: `cd akka-grpc-quickstart-scala`

1. Compile the project by entering:

    sbt
    :   ```
        ./sbt compile
        ```

    Maven
    :   ```
        mvn compile
        ```

    Gradle
    :   ```
        ./gradlew compileScala
        ```
 
    @sbt[sbt]@maven[Maven]@gradle[Gradle] downloads project dependencies, generates gRPC classes from protobuf, and compiles.

1. Run the server:

    sbt
    :   ```
        ./sbt "runMain com.example.helloworld.GreeterServer"
        ```

    Maven
    :   ```
        mvn compile dependency:properties exec:exec@server
        ```

    Gradle
    :   ```
        ./gradlew runServer
        ```
 
    @sbt[sbt]@maven[Maven]@gradle[Gradle] runs the `com.example.helloworld.GreeterServer` main class that starts the gRPC server.
    @maven[The `exec:exec@server` execution is defined in the Maven `pom.xml` build definition.]
    @gradle[The `runServer` task is defined in `build.gradle`.]

    The output should include something like:

    ```
    gRPC server bound to: /127.0.0.1:8080
    ```

1. Run the client, open another console window and enter:

    sbt
    :   ```
        ./sbt "runMain com.example.helloworld.GreeterClient"
        ```

    Maven
    :   ```
        mvn compile dependency:properties exec:exec@client
        ```

    Gradle
    :   ```
        ./gradlew runClient
        ```
 
    @sbt[sbt]@maven[Maven]@gradle[Gradle] runs the `com.example.helloworld.GreeterClient` main class that starts the gRPC client.
    @maven[The `exec:exec@client` execution is defined in the Maven `pom.xml` build definition.]
    @gradle[The `runClient` task is defined in `build.gradle`.]

    The output should include something like:

    ```
    Performing request: Alice
    Performing request: Bob
    HelloReply(Hello, Bob)
    HelloReply(Hello, Alice)
    ```


Congratulations, you just ran your first Akka gRPC server and client. Now take a look at what happened under the covers.

You can end the programs with `ctrl-c`.

## What Hello World does

As you saw in the console output, the example outputs several greetings. Let’s take at the code and what happens at runtime.

### Server

First, the `GreeterServer` main class creates an `akka.actor.ActorSystem`, a container in which Actors, Akka Streams and Akka HTTP run. Next, it defines a function from `HttpRequest` to `Future[HttpResponse]` using the `GreeterServiceImpl`. This function
handles gRPC requests in the HTTP/2 with TLS server that is bound to port 8080 in this example.

@@snip [GreeterServer.scala]($g8src$/scala/com/example/helloworld/GreeterServer.scala) { #import #server }

`GreeterServiceImpl` is our implementation of the gRPC service, but first we must define the interface of the service
in the protobuf file `src/main/protobuf/helloworld.proto`:

@@snip [helloworld.proto]($g8src$/protobuf/helloworld.proto) { #service-request-reply }

When compiling the project several things are generated from the proto definition. You can find the generated files in 
@sbt[`target/scala-2.12/src_managed/main/`]@maven[`target/generated-sources/`]@gradle[`build/generated/source/proto/main/`]
if you are curious.

For the server the following classes are generated:

* Message classes, such as `HelloRequest` and `HelloReply`
* `GreeterService` interface of the service
* `GreeterServiceHandler` utility to create the `HttpRequest` to `HttpResponse` function from the `GreeterServiceImpl`

The part that we have to implement on the server side is the `GreeterServiceImpl` which implements the generated `GreeterService` interface. It is this implementation that is bound to the `HTTP` server via the `GreeterServiceHandler` and it looks like this:

@@snip [GreeterServiceImpl.scala]($g8src$/scala/com/example/helloworld/GreeterServiceImpl.scala) { #import #service-request-reply }

### Client

In this example we have the client in the same project as the server. That is common for testing purposes but for real usage
you or another team would have a separate project (different service) that is using the client and doesn't implement the
server side of the service. Between such projects you would only share the proto file (by copying it).

From the same proto file that was used on the server side classes are generated for the client:

* Message classes, such as `HelloRequest` and `HelloReply`
* `GreeterService` interface of the service
* `GreeterServiceClient` that implements the client side of the `GreeterService`

On the client side we don't have to implement anything, the `GreeterServiceClient` is ready to be used as is.

We need an `ActorSystem` and then the `GreeterServiceClient` can be created and used like this:

@@snip [GreeterClient.scala]($g8src$/scala/com/example/helloworld/GreeterClient.scala) { #import #client-request-reply }

Note that clients and servers don't have to be implemented with Akka gRPC. They can be implemented/used with other libraries or languages and interoperate according to the gRPC specification.

### Other types of calls

In this first example we saw a gRPC service call for single request returning a `Future` reply.
The parameter and return type of the calls may also be streams in 3 different combinations:

* **client streaming call** - `Source` (stream) of requests from the client that returns a
  @scala[`Future`]@java[`CompletionStage`] with a single response,
  see `itKeepsTalking` in above example
* **server streaming call** - single request that returns a `Source` (stream) of responses,
  see `itKeepsReplying` in above example
* **client and server streaming call** - `Source` (stream) of requests from the client that returns a
  `Source` (stream) of responses,
  see `streamHellos` in above example

As next step, let's try the @ref[bidirectional streaming calls](streaming.md).

@@@index

* [Streaming gRPC](streaming.md)
* [Testing gRPC](testing.md)

@@@
