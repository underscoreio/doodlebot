package doodlebot

import scala.scalajs.js
import org.scalajs.dom

object Circuit {
  def act(action: Action, model: Model): (Model, Effect) =
    action match {
      case Action.Logout =>
        (NotAuthenticated, Effect.NoEffect)

      case Action.Signup(e, u, p) =>
        val payload = js.Dictionary("email" -> e, "userName" -> u, "password" -> p)
        val deserialize = (data: js.Dictionary[String]) => {
          Action.Authenticated(data("userName"), data("credentials"))
        }

        (NotAuthenticated, Effect.Request("/signup", payload, deserialize))

      case Action.Login(u, p) =>
        dom.console.log(action.toString)
        (model, Effect.NoEffect)

      case Action.Authenticated(u, c) =>
        (Authenticated(UserName(u), Credentials(c)), Effect.NoEffect)
    }

  def render(model: Model): dom.Element = {
    import scalatags.JsDom.all._

    model match {
      case NotAuthenticated =>
        div(
          h1("DoodleBot"),

          div(`class`:="forms",
            div(`class`:="signup",
                h2("Sign Up"),
                form(onsubmit:=(DoodleBot.onSignUp _),
                  input(`type`:="email", id:="signup-email", placeholder:="Your email address"),
                  input(`type`:="text",  id:="signup-username", placeholder:="Your username"),
                  input(`type`:="password", id:="signup-password", placeholder:="Your password"),
                  button(`type`:="submit", "Sign up")
                )
            ),

            div(`class`:="login",
                h2("Login"),
                form(onsubmit:=(DoodleBot.onLogin _),
                  input(`type`:="text",  id:="login-username", placeholder:="Your username"),
                  input(`type`:="password", id:="login-password", placeholder:="Your password"),
                  button(`type`:="submit",  "Login")
                )
            )
          )
        ).render
      case Authenticated(u, c) =>
        div(
          h1("DoodleBot"),
          p(s"Hi $u. Your secret credentials are $c")
        ).render
    }
  }
}
