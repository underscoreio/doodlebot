package doodlebot

import scala.scalajs.js
import org.scalajs.dom

object Circuit {
  import doodlebot.model._

  def act(action: Action, model: Model): (Model, Effect) =
    action match {
      case Action.Error(m) =>
        (model, Effect.NoEffect)

      case Action.Logout =>
        (NotAuthenticated(Signup.empty), Effect.NoEffect)

      case Action.Signup(e, u, p) =>
        val payload = js.Dictionary("email" -> e, "userName" -> u, "password" -> p)
        val deserialize = (data: js.Dictionary[String]) => {
          Action.Authenticated(data("userName"), data("credentials"))
        }

        (NotAuthenticated(Signup(u,e,p)), Effect.Request("/signup", payload, deserialize))

      case Action.Login(u, p) =>
        dom.console.log(action.toString)
        (NotAuthenticated(Signup.empty), Effect.NoEffect)

      case Action.Authenticated(u, c) =>
        (Authenticated(UserName(u), Credentials(c)), Effect.NoEffect)
    }

  def render(model: Model): dom.Element = {
    import scalatags.JsDom.all._

    model match {
      case NotAuthenticated(signup) =>
        div(
          h1("DoodleBot"),

          div(`class`:="forms",
            view.Signup.render(signup),
            div(id:="login",
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
