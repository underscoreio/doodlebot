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

  object chat {
    val name = "chat"
    val selector = s"#main input[name=$name]"
  }

  def onChat(event: dom.Event): Unit = {
    event.preventDefault()

    val p = dom.document.querySelector(chat.selector).asInstanceOf[dom.html.Input].value

    val payload = js.Dictionary("chat" -> p)
    val success = (data: js.Dictionary[String]) => {
      Message.Authenticated(data("name"), data("session"))
    }
    val failure =
      (errors: Map[String, List[String]]) => Message.ChatError(errors)

    val effect = Effect.Request("/chat", payload, success, failure)
    Effect.run(effect)
  }

  def render(name: String, session: String, chat: Model.Chat): VTree = {
    element("div#chat")(
        h2("Chat"),
        div(
          chat.messages.map(msg => p(msg)):_*
        ),
        form("onsubmit":=eventHandler(onChat _))(
          span(name),
          Input.text(
            name=Chat.chat.name,
            placeholder="Say something",
            value=chat.chat,
            onInput=(c) => Effect.Message(Message.Chat(chat.copy(chat=c)))
          )
        )
    )
  }
}
