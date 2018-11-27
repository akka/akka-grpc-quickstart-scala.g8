//#full-example
package com.example.helloworld

import scala.concurrent.Await

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._
import scala.language.postfixOps

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Span

class GreeterSpec
  extends Matchers
  with WordSpecLike
  with BeforeAndAfterAll
  with ScalaFutures {

  implicit val patience = PatienceConfig(5.seconds, Span(100, org.scalatest.time.Millis))

  val serverSystem: ActorSystem = {
    // important to enable HTTP/2 in server ActorSystem's config
    val conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())
    val sys = ActorSystem("HelloWorldServer", conf)
    val bound = new GreeterServer(sys).run()
    // make sure server is bound before using client
    bound.futureValue
    sys
  }

  implicit val clientSystem = ActorSystem("HelloWorldClient")
  implicit val mat = ActorMaterializer()

  val client = {
    implicit val ec = clientSystem.dispatcher
    GreeterServiceClient(GrpcClientSettings.fromConfig("helloworld.GreeterService"))
  }

  override def afterAll: Unit = {
    Await.ready(clientSystem.terminate(), 5.seconds)
    Await.ready(serverSystem.terminate(), 5.seconds)
  }

  "GreeterService" should {
    "reply to single request" in {
      val reply = client.sayHello(HelloRequest("Alice"))
      reply.futureValue should ===(HelloReply("Hello, Alice"))
    }
  }
}
//#full-example
