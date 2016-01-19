package Chat

import java.awt.Color

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Client2 extends App {
  val port = "2554"
  val config = ConfigFactory.parseString("include \"common\", akka.remote.netty.tcp.port = "++port)
  val system = ActorSystem("Sys", config)

  val John = User("John",Color.blue,system)

  val serverHostName = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.hostname")
  val serverPort = ConfigFactory.load("server.conf").getString("akka.remote.netty.tcp.port")
  val serverAddr = serverHostName ++":"++ serverPort

  John ! BecomeClient(serverAddr)
  Thread.sleep(2000)
  println("sending hi")
  John ! SendText("Hi!")
  John ! SendText("Hi again!")
}