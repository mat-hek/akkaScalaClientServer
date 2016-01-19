package Chat.drawing

/**
 * Created by Mat Hek on 2016-01-15.
 */

import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D, BasicStroke }

import scala.swing._
object Board extends App
{
  new Frame {
    visible = true
    title = "Board"
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

    var lineDrawingEvt = (point:Point) => {
      val newPoint = new Point(point.x, point.y)
      if (lastPoint == null)
        lastPoint = newPoint
      canvas.drawLine(new Line(lastPoint, newPoint, Color.green))
      lastPoint = newPoint
    }
    // react to events
    reactions += {
      case MouseDragged(_,point,_) => lineDrawingEvt(point)
      case MouseClicked(_,point,_,_,_) => lineDrawingEvt(point)
      case _:MouseReleased | _:MouseExited => {
        lastPoint = null
      }
    }
  }
  println("board opened")
}


case class Line(val point1:Point, val point2:Point, val color: java.awt.Color)


class Canvas extends Panel {

  var lines = List[Line]()

  override def paintComponent(g: Graphics2D) {

    // Start by erasing this Canvas

    g.clearRect(0, 0, size.width, size.height)

    // Draw things that change on top of background
    for (line <- lines) {
      g.setColor(line.color)
      g.setStroke(new BasicStroke(5))
      g.drawLine(line.point1.x, line.point1.y, line.point2.x, line.point2.y)
    }
  }


  def drawLine(line: Line): Unit = {
    lines = lines :+ line
    repaint
  }
}
