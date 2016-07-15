package doodlebot
package form

import org.scalajs.dom

object Input {
  def email(name: String, placeholder: String = "", value: String = "", help: String = ""): dom.Element =
    apply(name, "email", placeholder, value, help)

  def text(name: String, placeholder: String = "", value: String = "", help: String = ""): dom.Element =
    apply(name, "text", placeholder, value, help)

  def password(name: String, placeholder: String = "", value: String = "", help: String = ""): dom.Element =
    apply(name, "password", placeholder, value, help)

  def apply(name: String, `type`: String, placeholder: String = "", value: String = "", help: String = ""): dom.Element = {
    import scalatags.JsDom.short._

    div(*.`class`:="form-group")(
      input(*.`type`:=`type`, *.name:=name, *.placeholder:=placeholder, *.value:=value),
      if(help.isEmpty)
        span()
      else
        span(*.`class`:="help-block")(help)
    ).render
  }

}
