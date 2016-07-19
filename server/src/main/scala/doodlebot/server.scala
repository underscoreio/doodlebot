package doodlebot

import com.twitter.finagle.Http
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

object DoodleBot {
  import doodlebot.endpoint._
  import doodlebot.model.FormErrors

  implicit val encodeException: Encoder[Exception] = Encoder.instance {
    case fe @ FormErrors(_) => fe.asJson
    case e => Json.obj(
      "message" -> Json.fromString(s"""Exception was thrown: ${e.getMessage}""")
    )
  }

  val service =
    Static.static :+: Signup.signup :+: Login.login :+: Chat.message :+: Chat.poll

  val server = {
    import doodlebot.model._
    import doodlebot.action.Store
    Store.signup(
      User(Name("tester"), Email("tester@example.com"), Password("password"))
    )
    Http.serve(":8080", service.toServiceAs[Application.Json])
  }

}
