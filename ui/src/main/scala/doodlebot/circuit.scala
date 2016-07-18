package doodlebot

import org.scalajs.dom

object Circuit {
  import doodlebot.model._
  import doodlebot.model.Model._
  import doodlebot.message._
  import doodlebot.virtualDom._
  import doodlebot.virtualDom.Dom._

  def update(message: Message, model: Model): Model =
    (message, model) match {
      case (Message.NotAuthenticated, _) =>
        NotAuthenticated(Signup.empty)

      case (Message.Authenticated(u, c), _) =>
        Authenticated(Name(u), Credentials(c))

      case (Message.SignupError(errors), NotAuthenticated(signup)) =>
        NotAuthenticated(signup.withErrors(errors))

      case (Message.Signup(signup), NotAuthenticated(_)) =>
        NotAuthenticated(signup)

      case (_, _) =>
        dom.console.log("Unexpected message and model combination")
        model
    }

  def render(model: Model): VTree = {
    model match {
      case NotAuthenticated(signup) =>
        div(
          h1("DoodleBot"),

          element("div.forms")(
            view.Signup.render(signup),
            element("div#login")(
                h2("Login"),
                form("onsubmit":=(DoodleBot.onLogin _))(
                  input("type":="text", "id":="login-name", "placeholder":="Your name")(),
                  input("type":="password", "id":="login-password", "placeholder":="Your password")(),
                  button("type":="submit")("Login")
                )
            )
          )
        )
      case Authenticated(u, c) =>
        div(
          h1("DoodleBot"),
          p(s"Hi $u. Your secret credentials are $c")
        )
    }
  }
}
