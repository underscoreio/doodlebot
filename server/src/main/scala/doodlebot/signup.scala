package doodlebot

import io.finch._
import cats.data.Xor
import cats.std.list._
import cats.syntax.cartesian._
import cats.syntax.xor._

object Signup {
  import model._

  val signup: Endpoint[FormErrors Xor Authenticated] =
    post("signup" :: param("userName") :: param("email") :: param("password")) { (userName: String, email: String, password: String) =>
      import doodlebot.syntax.validation._

      (UserName.validate(userName).forInput("signup-username") |@|
         Email.validate(email).forInput("signup-email") |@|
         Password.validate(password).forInput("signup-password")).map { (n, e, p) =>
        val user = User(n, e, p)
        Authenticated(n.get, "credentials")
      }.fold(
        fe = errors => Ok(FormErrors(errors).left),
        fa = auth => Ok(auth.right).withContentType(Some("application/json"))
      )
  }

  object command {
    import scala.collection.mutable

    sealed abstract class SignupError extends Product with Serializable
    final case class UserAlreadyExists(userName: UserName) extends SignupError

    val signups: mutable.Map[UserName, User] = mutable.Map.empty

    def signup(user: User): Xor[SignupError, User] = {
      ???
    }
  }
}
