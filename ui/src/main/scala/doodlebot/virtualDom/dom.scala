package doodlebot
package virtualDom

import scala.scalajs.js
import org.scalajs.dom

object Dom {

  /** Utility type definition */
  type EventHandler = js.Function1[dom.Event,Unit]

  def eventHandler[A](f: dom.Event => Unit): EventHandler =
    f

  final case class DomAttribute(name: String, value: js.Any)
  implicit class DomAttributeOps(name: String) {
    def :=(value: String): DomAttribute =
      DomAttribute(name, value)

    def :=(value: EventHandler): DomAttribute =
      DomAttribute(name, eventHandler(value))
  }

  import js.JSConverters._

  final case class Element(name: String) {
    class WithAttributes(attrs: js.Dictionary[js.Any]) {
      def apply(): VTree = {
        VirtualDom.h(name, attrs, js.Array[VTree]())
      }

      def apply(child: VTree*): VTree = {
        VirtualDom.h(name, attrs, child.toJSArray)
      }

      def apply(text: String): VTree = {
        VirtualDom.h(name, attrs, text)
      }
    }

    def apply(attr: DomAttribute*): WithAttributes = {
      val attrs = (js.Dictionary.apply(attr.map { case DomAttribute(k,v) => k -> v }:_*))
      new WithAttributes(attrs)
    }

    def apply(child: VTree*): VTree = {
      VirtualDom.h(name, js.Dictionary[js.Any](), child.toJSArray)
    }

    def apply(text: String): VTree = {
      VirtualDom.h(name, js.Dictionary[js.Any](), text)
    }
  }

  /** Generic utility */
  def element(name: String): Element =
    Element(name)

  val div = Element("div")
  val span = Element("span")
  val h1 = Element("h1")
  val h2 = Element("h2")
  val form = Element("form")
  val p = Element("p")
  val input = Element("input")
  val button = Element("button")
}
