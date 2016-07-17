package doodlebot
package virtualDom

import scala.scalajs.js
import org.scalajs.dom

@js.native
trait VTree extends js.Object {}

@js.native
trait VPatch extends js.Object {}

@js.native
trait H extends js.Object {
  def apply(selector: String, children: js.Array[VTree]): VTree
  def apply(selector: String, properties: js.Dictionary[js.Any],  children: js.Array[VTree]): VTree
  def apply(selector: String, text: String): VTree
  def apply(selector: String, properties: js.Dictionary[js.Any], text: String): VTree
}

@js.native
object VirtualDom extends js.GlobalScope {
  val h: H = js.native
  def createElement(vtree: VTree): dom.Element = js.native
  def diff(a: VTree, b: VTree): js.Array[VPatch] = js.native
  def patch(elt: dom.Element, patches: js.Array[VPatch]): dom.Element = js.native
}
