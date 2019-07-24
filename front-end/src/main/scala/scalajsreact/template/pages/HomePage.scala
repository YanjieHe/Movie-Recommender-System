package scalajsreact.template.pages

import scalacss.Defaults._
import scalacss.ScalaCssReact._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePage {

  object Style extends StyleSheet.Inline {
    import dsl._
    val content = style(textAlign.center,
                        fontSize(30.px),
                        minHeight(450.px),
                        paddingTop(40.px))
  }

  def navigation() = 
  <.nav(^.cls := "navbar navbar-expand-lg navbar-light bg-light")

  val component =
    ScalaComponent.builder
      .static("HomePage")(<.div(Style.content, "Scala js react template"))
      .build

  def apply() = component()
}
