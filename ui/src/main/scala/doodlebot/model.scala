package doodlebot

sealed abstract class Model extends Product with Serializable
final case object NotAuthenticated extends Model
final case class Authenticated(username: UserName, credentials: Credentials) extends Model

final case class UserName(get: String) extends AnyVal
final case class Credentials(get: String) extends AnyVal
