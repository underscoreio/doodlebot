package doodlebot

import cats.data.Validated

object FormValidation {
  final case class Error(element: String, message: String)

  type FormErrors = List[Error]
  type Result[A] = Validated[FormErrors, A]
}
