package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client1 extends App {
  val port = "2553"
  val config = ConfigFactory.parseString("include \"common\", akka.remote.netty.tcp.port = "++port)

  val serverHostName = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.hostname")
  val serverPort = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.port")
  val system = ActorSystem("Sys", config)
  val Jack = Client(system, "Jack", serverHostName ++":"++ serverPort)
  Jack ! Send("hello")

}
