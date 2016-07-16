package doodlebot

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import doodlebot.virtualDom._

object DoodleBot extends js.JSApp {
  import doodlebot.model._
  import doodlebot.message._

  var model: Model = Model.NotAuthenticated(Model.Signup.empty)
  var current: VTree = null
  var rendered: dom.Element = null

  def main(): Unit = {
    current = Circuit.render(model)
    rendered = VirtualDom.createElement(current)
    val root = dom.document.getElementById("app")
    root.removeChild(root.firstChild)
    root.appendChild(rendered)
  }

  def loop(message: Message): Unit = {
    val newModel = Circuit.update(message, model)
    model = newModel
    this.render(Circuit.render(newModel))
  }

  def render(update: VTree): Unit = {
    val patches = VirtualDom.diff(current, update)
    rendered = VirtualDom.patch(rendered, patches)
    current = update
  }

  @JSExport
  def onLogin(evt: dom.Event): Unit = {
    val username = dom.document.getElementById("login-username").asInstanceOf[dom.html.Input].value
    val password = dom.document.getElementById("login-password").asInstanceOf[dom.html.Input].value

    evt.preventDefault()

    val message = Message.NotAuthenticated
    loop(message)
  }
}
