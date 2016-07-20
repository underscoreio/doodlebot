package doodlebot
package action

import cats.data.{NonEmptyList,ValidatedNel,Xor}
import cats.std.list._
import cats.std.option._
import cats.syntax.validated._
import cats.syntax.cartesian._
import cats.syntax.xor._

object Store {
  import doodlebot.model._
  import scala.collection.mutable

  sealed abstract class SignupError extends Product with Serializable
  final case class EmailAlreadyExists(email: Email) extends SignupError
  final case class NameAlreadyExists(name: Name) extends SignupError

  sealed abstract class LoginError extends Product with Serializable
  final case class NameDoesNotExist(name: Name) extends LoginError
  final case object PasswordIncorrect extends LoginError

  def emailAlreadyExists(email: Email): SignupError =
    EmailAlreadyExists(email)

  def nameAlreadyExists(name: Name): SignupError =
    NameAlreadyExists(name)

  def nameDoesNotExist(name: Name): LoginError =
    NameDoesNotExist(name)

  val passwordIncorrect: LoginError =
    PasswordIncorrect


  private var emails: mutable.Set[Email] = mutable.Set.empty
  private var names: mutable.Set[Name] = mutable.Set.empty
  private var accounts: mutable.Map[Name, User] = mutable.Map.empty
  private var sessionsBySession: mutable.Map[Session, Name] = mutable.Map.empty
  private var sessionsByName: mutable.Map[Name, Session] = mutable.Map.empty
  // This will gobble memory without limit, but is ok for our simple use case
  private var messages: mutable.ArrayBuffer[Message] = new mutable.ArrayBuffer(1024)

  def signup(user: User): Xor[NonEmptyList[SignupError],Session] = {
    Store.synchronized {
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
        makeSession(user.name)
      }.toXor
    }
  }

  def login(login: Login): Xor[LoginError,Session] = {
    Store.synchronized {
      nameDoesNotExist(login.name).left[Session]
    }
  }

  def makeSession(name: Name): Session = {
    Store.synchronized {
      sessionsByName.get(name).getOrElse {
        val session = Session()
        sessionsByName += (name -> session)
        sessionsBySession += (session -> name)
        session
      }
    }
  }

  def poll(offset: Int): Log =
    Store.synchronized {
      if(offset > messages.size)
        Log(messages.length, List.empty)
      else {
        Log(messages.length, messages.takeRight(messages.length - offset).toList)
      }
    }

  def message(message: Message): Unit =
    Store.synchronized {
      messages += message
    }

  def authenticated(name: Name, session: Session): Boolean =
    Store.synchronized {
      (sessionsBySession.get(session) |@| sessionsByName.get(name)).map { (n, s) =>
        n == name && s == session
      }.getOrElse(false)
    }
}
