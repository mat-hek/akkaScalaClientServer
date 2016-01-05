package Chat

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import akka.actor._
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random
/**
 * Created by Mat Hek on 2016-01-05.
 */

object User{
  def apply(nick:String, system:ActorSystem) = system.actorOf(Props(new User(nick)), "user")
}

class User(val nick: String) extends Actor{


  def receive = undefined
  def undefined : Receive = {
    case BecomeClient(addr:String) =>
      implicit val timeout = Timeout(5 seconds)
      val serverSel = context.actorSelection(s"akka.tcp://Sys@$addr/user/user").resolveOne()
      serverSel onComplete {
        case Success(server) =>
          server ! Connect(nick)
          context.become(client(server))

        case Failure(e) => e.printStackTrace
      }


    case BecomeServer =>
      context.become(server((nick,self) :: List[(String,ActorRef)]()))

  }
  def server(clients:List[(String,ActorRef)]): Receive=
  {
    case Connect(username) =>
      import serverFunc._
      broadcast(Info(f"$username%s joined the chat"),clients)
      context.become(server((username,sender) :: clients))

    case Broadcast(msg) =>
      import serverFunc._
      val username = getUsername(sender,clients)
      broadcast(NewMsg(username, msg),clients)

    case Terminated(client) =>
      import serverFunc._
      val username = getUsername(client, clients)
      broadcast(Info(f"$username%s left the chat"), clients)
      context.become(server(clients.filter(sender != _._2)))

    case m => common(m,self)
  }

  object serverFunc {
    def broadcast(msg: Msg, clients:List[(String,ActorRef)]) = clients.foreach(x => x._2 ! msg)
    def getUsername(actor: ActorRef, clients:List[(String,ActorRef)]): String = clients.filter(actor == _._2).head._1
  }

  def client(server:ActorRef) : Receive = {
    case m => common(m,server)
  }


  def common(m:Any,server:ActorRef) = m match {
      case NewMsg(from, msg) => println(f"[$nick%s's client] - $from%s: $msg%s")
      case Send(msg) => server ! Broadcast(msg)
      case Info(msg) => println(f"[$nick%s's client] - $msg%s")
      case Disconnect => self ! PoisonPill
    }

}
