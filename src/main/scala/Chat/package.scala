import java.awt.Color

package object Chat {
  def getColor(name:String):Color = {
    var color:Color = null
    try {
      return Class.forName("java.awt.Color").getField(name).get(null).asInstanceOf[Color]
    } catch {
      case e: Exception => return Color.green; // Not defined
    }
  }
}
