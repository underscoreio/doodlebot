package doodlebot
package endpoint

import io.finch._
import cats.data.Xor
import cats.std.list._
import cats.syntax.semigroup._
import cats.syntax.xor._
import doodlebot.action.Store
import doodlebot.validation.InputError

object Login {
  import doodlebot.model._

  val login: Endpoint[FormErrors Xor Authenticated] = post("login" :: param("name") :: param("password")) { (name: String, password: String) =>
    val login = model.Login(Name(name), Password(password))
    val result: Xor[FormErrors,Authenticated] =
      Store.login(login).fold(
        fe = errors => {
          val errs =
            errors.foldLeft(InputError.empty){ (accum, elt) =>
              elt match {
                case Store.NameDoesNotExist(name) =>
                  accum |+| InputError("name", "Nobody has signed up with this name")
                case Store.PasswordIncorrect =>
                  accum |+| InputError("password", "Your password is incorrect")
              }
            }

          FormErrors(errs).left
        },
        fa = session => {
          Authenticated(name, session.get.toString).right
        }
      )

    Ok(result)
  }
}
