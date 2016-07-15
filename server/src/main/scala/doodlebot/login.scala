package doodlebot

import io.finch._

object Login {
  import model._

  val login: Endpoint[Authenticated] = post("login") {
    Ok(Authenticated("foo", "credentials")).withContentType(Some("application/json"))
  }
}
