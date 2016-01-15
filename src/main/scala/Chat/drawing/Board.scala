package Chat.drawing

/**
 * Created by Mat Hek on 2016-01-15.
 */

import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D }

import scala.swing._
object Board extends App
{
  new Frame {
    visible = true
    title = "Board"

    val canvas = new Canvas {
      preferredSize = new Dimension(100, 100)
    }
    contents = new BorderPanel {
      layout(canvas) = Center
    }
    size = new Dimension(600, 400)


    // specify which Components produce events of interest
    listenTo(canvas.mouse.clicks, canvas.mouse.moves)

    // react to events
    reactions += {
      case MouseClicked(_, point, _, _, _) =>
        canvas.drawPoint(new Point(point.x, point.y, Color.green))
      case MouseDragged(_,point,_) =>
        canvas.drawPoint(new Point(point.x, point.y, Color.green))
    }
  }
  println("board closed")
}


case class Point(val x: Int, val y: Int, val color: java.awt.Color)


class Canvas extends Panel {

  var points = List[Point]()

  override def paintComponent(g: Graphics2D) {

    // Start by erasing this Canvas
    g.clearRect(0, 0, size.width, size.height)


    // Draw things that change on top of background
    for (point <- points) {
      g.setColor(point.color)
      g.fillOval(point.x, point.y, 10, 10)
    }
  }

  /** Add a "dart" to list of things to display */
  def drawPoint(point: Point) {
    points = points :+ point
    // Tell Scala that the display should be repainted
    repaint()
  }
}
