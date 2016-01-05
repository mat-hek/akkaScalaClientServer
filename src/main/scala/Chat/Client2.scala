package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client2 extends App {
  val port = "2554"
  val config = ConfigFactory.parseString("include \"common\", akka.remote.netty.tcp.port = "++port)
  val system = ActorSystem("Sys", config)

  val John = User("John",system)

  val serverHostName = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.hostname")
  val serverPort = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.port")
  val serverAddr = serverHostName ++":"++ serverPort

  John ! BecomeClient(serverAddr)
  Thread.sleep(2000)
  println("sending hi")
  John ! Send("Hi!")
  John ! Send("Hi again!")
}