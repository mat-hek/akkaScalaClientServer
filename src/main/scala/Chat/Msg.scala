package Chat

/**
 * Created by Mat Hek on 2016-01-04.
 */
abstract class Msg

case class Send(msg: String) extends Msg
case class NewMsg(from: String, msg: String) extends Msg
case class Info(msg: String) extends Msg

case class Connect(username: String) extends Msg
case class Broadcast(msg: String) extends Msg
case object Disconnect extends Msg

case object BecomeServer extends Msg
case class BecomeClient(addr:String) extends Msg