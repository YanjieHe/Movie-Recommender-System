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
        ^.cls := "container",
        <.a(^.cls := "navbar-brand", ^.href := "#", "Navbar"),
        <.button(
          ^.cls := "navbar-toggler",
          ^.`type` := "button",
          VdomAttr("data-toggle") := "collapse",
          VdomAttr("data-target") := "#navbarNavAltMarkup",
          VdomAttr("aria-controls") := "navbarNavAltMarkup",
          VdomAttr("aria-expanded") := "false",
          VdomAttr("aria-label") := "Toggle navigation",
          <.span(^.cls := "navbar-toggler-icon")
        ),
        <.div(
          ^.cls := "collapse navbar-collapse",
          ^.id := "navbarNavAltMarkup",
          <.div(
            ^.cls := "navbar-nav",
            <.a(^.cls := "nav-item nav-link active", ^.href := "#", "Home"),
            <.a(^.cls := "nav-item nav-link", ^.href := "#", "Discover"),
            <.a(^.cls := "nav-item nav-link", ^.href := "#", "Movies"),
            <.a(^.cls := "nav-item nav-link", ^.href := "#", "People")
          )
        )
      )
    )

  def avgRating(score: Float) = {
    <.span("rating: " + score)
  }

  def overview(text: String) = {
    <.div(<.br(), <.h5("Overview"), <.span(text), <.br(), <.br())
  }

  case class CrewMember(name: String, job: String)

  def featuredCrew(members: List[CrewMember]) = {

    def args(memberList: List[CrewMember]) =
      memberList.map { member =>
        <.div(^.cls := "col-sm-4", <.strong(member.name), <.br(), member.job)
      }.toList

    val res = members
      .grouped(3)
      .map { memberList =>
        println(memberList)
        <.div(<.div(((^.cls := "row") :: args(memberList)): _*), <.br())
      }
      .toList
    <.div(
      <.h5("Featured Crew"),
      <.div(^.cls := "container", <.div(res: _*))
    )
  }

  def header(title: String, year: Option[Int]) =
    year match {
      case Some(y) => <.h5(title + " (" + y + ")")
      case None =>
        <.h5(title)
    }

  def movieInfo() =
    <.div(
      ^.cls := "container",
      <.div(
        ^.cls := "row",
        <.div(
          ^.cls := "col-3",
          <.img(
            ^.src := "https://image.tmdb.org/t/p/w600_and_h900_bestv2/dzBtMocZuJbjLOXvrl4zGYigDzh.jpg",
            ^.width := "100%"
          )
        ),
        <.div(
          ^.cls := "col-9",
          header("The Lion King", Some(2019)),
          avgRating(3.5f),
          overview(
            "Simba idolises his father, King Mufasa, and takes to heart his own royal destiny. But not everyone in the kingdom celebrates the new cub's arrival. Scar, Mufasa's brother—and former heir to the throne—has plans of his own. The battle for Pride Rock is ravaged with betrayal, tragedy and drama, ultimately resulting in Simba's exile. With help from a curious pair of newfound friends, Simba will have to figure out how to grow up and take back what is rightfully his."
          ),
          featuredCrew(
            List(
              new CrewMember("Jonathan Roberts", "Characters"),
              new CrewMember("Linda Woolverton", "Characters"),
              new CrewMember("Irene Mecchi", "Characters"),
              new CrewMember("Jon Favreau", "Director"),
              new CrewMember("Jeff Nathanson", "Screenplay"),
              new CrewMember("Brenda Chapman", "Story")
            )
          )
        )
      )
    )

  val component =
    ScalaComponent.builder
      .static("HomePage")(
        <.div(navigation(), <.div(^.cls := "container", movieInfo()))
      )
      .build

  def apply() = component()
}
