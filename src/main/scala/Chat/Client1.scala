package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client1 extends App {
  val system = ActorSystem("Sys", ConfigFactory.load("client1.conf"))
  val Jack = Client(system, "John","192.168.1.30:2553")
  Jack ! Send("hello")
}
