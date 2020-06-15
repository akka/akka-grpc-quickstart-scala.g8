name := "akka-grpc-quickstart-scala"

version := "1.0"

scalaVersion := "$scala_version$"

lazy val akkaVersion = "$akka_version$"
lazy val akkaGrpcVersion = "$akka_grpc_version$"

enablePlugins(AkkaGrpcPlugin)

// ALPN agent
enablePlugins(JavaAgent)
javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "$jetty_alpn_agent_version$" % "runtime;test"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.1" % Test
)

// TODO remove when Akka gRPC 1.0.0 is final
resolvers += Resolver.bintrayRepo("akka", "snapshots")
