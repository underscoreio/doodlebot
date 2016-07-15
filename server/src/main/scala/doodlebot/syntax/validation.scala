package doodlebot
package syntax

import cats.data.{Validated,ValidatedNel,NonEmptyList}

object validation {
  import doodlebot.validation._

  implicit class ValidationOps[A](a: A) {
    def validate(predicate: Predicate[A]): Validated[NonEmptyList[String],A] =
      predicate(a)
  }

  implicit class FormValidatedOps[A](validated: Validated[NonEmptyList[String],A]) {
    def forInput(id: String): ValidatedNel[InputError,A] =
      validated.leftMap { messages => NonEmptyList(InputError(id, messages)) }
  }
}
