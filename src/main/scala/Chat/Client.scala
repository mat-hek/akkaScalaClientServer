package Chat

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by Mat Hek on 2016-01-04.
 */

object Client {
  def apply(system: ActorSystem, username: String, remoteHostPort: String) = {
    system.actorOf(Props(new Client(username, remoteHostPort)))
  }

}

class Client(val username: String, val remoteHostPort:String) extends Actor {

  val path = s"akka.tcp://Sys@$remoteHostPort/user/server"
  val server = context.actorSelection(path)
  server ! Connect(username)

  def receive = {
    case NewMsg(from, msg) => {
      println(f"[$username%s's client] - $from%s: $msg%s")
    }
    case Send(msg) => server ! Broadcast(msg)
    case Info(msg) => {
      println(f"[$username%s's client] - $msg%s")
    }
    case Disconnect => {
      self ! PoisonPill
    }
  }
}