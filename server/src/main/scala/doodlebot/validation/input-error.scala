package doodlebot
package validation

import cats.data.NonEmptyList

final case class InputError(id: String, messages: NonEmptyList[String])
