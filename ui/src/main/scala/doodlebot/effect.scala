package doodlebot

import doodlebot.message.{Message => Msg}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.jquery
import scala.scalajs.js.JSON

sealed abstract class Effect extends Product with Serializable
object Effect {
  final case object NoEffect extends Effect
  final case class Message(message: Msg) extends Effect
  final case class Request(
    path: String,
    payload: js.Dictionary[String],
    success: js.Dictionary[js.Any] => Msg,
    failure: Map[String, List[String]] => Msg,
    headers: List[(String, String)]
  ) extends Effect
  final case class Tick(message: Msg) extends Effect

  val noEffect: Effect =
    NoEffect
  def message(message: Msg): Effect =
    Message(message)
  def request(
    path: String,
    payload: js.Dictionary[String],
    success: js.Dictionary[js.Any] => Msg,
    failure: Map[String, List[String]] => Msg,
    headers: List[(String, String)] = List.empty
  ) =
    Request(path, payload, success, failure, headers)
  def tick(message: Msg): Effect =
    Tick(message)

  def run(effect: Effect): Unit =
    effect match {
      case NoEffect =>
        ()

      case Message(message) =>
        DoodleBot.loop(message)

      case Request(path, payload, success, failure, hdrs) =>
        dom.console.log("Sending", payload, " to ", path, " with headers", hdrs.toString)

        val callbackFailure = (data: js.Dictionary[js.Any]) => {
          dom.console.log("Ajax request failed with", data)

          val raw = data.asInstanceOf[js.Dictionary[js.Dictionary[js.Dictionary[js.Array[String]]]]]
          val converted = raw("errors")("messages").toMap.mapValues(_.toList)

          DoodleBot.loop(failure(converted))
        }

        val callbackSuccess = (data: js.Dictionary[js.Any]) => {
          dom.console.log("Ajax request succeeded with", data)
          DoodleBot.loop(success(data))
        }

        val theHeaders: js.Dictionary[String] = js.Dictionary()
        hdrs.foreach { hdr => theHeaders += hdr }

        val settings: jquery.JQueryAjaxSettings =
          js.Dynamic.literal (
            headers = theHeaders,
            method = "POST",
            url = path,
            data = payload,
            dataType = "json",
            success = (data: js.Any, textStatus: String, jqXHR: jquery.JQueryXHR) =>
              callbackSuccess(data.asInstanceOf[js.Dictionary[js.Any]]) ,
            error = (jqXHR: jquery.JQueryXHR, textStatus: String, errorThrow: String) =>
              callbackFailure(JSON.parse(jqXHR.responseText).asInstanceOf[js.Dictionary[js.Any]])
          ).asInstanceOf[jquery.JQueryAjaxSettings]

        jquery.jQuery.ajax(settings)

      case Tick(message) =>
        dom.window.setInterval(() => DoodleBot.loop(message), 1000)
    }

}
