package doodlebot

import cats.data.Xor
import com.twitter.finagle.Http
import io.circe.Encoder
import io.circe.generic.auto._
import io.finch.circe._

object DoodleBot {
  import doodlebot.endpoint._

  val service =
    Static.static :+: Signup.signup :+: Login.login :+: Chat.message :+: Chat.poll

  implicit def xorEncoder[A: Encoder,B: Encoder]: Encoder[Xor[A,B]] =
    Encoder.encodeXor("error", "success")

  val server = {
    import doodlebot.model._
    import doodlebot.action.Store
    Store.signup(
      User(Name("tester"), Email("tester@example.com"), Password("password"))
    )
    Http.serve(":8080", service.toService)
  }

}
