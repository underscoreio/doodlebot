package doodlebot

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.jquery

sealed abstract class Effect extends Product with Serializable
object Effect {
  final case object NoEffect extends Effect
  final case class Message(message: doodlebot.message.Message) extends Effect
  final case class Request(
    path: String,
    payload: js.Dictionary[String],
    success: js.Dictionary[String] => message.Message,
    failure: Map[String, List[String]] => message.Message
  ) extends Effect

  def run(effect: Effect): Unit =
    effect match {
      case NoEffect =>
        ()

      case Message(message) =>
        DoodleBot.loop(message)

      case Request(path, payload, success, failure) =>
        dom.console.log("Sending", payload, " to ", path)
        val callback = (data: js.Dictionary[js.Any]) => {
          dom.console.log("Ajax request succeeded with", data)
          val message =
            data.get("error").map { errors =>
              val raw =
                errors.asInstanceOf[js.Dictionary[js.Dictionary[js.Dictionary[js.Array[String]]]]]
              val converted = raw("errors")("messages").toMap.mapValues(_.toList)
              failure(converted)
            }.getOrElse { success(data("success").asInstanceOf[js.Dictionary[String]]) }
          DoodleBot.loop(message)
        }
        jquery.jQuery.post(path, payload, callback, "json")
    }
}
