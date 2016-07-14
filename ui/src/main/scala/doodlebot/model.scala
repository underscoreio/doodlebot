package doodlebot

sealed abstract class Model extends Product with Serializable {
  def flash(messages: List[String]): Model =
    this match {
      case na: NotAuthenticated => na.copy(flash = messages)
      case a: Authenticated => a.copy(flash = messages)
    }
}
final case class NotAuthenticated(flash: List[String] = List.empty) extends Model
final case class Authenticated(username: UserName, credentials: Credentials, flash: List[String] = List.empty) extends Model

final case class UserName(get: String) extends AnyVal
final case class Credentials(get: String) extends AnyVal
