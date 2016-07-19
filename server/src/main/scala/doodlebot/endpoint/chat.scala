package doodlebot
package endpoint

import cats.data.Xor
import cats.syntax.xor._
import io.finch._
import com.twitter.util.Base64StringEncoder
import doodlebot.action.Store
import java.util.UUID

object Chat {
  import doodlebot.model._

  val authorized: Endpoint0 =
    header("authorization") { header: String =>
      val authorized =
        header.split(" ", 2) match {
          case Array(scheme, params) =>
            if(scheme.toLowerCase == "basic") {
              new String(Base64StringEncoder.decode(params)).split(":", 2) match {
                case Array(name, session) =>
                  try {
                    val n = Name(name)
                    val s = Session(UUID.fromString(session))
                    Store.authenticated(n, s)
                  } catch {
                    case exn: IllegalArgumentException =>
                      false
                  }

                case _ => false
              }
            } else {
              false
            }

          case _ => false
        }

      if(authorized)
        Ok(shapeless.HNil : shapeless.HNil)
      else
        NotAcceptable(BasicAuthFailed)
    }

  val message: Endpoint[FormErrors Xor Unit] =
    post("message" :: authorized :: param("name") :: param("message")) { (name: String, message: String) =>
      val msg = model.Message(name, message)
      Store.message(msg)
      Ok(().right[FormErrors])
    }

  val poll: Endpoint[FormErrors Xor Log] =
    post("poll" :: authorized :: param("offset").as[Int]) { (offset: Int) =>
      val log = Store.poll(offset)
      Ok(log.right[FormErrors])
    }
}
