name := "akka-grpc-quickstart-scala"

version := "1.0"

scalaVersion := "$scala_version$"

lazy val akkaVersion = "$akka_version$"
lazy val akkaGrpcVersion = "$akka_grpc_version$"

enablePlugins(AkkaGrpcPlugin)

// ALPN agent
enablePlugins(JavaAgent)
javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.10" % "runtime;test"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
