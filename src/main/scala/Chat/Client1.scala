package Chat

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * Created by Mat Hek on 2016-01-04.
 */
object Client1 extends App {
  val system = ActorSystem("Sys", ConfigFactory.load("client1.conf"))
  val John = Client(system, "John","127.0.0.1:2553")
  John ! Send("hi!")
}
