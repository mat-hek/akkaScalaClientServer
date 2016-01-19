package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import java.awt.Color

object UserServer extends App {
  val system = ActorSystem("Sys", ConfigFactory.load("server.conf"))

  val Mike = User("Mike",Color.green,system)
  Mike ! BecomeServer

}