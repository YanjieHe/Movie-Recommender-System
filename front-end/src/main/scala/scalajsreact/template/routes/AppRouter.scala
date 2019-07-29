package scalajsreact.template.routes

import scalajsreact.template.models.Menu
import scalajsreact.template.pages.HomePage
import scalajsreact.template.pages.MoviePage
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._

import japgolly.scalajs.react.extra.router.{
  Resolution,
  RouterConfigDsl,
  RouterCtl,
  _
}
import japgolly.scalajs.react.vdom.html_<^._

object AppRouter {

  sealed trait AppPage

  case object Home extends AppPage
  case class Movie(imdbId: Int) extends AppPage

  val config = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Home) ~> render(HomePage()))
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  val mainMenu = Vector(
    Menu("Home", Home)
  )

  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]) =
    <.div(
      r.render()
    )

  val baseUrl = BaseUrl.fromWindowOrigin / "index.html"

  val router = Router(baseUrl, config)
}
