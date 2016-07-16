package doodlebot
package form

import org.scalajs.dom
import doodlebot.virtualDom._
import doodlebot.virtualDom.Dom._

object Input {
  def email(
    name: String,
    placeholder: String = "",
    value: String = "",
    help: String = "",
    onInput: (String) => Effect = (string) => Effect.NoEffect
  ): VTree =
    apply(name, "email", placeholder, value, help, onInput)

  def text(
    name: String,
    placeholder: String = "",
    value: String = "",
    help: String = "",
    onInput: (String) => Effect = (string) => Effect.NoEffect
  ): VTree =
    apply(name, "text", placeholder, value, help, onInput)

  def password(
    name: String,
    placeholder: String = "",
    value: String = "",
    help: String = "",
    onInput: (String) => Effect = (string) => Effect.NoEffect
  ): VTree =
    apply(name, "password", placeholder, value, help, onInput)

  def apply(
    name: String,
    `type`: String,
    placeholder: String = "",
    value: String = "",
    help: String = "",
    onInput: (String) => Effect = (string) => Effect.NoEffect
  ): VTree = {
    val handler = eventHandler((event: dom.Event) =>
        Effect.run(onInput(event.target.asInstanceOf[dom.raw.HTMLInputElement].value)))

    element("div.form-group")(
      input(
        "type":=`type`,
        "name":=name,
        "placeholder":=placeholder,
        "value":=value,
        "oninput":=handler
      )(),
      if(help.isEmpty)
        span()
      else
        element("span.help-block")(help)
    )
  }

}
