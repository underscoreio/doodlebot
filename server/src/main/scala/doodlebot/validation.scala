package doodlebot

import cats.data.{NonEmptyList,Validated,ValidatedNel}
import cats.std.list._
import cats.syntax.validated._
import cats.syntax.semigroup._

object Validation {
  type Result[A] = ValidatedNel[String,A]

  final case class Predicate[A](messages: NonEmptyList[String], check: A => Boolean) {
    def apply(a: A): Result[A] =
      if(check(a))
        a.validNel
      else
        Validated.invalid(messages)

    def and(that: Predicate[A]): Predicate[A] =
      Predicate(this.messages |+| that.messages, (a: A) => this.check(a) && that.check(a))
  }
  object Predicate {
    def lift[A](message: String)(f: A => Boolean): Predicate[A] =
      Predicate(NonEmptyList(message), f)
  }

  def lengthAtLeast(length: Int): Predicate[String] =
    Predicate.lift(s"Must be at least $length characters."){ string =>
      string.length >= length
    }

  val onlyLettersOrDigits: Predicate[String] =
    Predicate.lift("Must contain only letters or digits."){ string =>
      string.forall(_.isLetterOrDigit)
    }

  def containsAllChars(chars: String): Predicate[String] =
    Predicate.lift(s"Must contain all of $chars"){ string =>
      val chs = chars.toList
      chs.foldLeft(true){ (accum, elt) =>
        accum && string.contains(elt)
      }
    }

  object syntax {
    implicit class ValidationOps[A](a: A) {
      def validate(predicate: Predicate[A]): Result[A] =
        predicate(a)
    }
  }
}
