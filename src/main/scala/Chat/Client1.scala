package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client1 extends App {

  val port = "2553"
  val config = ConfigFactory.parseString("include \"common\", akka.remote.netty.tcp.port = "++port)
  val system = ActorSystem("Sys", config)

  val Jack = User("Jack",system)

  val serverHostName = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.hostname")
  val serverPort = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.port")
  val serverAddr = serverHostName ++":"++ serverPort

  Jack ! BecomeClient(serverAddr)
  Thread.sleep(2000)
  println("sending hi")
  Jack ! Send("Hi!")
  Jack ! Send("Hi again!")

}
