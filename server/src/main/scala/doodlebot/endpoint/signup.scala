package doodlebot
package endpoint

import io.finch._
import cats.data.Xor
import cats.std.list._
import cats.syntax.cartesian._
import cats.syntax.semigroup._
import cats.syntax.xor._
import doodlebot.action.Store
import doodlebot.validation.InputError

object Signup {
  import model._

  val signup: Endpoint[Authenticated] =
    post("signup" :: param("name") :: param("email") :: param("password")) { (name: String, email: String, password: String) =>
      import doodlebot.syntax.validation._

      val validatedUser: Xor[FormErrors,User] =
        (
          Name.validate(name).forInput("name") |@|
          Email.validate(email).forInput("email") |@|
          Password.validate(password).forInput("password")
        ).map { (n, e, p) => User(n, e, p) }.toXor.leftMap(errs => FormErrors(errs))

      val result: Xor[FormErrors,Authenticated] =
        validatedUser.flatMap { user =>
          Store.signup(user).fold(
            fa = errors => {
              val errs =
                errors.foldLeft(InputError.empty){ (accum, elt) =>
                  elt match {
                    case Store.EmailAlreadyExists(email) =>
                      accum |+| InputError("email", "This email is already taken")
                    case Store.NameAlreadyExists(name) =>
                      accum |+| InputError("name", "This name is already taken")
                  }
              }

              FormErrors(errs).left
            },
            fb = session => {
              Authenticated(name, session.get.toString).right
            }
          )
        }

      result.fold(BadRequest, Ok)
    }
}
