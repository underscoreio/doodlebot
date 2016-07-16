package doodlebot
package model

sealed abstract class Model extends Product with Serializable
object Model {
  final case class NotAuthenticated(signup: Signup) extends Model
  final case class Authenticated(username: UserName, credentials: Credentials) extends Model

  final case class UserName(get: String) extends AnyVal
  final case class Credentials(get: String) extends AnyVal
  final case class Signup(email: String, userName: String, password: String, errors: Map[String, List[String]] = Map.empty) {
    def withErrors(errors: Map[String, List[String]]): Signup =
      this.copy(errors = errors)
  }
  object Signup {
    val empty = Signup("","","")
  }

}
