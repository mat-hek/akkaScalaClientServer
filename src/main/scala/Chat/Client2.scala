package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client2 extends App {
  val system = ActorSystem("Sys", ConfigFactory.load("client2.conf"))
  val John = Client(system, "John","192.168.1.30:2553")
  John ! Send("hi!")
}