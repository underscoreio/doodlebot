package doodlebot

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

object DoodleBot extends js.JSApp {
  import doodlebot.model._

  var model: Model = NotAuthenticated(Signup.empty)

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
    evt.preventDefault()

    val email = dom.document.querySelector("#signup input[name=email]").asInstanceOf[dom.html.Input].value
    val username = dom.document.querySelector("#signup input[name=username]").asInstanceOf[dom.html.Input].value
    val password = dom.document.querySelector("#signup input[name=password]").asInstanceOf[dom.html.Input].value

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
