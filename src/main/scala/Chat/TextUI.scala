package Chat

import java.awt.Color
import java.lang.String

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by Mat Hek on 2016-01-20.
 */
object TextUI extends App {

  var port = Console.in.readLine
  if(port.length == 0) port = "2552"
  val config = ConfigFactory.parseString("include \"common\", akka.remote.netty.tcp.port = "++port)
  val system = ActorSystem("Sys", config)

  system.actorOf(Props[TextUIActor])

  case class Cmd(val cmd:String, val params:Array[String])
  def matchCmd(input:String) = {
    val idx = input indexOf " "
    if(idx == -1 || idx == input.length)
      Cmd(input, Array[String]())
    else
      Cmd(input.substring(0,idx), input.substring(idx+1).split(" "))
  }

  var user:ActorRef = null

  while(true) {
    matchCmd(Console.in.readLine) match {
      case Cmd("connect",Array(hostName,userName,color)) =>
        user = User(userName, Color.orange, system)
        user ! BecomeClient(hostName)
      case Cmd("openChat", Array(userName,color)) =>
        user = User(userName, Color.orange, system)
        user ! BecomeServer
      case Cmd("sendMsg", Array(msg)) =>
        user ! SendText(msg)

      case _ => Console println "command not recognized"
    }
  }
  /*


  Jack ! BecomeClient(serverAddr)
  Thread.sleep(2000)
  println("sending hi")
  Jack ! SendText("Hi!")
  Jack ! SendText("Hi again!")
  */
}

class TextUIActor extends Actor {
  def receive = {
    case SendText(msg) => Console.println(msg)
  }
}