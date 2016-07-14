package doodlebot

import io.finch._

object Login {
  import Model._

  val login: Endpoint[Authenticated] = post("login") {
    Ok(Authenticated("foo", "credentials")).withContentType(Some("application/json"))
  }
}
