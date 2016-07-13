package doodlebot

sealed abstract class Action 
object Action {
  final case class Login(userName: String, password: String) extends Action
  final case class Signup(email: String, userName: String, password: String) extends Action
  final case class Authenticated(userName: String, credentials: String) extends Action
  final case object Logout extends Action
}
