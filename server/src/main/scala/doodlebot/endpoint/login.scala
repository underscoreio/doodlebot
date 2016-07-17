package doodlebot
package endpoint

import io.finch._

object Login {
  import doodlebot.model._

  val login: Endpoint[Authenticated] = post("login") {
    Ok(Authenticated("foo", "credentials")).withContentType(Some("application/json"))
  }
}
