package com.example.helloworld

//#import

import java.io.ByteArrayOutputStream
import java.security.{KeyFactory, KeyStore, SecureRandom}
import java.security.cert.{Certificate, CertificateFactory}
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import akka.stream.SystemMaterializer

import com.typesafe.config.ConfigFactory
import javax.net.ssl.{KeyManagerFactory, SSLContext}

import scala.concurrent.{ExecutionContext, Future}

//#import


//#server
object GreeterServer {

  def main(args: Array[String]): Unit = {
    // important to enable HTTP/2 in ActorSystem's config
    val conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())
    val system = ActorSystem[Nothing](Behaviors.empty, "GreeterServer", conf)
    new GreeterServer(system).run()
  }
}

class GreeterServer(system: ActorSystem[_]) {

  def run(): Future[Http.ServerBinding] = {
    implicit val sys = system
    implicit val ec: ExecutionContext = system.executionContext

    val service: HttpRequest => Future[HttpResponse] =
      GreeterServiceHandler(new GreeterServiceImpl(system))

    // Akka HTTP 10.1 requires adapters to accept the new actors APIs
    val bound = Http()(system.toClassic).bindAndHandleAsync(
      service,
      interface = "127.0.0.1",
      port = 8080,
      serverHttpContext
    )(SystemMaterializer(system).materializer)

    bound.foreach { binding =>
      println(s"gRPC server bound to: ${binding.localAddress}")
    }

    bound
  }
  //#server

  // FIXME this will be replaced by a more convenient utility, see https://github.com/akka/akka-grpc/issues/89
  private def serverHttpContext: HttpsConnectionContext = {
    val keyEncoded = read(classOf[GreeterServer].getResourceAsStream("/certs/server1.key")).replace("-----BEGIN PRIVATE KEY-----\n", "").replace("-----END PRIVATE KEY-----\n", "").replace("\n", "")
    val decodedKey = Base64.getDecoder.decode(keyEncoded)
    val spec = new PKCS8EncodedKeySpec(decodedKey)
    val kf = KeyFactory.getInstance("RSA")
    val privateKey = kf.generatePrivate(spec)
    val fact = CertificateFactory.getInstance("X.509")
    val cer = fact.generateCertificate(classOf[GreeterServer].getResourceAsStream("/certs/server1.pem"))
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(null)
    ks.setKeyEntry("private", privateKey, new Array[Char](0), Array[Certificate](cer))
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, null)
    val context = SSLContext.getInstance("TLS")
    context.init(keyManagerFactory.getKeyManagers, null, new SecureRandom)
    ConnectionContext.https(context)
  }

  private def read(in: java.io.InputStream) = {
    val baos = new ByteArrayOutputStream(Math.max(64, in.available))
    val buffer = new Array[Byte](32 * 1024)
    var bytesRead = in.read(buffer)
    while (bytesRead >= 0) {
      baos.write(buffer, 0, bytesRead)
      bytesRead = in.read(buffer)
    }
    new String(baos.toByteArray, "UTF-8")
  }

  //#server

}
//#server
