// Replace this with your tokenized resolver URL, see README.md
resolvers += "Akka library repository".at("https://repo.akka.io/maven/github_actions")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "$akka_grpc_version$")
