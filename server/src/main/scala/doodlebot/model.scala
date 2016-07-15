package doodlebot

import cats.data.{NonEmptyList,ValidatedNel}
import cats.std.list._
import cats.syntax.cartesian._

object model {
  import doodlebot.validation._
  import doodlebot.syntax.validation._
  import doodlebot.validation.Predicate._

  // Messages
  final case class Authenticated(userName: String, credentials: String)
  final case class Errors(errors: NonEmptyList[String])
  final case class FormErrors(errors: NonEmptyList[InputError])

  // Wrappers
  final case class UserName(get: String) extends AnyVal
  object UserName {
    def validate(userName: String): ValidatedNel[String,UserName] = {
      userName.validate(lengthAtLeast(6) and onlyLettersOrDigits).map(n => UserName(n))
    }
  }
  final case class Email(get: String) extends AnyVal
  object Email {
    def validate(email: String): ValidatedNel[String,Email] = {
      email.validate(containsAllChars("@.")).map(e => Email(e))
    }
  }
  final case class Password(get: String) extends AnyVal
  object Password {
    def validate(password: String): ValidatedNel[String,Password] = {
      password.validate(lengthAtLeast(8)).map(p => Password(p))
    }
  }

  // State
  final case class User(userName: UserName, email: Email, password: Password)
  object User {
    def validate(userName: String, email: String, password: String): ValidatedNel[String,User] = {
      (UserName.validate(userName) |@| Email.validate(email) |@| Password.validate(password)).map { User.apply _ }
    }
  }
}
