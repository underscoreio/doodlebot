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
        NotAuthenticated(Signup.empty, Login.empty)

      case (Message.Authenticated(u, c), _) =>
        Authenticated(u, c, Chat.empty)

      case (Message.SignupError(errors), NotAuthenticated(signup, login)) =>
        NotAuthenticated(signup.withErrors(errors), login)

      case (Message.LoginError(errors), NotAuthenticated(signup, login)) =>
        NotAuthenticated(signup, login.withErrors(errors))

      case (Message.Signup(signup), NotAuthenticated(_, login)) =>
        NotAuthenticated(signup, login)

      case (Message.Login(login), NotAuthenticated(signup, _)) =>
        NotAuthenticated(signup, login)

      case (Message.Chat(chat), Authenticated(name, session, _)) =>
        Authenticated(name, session, chat)

      case (_, _) =>
        dom.console.log("Unexpected message and model combination")
        model
    }

  def render(model: Model): VTree = {
    model match {
      case NotAuthenticated(signup, login) =>
        div(
          h1("DoodleBot"),

          element("div.forms")(
            view.Signup.render(signup),
            view.Login.render(login)
          )
        )
      case Authenticated(n, s, c) =>
        div(
          h1("DoodleBot"),
          view.Chat.render(n, s, c)
        )
    }
  }
}
