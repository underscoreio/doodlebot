package doodlebot
package view

import doodlebot.form.Input
import org.scalajs.dom

object Signup {
  import scalatags.JsDom.all._

  def render: dom.Element =
    div(`class`:="signup",
        h2("Sign Up"),
        form(onsubmit:=(DoodleBot.onSignUp _),
             Input.email(name="signup-email", placeholder="Your email address"),
             Input.text(name="signup-username", placeholder="Your username"),
             Input.password(name="signup-password", placeholder="Your password"),
             button(`type`:="submit", "Sign up")
        )
    ).render
}
