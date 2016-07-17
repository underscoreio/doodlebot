package doodlebot

import cats.data.Xor
import com.twitter.finagle.Http
import io.circe.Encoder
import io.circe.generic.auto._
import io.finch.circe._

object DoodleBot {
  import doodlebot.endpoint._

  val service = Static.static :+: Signup.signup :+: Login.login

  implicit def xorEncoder[A: Encoder,B: Encoder]: Encoder[Xor[A,B]] =
    Encoder.encodeXor("error", "success")

  val server = Http.serve(":8080", service.toService)
}
