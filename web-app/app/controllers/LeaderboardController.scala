package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.StringInterpolation
import services.MovieService
import services.PrincipalsService
import scala.collection.mutable.ArrayBuffer
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport

import models.Movie

case class LeaderboardFilters(
    genres: String,
    orderBy: String
)

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class LeaderboardController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with I18nSupport {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  val leaderboardForm = Form(
    mapping(
      "genres" -> text,
      "orderBy" -> text
    )(LeaderboardFilters.apply)(LeaderboardFilters.unapply)
  )

  val userPost = Action(parse.form(leaderboardForm)) { implicit request =>
    val userData = request.body
    println((userData.genres, userData.orderBy))
    Redirect(routes.LeaderboardController.leaderboard())
  }

  def leaderboard() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.leaderboard("Leaderboard")(leaderboardForm))
  }
}
