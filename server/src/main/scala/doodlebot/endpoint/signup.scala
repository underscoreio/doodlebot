package doodlebot
package endpoint

import io.finch._
import cats.data.Xor
import cats.syntax.cartesian._
import cats.syntax.xor._

object Signup {
  import model._

  val signup: Endpoint[FormErrors Xor Authenticated] =
    post("signup" :: param("userName") :: param("email") :: param("password")) { (userName: String, email: String, password: String) =>
      import doodlebot.syntax.validation._

      (UserName.validate(userName).forInput("username") |@|
         Email.validate(email).forInput("email") |@|
         Password.validate(password).forInput("password")).map { (n, e, p) =>
        val user = User(n, e, p)
        Authenticated(n.get, "credentials")
      }.fold(
        fe = errors => Ok(FormErrors(errors).left),
        fa = auth => Ok(auth.right).withContentType(Some("application/json"))
      )
  }
}
