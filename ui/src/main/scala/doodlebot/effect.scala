package doodlebot

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.jquery

sealed abstract class Effect extends Product with Serializable
object Effect {
  final case object NoEffect extends Effect
  final case class Request(path: String, payload: js.Dictionary[String], deserialize: js.Dictionary[String] => Action) extends Effect

  def run(effect: Effect): Unit =
    effect match {
      case NoEffect =>
        ()
      case Request(path, payload, deserialize) =>
        dom.console.log(s"Sending $payload to $path")
        val success = (data: js.Dictionary[String]) => {
          dom.console.log(s"Ajax request succeeded $data")
          DoodleBot.loop(deserialize(data))
        }
        jquery.jQuery.post(path, payload, success, "json")
    }
}
