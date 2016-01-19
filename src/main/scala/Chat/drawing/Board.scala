package Chat.drawing

/**
 * Created by Mat Hek on 2016-01-15.
 */

import Chat.SendDrawing
import akka.actor._

import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D, BasicStroke }

import scala.swing._
object Board{
  def apply(system:ActorSystem, userActor: ActorRef, userName:String, userColor:Color):ActorRef = {
    val boardDrawing = new BoardDrawing(userActor, userName, userColor)
    system.actorOf(Props(new BoardActor(boardDrawing)))
  }
}

class BoardActor(boardDrawing:BoardDrawing) extends Actor
{
  def receive = {
    case drawing:Drawing => boardDrawing.draw(drawing)
  }
}
class BoardDrawing(userActor:ActorRef, userName:String, userColor:Color) extends Frame
{
  visible = true
  title = userName ++ "'s board"
  var lastPoint:Point = null

  val canvas = new Canvas {
    preferredSize = new Dimension(100, 100)
  }
  contents = new BorderPanel {
    layout(canvas) = Center
  }
  size = new Dimension(600, 400)


  // specify which Components produce events of interest
  listenTo(canvas.mouse.clicks, canvas.mouse.moves)

  def draw(drawing:Drawing) {canvas.drawLine(drawing.line)}

  var lineDrawingEvt = (point:Point) => {
    val newPoint = new Point(point.x, point.y)
    if (lastPoint == null)
      lastPoint = newPoint
    val line = new Line(lastPoint, newPoint, userColor)
    userActor ! SendDrawing(new Drawing(line))
    canvas.preDrawLine(line)
    lastPoint = newPoint
  }
  // react to events
  reactions += {
    case MouseDragged(_,point,_) => lineDrawingEvt(point)
    case MouseClicked(_,point,_,_,_) => {
      lineDrawingEvt(point)
      lastPoint = null
    }
    case _:MouseReleased | _:MouseExited => {
      lastPoint = null
    }
  }


}

case class Drawing(val line:Line)

case class Line(val point1:Point, val point2:Point, val color: java.awt.Color)


class Canvas extends Panel {

  var preLines = List[Line]()
  var lines = List[Line]()

  override def paintComponent(g: Graphics2D) {

    // Start by erasing this Canvas

    g.clearRect(0, 0, size.width, size.height)
    g.setStroke(new BasicStroke(5))

    g.setColor(Color.GRAY)
    for (line <- preLines)
      g.drawLine(line.point1.x, line.point1.y, line.point2.x, line.point2.y)

    for (line <- lines) {
      g.setColor(line.color)
      g.drawLine(line.point1.x, line.point1.y, line.point2.x, line.point2.y)
    }
  }


  def drawLine(line: Line) {
    lines = lines :+ line
    repaint
  }
  def preDrawLine(line: Line): Unit ={
    preLines = lines :+ line
    repaint
  }

}
