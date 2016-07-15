package doodlebot
package model

sealed abstract class Model extends Product with Serializable 
final case class NotAuthenticated(signup: Signup) extends Model
final case class Authenticated(username: UserName, credentials: Credentials) extends Model

final case class UserName(get: String) extends AnyVal
final case class Credentials(get: String) extends AnyVal
final case class Signup(userName: String, email: String, password: String)
object Signup {
  val empty = Signup("","","")
}
