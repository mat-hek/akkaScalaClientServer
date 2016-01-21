package Chat
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
        user = User(userName, getColor(color), system)
        user ! BecomeClient(hostName)
      case Cmd("openChat", Array(userName,color)) =>
        user = User(userName, getColor(color), system)
        user ! BecomeServer
      case Cmd("msg", Array(msg)) =>
        user ! SendText(msg)
      case Cmd("board",_) =>
        user ! OpenBoard
      case Cmd("quit",_) =>
        user ! PoisonPill
        System.exit(0)


      case _ => Console println "command not recognized"
    }
  }

}

class TextUIActor extends Actor {
  def receive = {
    case SendText(msg) => Console.println(msg)
  }
}