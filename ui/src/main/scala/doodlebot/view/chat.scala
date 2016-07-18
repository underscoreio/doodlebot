package doodlebot
package view

import doodlebot.form.Input
import doodlebot.model.Model
import doodlebot.message.Message
import org.scalajs.dom
import scala.scalajs.js

object Chat {
  import doodlebot.virtualDom._
  import doodlebot.virtualDom.Dom._

  object message {
    val name = "message"
    val selector = s"#chat input[name=$name]"
  }

  def onMessage(event: dom.Event): Unit = {
    dom.console.log("onMessage")
    event.preventDefault()

    val m = dom.document.querySelector(message.selector).asInstanceOf[dom.html.Input].value

    val payload = js.Dictionary("message" -> m)
    val success = (data: js.Dictionary[String]) => {
      Message.Authenticated(data("name"), data("session"))
    }
    val failure =
      (errors: Map[String, List[String]]) => Message.ChatError(errors)

    val effect = Effect.Request("/message", payload, success, failure)
    Effect.run(effect)
  }

  def render(name: String, session: String, chat: Model.Chat): VTree = {
    element("div#chat")(
        h2("Chat"),
        div(
          chat.messages.map(msg => p(msg)):_*
        ),
        form("onsubmit":=eventHandler(onMessage _))(
          span(name),
          Input.text(
            name=message.name,
            placeholder="Say something",
            value=chat.message,
            onInput=(m) => Effect.Message(Message.Chat(chat.copy(message=m)))
          )
        )
    )
  }
}
