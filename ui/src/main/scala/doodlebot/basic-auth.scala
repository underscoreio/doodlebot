package doodlebot

import org.scalajs.dom

object BasicAuth {
  def header(name: String, password: String): (String, String) = {
    val encoded = dom.window.btoa(s"$name:$password")
    "Authorization" -> s"Basic $encoded"
  }
}
