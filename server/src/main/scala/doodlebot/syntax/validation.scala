package doodlebot
package syntax

import cats.data.{Validated,ValidatedNel}

object validation {
  import doodlebot.validation._

  implicit class ValidationOps[A](a: A) {
    def validate(predicate: Predicate[A]): ValidatedNel[String,A] =
      predicate(a)
  }

  implicit class FormValidatedOps[A](validated: ValidatedNel[String,A]) {
    def forInput(id: String): Validated[InputError,A] =
      validated.leftMap { messages => InputError(id, messages) }
  }
}
