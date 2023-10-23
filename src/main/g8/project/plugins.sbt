resolvers += "Akka library repository".at("https://repo.akka.io/maven")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "$akka_grpc_version$")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.5")
