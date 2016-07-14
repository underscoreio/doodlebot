package doodlebot

import io.finch._
import cats.data.Xor
import cats.syntax.xor._

object Signup {
  import Model._

  val signup: Endpoint[Errors Xor Authenticated] =
    post("signup" :: param("email") :: param("userName") :: param("password")) { (email: String, userName: String, password: String) =>
      User.validate(userName, email, password).map{ user =>
        Authenticated(userName, "credentials")
      }.fold(
        fe = errors => Ok(Errors(errors).left),
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
