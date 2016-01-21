package Chat


abstract class Msg

abstract class Send extends Msg
case class SendText(msg: String) extends Send
case class SendDrawing(drawing: Drawing) extends Send

case class NewMsg(from: String, msg:Send) extends Msg

case class Info(msg: String) extends Msg

case class Connect(username: String) extends Msg
case class Broadcast(msg: Send) extends Msg
case object Disconnect extends Msg

abstract class BecomeMsg extends Msg
case object BecomeServer extends BecomeMsg
case class BecomeClient(addr:String) extends BecomeMsg
