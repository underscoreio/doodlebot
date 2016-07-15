package doodlebot
package view

import doodlebot.form.Input
import org.scalajs.dom

object Signup {
  import scalatags.JsDom.all._

  def render(signup: model.Signup): dom.Element =
    div(id:="signup",
        h2("Sign Up"),
        form(onsubmit:=(DoodleBot.onSignUp _),
             Input.email(name="email", placeholder="Your email address", value=signup.email),
             Input.text(name="username", placeholder="Your username", value=signup.userName),
             Input.password(name="password", placeholder="Your password", value=signup.password),
             button(`type`:="submit", "Sign up")
        )
    ).render
}
