package doodlebot
package view

import doodlebot.form.Input
import doodlebot.message.{Message => GlobalMessage}
import org.scalajs.dom
import scala.scalajs.js

object Chat {
  import doodlebot.virtualDom._
  import doodlebot.virtualDom.Dom._

  final case class Model(name: String, password: String, log: Log, message: String)
  final case class Log(offset: Int, messages: List[Message])
  final case class Message(author: String, message: String)
  object Message {
    def deserialize(data: js.Dictionary[js.Any]): Message = {
      val author = data("author").asInstanceOf[String]
      val msg = data("message").asInstanceOf[String]
      Message(author, msg)
    }
  }

  sealed abstract class Msg extends Product with Serializable
  final case class LogMsg(log: Log) extends Msg
  object LogMsg {
    def deserialize(data: js.Dictionary[js.Any]): LogMsg = {
      val offset = data("offset").asInstanceOf[Int]
      val messages = data("messages").asInstanceOf[js.Array[js.Any]].toList.map { msg =>
        Message.deserialize(msg.asInstanceOf[js.Dictionary[js.Any]])
      }
      LogMsg(Log(offset, messages))
    }
  }
  final case class MessageMsg(message: String) extends Msg
  final case class Errors(errors: Map[String, List[String]]) extends Msg
  final case object Poll extends Msg


  def init(name: String, password: String): (Model, Effect) = {
    (Model(name, password, Log(0, List.empty), ""), Effect.tick(GlobalMessage.Chat(Poll)))
  }


  def update(model: Model, msg: Msg): (Model, Effect) =
    msg match {
      case LogMsg(l) =>
       val range = l.offset - model.log.offset
        val m =
          if(range > 0) {
            val newLog =
              model.log.copy(
                offset = l.offset, messages = l.messages.take(range) ++ model.log.messages
              )
            model.copy(log = newLog)
          }
          else
            model

        (m, Effect.noEffect)

      case MessageMsg(m) =>
        (model.copy(message = m), Effect.noEffect)

      case Poll =>
        (
          model,
          Effect.request(
            "/poll",
            js.Dictionary("offset" -> model.log.offset.toString),
            (data) => GlobalMessage.Chat(LogMsg.deserialize(data)),
            (errors) => GlobalMessage.Chat(Errors.apply(errors)),
            List(BasicAuth.header(model.name, model.password))
          )
        )

      case Errors(errors) =>
        dom.console.log(errors)
        (model, Effect.noEffect)
    }


  def render(model: Model): VTree = {
    def onSubmit(event: dom.Event): Unit = {
      event.preventDefault()

      val payload = js.Dictionary("name" -> model.name, "message" -> model.message)
      val success = (data: js.Dictionary[js.Any]) => {
        // Clear the message input now that we've sent it to the server
        GlobalMessage.Chat(MessageMsg(""))
      }
      val failure =
        (errors: Map[String, List[String]]) => GlobalMessage.Chat(Errors(errors))

      val effect =
        Effect.request(
          "/message",
          payload,
          success,
          failure,
          List(BasicAuth.header(model.name, model.password))
        )

      Effect.run(effect)
    }

    element("div#chat")(
        h2("Chat"),
        div(
          model.log.messages.reverse.map { case Message(author, message) =>
            span(span(s"$author: "), span(message), br)
          }:_*
        ),
        form("onsubmit":=eventHandler(onSubmit _))(
          Input.text(
            name=messageInput.name,
            placeholder="Say something",
            value=model.message,
            onInput=(m) => Effect.message(GlobalMessage.Chat(MessageMsg(m)))
          )
        )
    )
  }

  object messageInput {
    val name = "message"
    val selector = s"#chat input[name=$name]"
  }
}
