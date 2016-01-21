package Chat

abstract class LocalMsg
case object OpenBoard extends  LocalMsg
case class ThrowServer(userName:String) extends  LocalMsg
