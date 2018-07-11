## Testing gRPC
 
The tests in the Hello World example illustrates use of the [ScalaTest](http://www.scalatest.org/) framework. The test coverage is not complete. It only shows how to get started with testing gRPC services. You could add to it as an exercise to increase your own knowledge.
 
Let's look at the test class definition in the `GreeterSpec.scala` source file:
 
@@snip [GreeterSpec.scala]($g8srctest$/scala/com/example/helloworld/GreeterSpec.scala) { #full-example }

Note how we create two `ActorSystem`s, one for the server and another for the client. The test is then using the client
to verify that it retrieves the expected responses from the server.

### Unit testing

The above test example is a full integration test using real client and server, including communication via HTTP/2.
For some testing of the service implementation it might be more appropriate to write unit tests without interaction
via the gRPC client. Since the service interface and implementation doesn't require any gRPC intrastructure it can
be tested without binding it to a HTTP server.

@@snip [GreeterServiceImplSpec.scala]($g8srctest$/scala/com/example/helloworld/GreeterServiceImplSpec.scala) { #full-example }

### Add streaming tests

As an exercise to increase your understanding you could add tests for the @ref[streaming call](streaming.md), both as
integration test and unit test style.

The Akka documentation of [Testing streams](https://doc.akka.io/docs/akka/current/stream/stream-testkit.html) might
be useful.