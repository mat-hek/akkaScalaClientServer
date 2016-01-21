package Chat

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import akka.actor._
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random
import java.awt.Color

object User{
  def apply(nick:String, color:Color, system:ActorSystem) = system.actorOf(Props(new User(nick, color, system)), "user")
}

class User(val nick: String, val color:Color, val system:ActorSystem) extends Actor{

  val board = Board(system,self,nick,color)

  def receive = undefined
  def undefined : Receive = {
    case BecomeClient(addr:String) => connectToServer(addr) onComplete {
      case Success(server) =>
        server ! Connect(nick)
        context.watch(server)
        context.become(client(server) orElse common(server))
      case Failure(e) => e.printStackTrace
    }

    case BecomeServer =>
      context.become(server((nick,self) :: List[(String,ActorRef)]()) orElse common(self) )
  }

  def server(clients:List[(String,ActorRef)]): Receive=
  {
    def broadcast(msg: Msg, clients:List[(String,ActorRef)]) = clients.foreach(x => x._2 ! msg)
    def getUsername(actor: ActorRef, clients:List[(String,ActorRef)]): String = clients.filter(actor == _._2).head._1
    val serverRecieve: Receive = {
      case Connect(username) =>
        broadcast(Info(f"$username%s joined the chat"), clients)
        context.watch(sender)
        context.become(server((username, sender) :: clients))

      case Broadcast(msg) =>
        val username = getUsername(sender, clients)
        broadcast(NewMsg(username, msg), clients)

      case Terminated(client) =>
        val username = getUsername(client, clients)
        broadcast(Info(f"$username%s left the chat"), clients)
        context.become(server(clients.filter(sender != _._2)))
    }
    return serverRecieve orElse common(self)
  }


  def client(server:ActorRef) : Receive = {
    val clientReceive:Receive = {
      case Terminated(server)  =>
        println("Connection to the server has been lost")
        self ! PoisonPill
      case Disconnect =>
        println("You are disconnected by the server")
        self ! PoisonPill
    }
    return clientReceive orElse common(server)
  }

  def connectToServer(addr:String) = {
    implicit val timeout = Timeout(5 seconds)
    context.actorSelection(s"akka.tcp://Sys@$addr/user/user").resolveOne
  }

  def common(server:ActorRef):Receive = {
    case NewMsg(from, msg) =>
      msg match{
        case SendText(msg) => println(f"[$nick%s's client] - $from%s: $msg%s")
        case SendDrawing(msg) => board ! msg
      }
    case m:Send => server ! Broadcast(m)
    case Info(msg) => println(f"[$nick%s's client] - $msg%s")
    case OpenBoard => board ! OpenBoard
  }

}
