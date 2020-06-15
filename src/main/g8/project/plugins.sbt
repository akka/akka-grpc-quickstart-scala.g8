// addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "$akka_grpc_version$")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.5")

// TODO remove when Akka gRPC 1.0.0 is final
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "1.0.0-M1+35-c8aa943b")
resolvers += Resolver.bintrayRepo("akka", "snapshots")
