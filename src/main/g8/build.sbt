name := "akka-grpc-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaGrpcVersion = "$akka_grpc_version$"

enablePlugins(AkkaGrpcPlugin)

// ALPN agent
enablePlugins(JavaAgent)
javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.7" % "runtime;test"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
