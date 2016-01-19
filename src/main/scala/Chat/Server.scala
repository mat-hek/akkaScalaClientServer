/*
package Chat

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by Mat Hek on 2016-01-04.
 */
object Server extends  App {
    val system = ActorSystem("Sys", ConfigFactory.load("server.conf"))
    system.actorOf(Props[Server], "server")
}


class Server extends Actor {

  var clients = List[(String, ActorRef)]();

  def receive = {
    case Connect(username) => {
      broadcast(Info(f"$username%s joined the chat"))
      clients = (username,sender) :: clients
      context.watch(sender)
    }
    case Broadcast(msg) => {
      val username = getUsername(sender)
      broadcast(NewMsg(username, msg))
    }
    case Terminated(client) => {
      val username = getUsername(client)
      clients = clients.filter(sender != _._2)
      broadcast(Info(f"$username%s left the chat"))
    }
  }

  def broadcast(msg: Msg) {
    clients.foreach(x => x._2 ! msg)
  }

  def getUsername(actor: ActorRef): String = {
    clients.filter(actor == _._2).head._1
  }
}
*/