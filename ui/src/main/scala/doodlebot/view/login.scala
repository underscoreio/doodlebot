package doodlebot
package view

import doodlebot.form.Input
import doodlebot.model.Model
import doodlebot.message.Message
import org.scalajs.dom
import scala.scalajs.js

object Login {
  import doodlebot.virtualDom._
  import doodlebot.virtualDom.Dom._

  object name {
    val name = "name"
    val selector = s"#login input[name=$name]"
  }
  object password {
    val name = "password"
    val selector = s"#login input[name=$name]"
  }

  def onLogin(event: dom.Event): Unit = {
    event.preventDefault()

    val n = dom.document.querySelector(name.selector).asInstanceOf[dom.html.Input].value
    val p = dom.document.querySelector(password.selector).asInstanceOf[dom.html.Input].value

    val payload = js.Dictionary("name" -> n, "password" -> p)
    val success = Message.Authenticated.deserialize _
    val failure =
      (errors: Map[String, List[String]]) => Message.LoginError(errors)

    val effect = Effect.Request("/login", payload, success, failure)
    Effect.run(effect)
  }

  def render(login: Model.Login): VTree = {
    dom.console.log(s"Rendering $login")
    element("div#login")(
        h2("Login"),
        form("onsubmit":=eventHandler(onLogin _))(
             Input.text(
               name=name.name,
               placeholder="Your name",
               value=login.name,
               help=login.errors.get(name.name).map(_.mkString(" ")).getOrElse(""),
               onInput=(n) => Effect.message(Message.Login(login.copy(name=n)))
             ),
             Input.password(
               name=password.name,
               placeholder="Your password",
               value=login.password,
               help=login.errors.get(password.name).map(_.mkString(" ")).getOrElse(""),
               onInput=(p) => Effect.message(Message.Login(login.copy(password=p)))
             ),
             button("type":="submit")("Login")
        )
    )
  }
}
