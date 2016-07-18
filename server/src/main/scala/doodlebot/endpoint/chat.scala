package doodlebot
package endpoint

import io.finch._
import doodlebot.action.Store

object Chat {
  import doodlebot.model._

  val message: Endpoint[Unit] =
    post("message" :: param("message")) { (message: String) =>
      val msg = model.Message(Name("foo"), message)
      Store.message(msg)
      Ok(()).withContentType(Some("application/json"))
    }
}
