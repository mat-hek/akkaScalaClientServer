package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory


object UserServer extends App {
  val system = ActorSystem("Sys", ConfigFactory.load("server.conf"))

  val Mike = User("Mike",system)
  Mike ! BecomeServer

}