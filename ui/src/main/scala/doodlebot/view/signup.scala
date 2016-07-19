package doodlebot
package view

import doodlebot.form.Input
import doodlebot.model.Model
import doodlebot.message.Message
import org.scalajs.dom
import scala.scalajs.js

object Signup {
  import doodlebot.virtualDom._
  import doodlebot.virtualDom.Dom._

  object email {
    val name = "email"
    val selector = s"#signup input[name=$name]"
  }
  object name {
    val name = "name"
    val selector = s"#signup input[name=$name]"
  }
  object password {
    val name = "password"
    val selector = s"#signup input[name=$name]"
  }

  def onSignUp(event: dom.Event): Unit = {
    event.preventDefault()

    val e = dom.document.querySelector(email.selector).asInstanceOf[dom.html.Input].value
    val u = dom.document.querySelector(name.selector).asInstanceOf[dom.html.Input].value
    val p = dom.document.querySelector(password.selector).asInstanceOf[dom.html.Input].value

    val payload = js.Dictionary("email" -> e, "name" -> u, "password" -> p)
    val success = Message.Authenticated.deserialize _
    val failure =
      (errors: Map[String, List[String]]) => Message.SignupError(errors)

    val effect = Effect.request("/signup", payload, success, failure)
    Effect.run(effect)
  }

  def render(signup: Model.Signup): VTree = {
    dom.console.log(s"Rendering $signup")
    element("div#signup")(
        h2("Sign Up"),
        form("onsubmit":=eventHandler(onSignUp _))(
             Input.email(
               name=email.name,
               placeholder="Your email address",
               value=signup.email,
               help=signup.errors.get(email.name).map(_.mkString(" ")).getOrElse(""),
               onInput=(e) => Effect.message(Message.Signup(signup.copy(email=e)))
             ),
             Input.text(
               name=name.name,
               placeholder="Your name",
               value=signup.name,
               help=signup.errors.get(name.name).map(_.mkString(" ")).getOrElse(""),
               onInput=(u) => Effect.message(Message.Signup(signup.copy(name=u)))
             ),
             Input.password(
               name=password.name,
               placeholder="Your password",
               value=signup.password,
               help=signup.errors.get(password.name).map(_.mkString(" ")).getOrElse(""),
               onInput=(p) => Effect.message(Message.Signup(signup.copy(password=p)))
             ),
             button("type":="submit")("Sign up")
        )
    )
  }
}
