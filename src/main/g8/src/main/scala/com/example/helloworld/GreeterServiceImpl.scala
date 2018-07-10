package com.example.helloworld

//#import
import scala.concurrent.Future
import akka.stream.Materializer

//#import

//#service-impl
class GreeterServiceImpl(materializer: Materializer) extends GreeterService {
  import materializer.executionContext
  private implicit val mat: Materializer = materializer

  override def sayHello(in: HelloRequest): Future[HelloReply] = {
    Future.successful(HelloReply(s"Hello, ${in.name}"))
  }
}
//#service-impl
