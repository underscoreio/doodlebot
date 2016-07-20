package doodlebot
package endpoint

import io.finch._
import cats.data.Xor
import cats.syntax.xor._
import doodlebot.action.Store
import doodlebot.validation.InputError

object Login {
  import doodlebot.model._

  val login: Endpoint[Authenticated] = post("login" :: param("name") :: param("password")) { (name: String, password: String) =>
    val login = model.Login(Name(name), Password(password))
    val result: Xor[FormErrors,Authenticated] =
      Store.login(login).fold(
        fa = error => {
          FormErrors(
            error match {
              case Store.NameDoesNotExist(name) =>
                InputError("name", "Nobody has signed up with this name")
              case Store.PasswordIncorrect =>
                InputError("password", "Your password is incorrect")
            }
          ).left
        },

        fb = session => {
          Authenticated(name, session.get.toString).right
        }
      )

    result.fold(BadRequest, Ok)
  }
}
