package doodlebot

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

object DoodleBot extends js.JSApp {
  var model: Model = NotAuthenticated

  def main(): Unit = {
    val rendered = Circuit.render(model)
    this.render(rendered)
  }

  def loop(action: Action): Unit = {
    val (newModel, effect) = Circuit.act(action, model)
    model = newModel
    this.render(Circuit.render(newModel))
    Effect.run(effect)
  }

  def render(content: dom.Element): Unit = {
    val root = dom.document.getElementById("app")
    root.removeChild(root.firstChild)
    root.appendChild(content)
  }

  @JSExport
  def onSignUp(evt: dom.Event): Unit = {
    val email = dom.document.getElementById("signup-email").asInstanceOf[dom.html.Input].value
    val username = dom.document.getElementById("signup-username").asInstanceOf[dom.html.Input].value
    val password = dom.document.getElementById("signup-password").asInstanceOf[dom.html.Input].value

    evt.preventDefault()

    val action = Action.Signup(email, username, password)
    loop(action)
  }

  @JSExport
  def onLogin(evt: dom.Event): Unit = {
    val username = dom.document.getElementById("login-username").asInstanceOf[dom.html.Input].value
    val password = dom.document.getElementById("login-password").asInstanceOf[dom.html.Input].value

    evt.preventDefault()

    val action = Action.Login(username, password)
    loop(action)
  }
}
