package doodlebot
package model

sealed abstract class Model extends Product with Serializable
object Model {
  final case class NotAuthenticated(signup: Signup, login: Login) extends Model
  final case class Authenticated(name: String, session: String, chat: view.Chat.Model) extends Model

  final case class Signup(email: String, name: String, password: String, errors: Map[String, List[String]] = Map.empty) {
    def withErrors(errors: Map[String, List[String]]): Signup =
      this.copy(errors = errors)
  }
  object Signup {
    val empty = Signup("","","")
  }

  final case class Login(name: String, password: String, errors: Map[String, List[String]] = Map.empty) {
    def withErrors(errors: Map[String, List[String]]): Login =
      this.copy(errors = errors)
  }
  object Login {
    val empty = Login("","")
  }
}
