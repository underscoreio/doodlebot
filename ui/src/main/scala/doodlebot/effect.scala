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
        val success = (data: js.Dictionary[js.Any]) => {
          dom.console.log(s"Ajax request succeeded with $data")
          val action =
            data.get("error").map { errors =>
              Action.Error(errors.asInstanceOf[js.Dictionary[js.Array[String]]]("errors").toList)
            }.getOrElse { deserialize(data("success").asInstanceOf[js.Dictionary[String]]) }
          DoodleBot.loop(action)
        }
        jquery.jQuery.post(path, payload, success, "json")
    }
}
