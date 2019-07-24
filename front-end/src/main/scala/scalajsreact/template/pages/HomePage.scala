package scalajsreact.template.pages

import scalacss.Defaults._
import scalacss.ScalaCssReact._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePage {

  object Style extends StyleSheet.Inline {
    import dsl._
    val content = style(
      textAlign.center,
      fontSize(30.px),
      minHeight(450.px),
      paddingTop(40.px)
    )
  }

  def navigation() =
    <.nav(
      ^.cls := "navbar navbar-expand-lg navbar-dark bg-dark",
      <.div(
        ^.cls := "collapse navbar-collapse",
        ^.id := "navbarNav",
        <.ul(
          ^.cls := "nav-item active",
          <.a(^.cls := "nav-link", ^.href := "#", "Home")
        )
      )
    )

  val component =
    ScalaComponent.builder
      .static("HomePage")(<.div(navigation()))
      .build

  def apply() = component()
}
