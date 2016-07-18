package doodlebot
package endpoint

import cats.data.Xor
import cats.syntax.xor._
import io.finch._
import doodlebot.action.Store

object Chat {
  import doodlebot.model._

  val message: Endpoint[FormErrors Xor Unit] =
    post("message" :: param("message")) { (message: String) =>
      val msg = model.Message("foo", message)
      Store.message(msg)
      Ok(().right[FormErrors]).withContentType(Some("application/json"))
    }

  val poll: Endpoint[FormErrors Xor Log] =
    post("poll" :: param("offset").as[Int]) { (offset: Int) =>
      val log = Store.poll(offset)
      Ok(log.right[FormErrors]).withContentType(Some("application/json"))
    }
}
