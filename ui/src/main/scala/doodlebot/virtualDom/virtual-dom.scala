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

  //val h: H =
    //js.Dynamic.global.h.asInstanceOf[H]
  //val createElement: VTree => dom.Element = {
    //val raw = js.Dynamic.global.createElement.asInstanceOf[(VTree, js.Any) => dom.Element]
    //(vTree: VTree) => raw(vTree, dom.document)
    ////(vTree: VTree) => ???
  //}
  //val diff: (VTree, VTree) => Array[VPatch] =
    ////js.Dynamic.global.diff.asInstanceOf[(VTree, VTree) => Array[VPatch]]
    //(a: VTree, b: VTree) => ???
  //val patch: (dom.Element, Array[VPatch]) => dom.Element = {
    ////val raw =
      ////js.Dynamic.global.patch.asInstanceOf[(dom.Element, Array[VPatch], js.Any) => dom.Element]
    ////(elt: dom.Element, patch: Array[VPatch]) => raw(elt, patch, js.Object())
    //(elt: dom.Element, patch: Array[VPatch]) => ???
  //}

}
