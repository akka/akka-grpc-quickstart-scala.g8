package com.example.helloworld

//#import
import scala.util.Failure
import scala.util.Success

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer

//#import

//#client
object GreeterClient {

  def main(args: Array[String]): Unit = {
    implicit val sys = ActorSystem("HelloWorldClient")
    implicit val mat = ActorMaterializer()
    implicit val ec = sys.dispatcher

    val client = new GreeterServiceClient(
      GrpcClientSettings("127.0.0.1", 8080)
        .withOverrideAuthority("foo.test.google.fr")
        .withTrustedCaCertificate("ca.pem"))

    singleRequestReply("Alice")
    singleRequestReply("Bob")

    def singleRequestReply(name: String): Unit = {
      println(s"Performing request: $name")
      val reply = client.sayHello(HelloRequest(name))
      reply.onComplete {
        case Success(msg) =>
          println(msg)
        case Failure(e) =>
          println(s"Error: $e")
      }
    }
  }

}
//#client
