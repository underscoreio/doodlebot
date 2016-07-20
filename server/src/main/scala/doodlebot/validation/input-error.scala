package doodlebot
package validation

import cats.Monoid
import cats.data.NonEmptyList
//import cats.std.list._
//import cats.std.map._
//import cats.syntax.semigroup._

final case class InputError(messages: Map[String,NonEmptyList[String]])
object InputError {
  val empty: InputError = InputError(Map.empty)

  def apply(name: String, messages: NonEmptyList[String]): InputError =
    InputError(Map(name -> messages))

  def apply(name: String, message: String): InputError =
    InputError(Map(name -> NonEmptyList(message)))

  implicit object inputErrorInstances extends Monoid[InputError] {
    override def combine(a1: InputError, a2: InputError): InputError =
      ???

    override def empty: InputError =
      ???
  }
}
