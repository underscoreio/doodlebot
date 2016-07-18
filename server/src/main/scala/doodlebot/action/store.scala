package doodlebot
package action

import cats.data.ValidatedNel
import cats.std.list._
import cats.syntax.validated._
import cats.syntax.cartesian._

object Store {
  import doodlebot.model._
  import scala.collection.mutable

  sealed abstract class SignupError extends Product with Serializable
  final case class EmailAlreadyExists(email: Email) extends SignupError
  final case class NameAlreadyExists(name: Name) extends SignupError

  def emailAlreadyExists(email: Email): SignupError =
    EmailAlreadyExists(email)

  def nameAlreadyExists(name: Name): SignupError =
    NameAlreadyExists(name)

  private var emails: mutable.Set[Email] = mutable.Set.empty
  private var names: mutable.Set[Name] = mutable.Set.empty
  private var accounts: mutable.Map[Name, User] = mutable.Map.empty

  def signup(user: User): ValidatedNel[SignupError,User] = {
    val emailCheck: ValidatedNel[SignupError,Unit] =
      if(emails(user.email))
        emailAlreadyExists(user.email).invalidNel[Unit]
      else
        ().validNel[SignupError]

    val nameCheck: ValidatedNel[SignupError,Unit] =
      if(names(user.name))
        nameAlreadyExists(user.name).invalidNel[Unit]
      else
        ().validNel

    (emailCheck |@| nameCheck).map { (_,_) =>
      emails += user.email
      names += user.name
      accounts += (user.name -> user)
      user
    }
  }
}
