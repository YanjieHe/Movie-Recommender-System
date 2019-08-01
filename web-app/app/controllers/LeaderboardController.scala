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
import services.MovieService

case class LeaderboardFilters(
    year: String,
    orderBy: String,
    genre: String,
    page: Int
)

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class LeaderboardController @Inject()(
    cc: ControllerComponents,
    movieService: MovieService
) extends AbstractController(cc)
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
      "year" -> text,
      "orderBy" -> text,
      "genre" -> text,
      "page" -> number
    )(LeaderboardFilters.apply)(LeaderboardFilters.unapply)
  )

  val defaultData = Map(
    "year" -> "Any Year",
    "orderBy" -> "Rating Descending",
    "genre" -> "Any Genre",
    "page" -> "1"
  )
  var userData = leaderboardForm.bind(defaultData).get
  val userPost = Action(parse.form(leaderboardForm)) { implicit request =>
    userData = request.body
    Redirect(routes.LeaderboardController.leaderboard())
  }

  def leaderboard() = Action { implicit request: Request[AnyContent] =>
    val movies =
      movieService
        .filterMovies(
          userData.year.toLowerCase(),
          userData.orderBy.toLowerCase(),
          userData.genre.toLowerCase(),
          userData.page.toInt
        )
    val currentData = Map(
      "year" -> userData.year,
      "orderBy" -> userData.orderBy,
      "genre" -> userData.genre,
      "page" -> userData.page.toString()
    )
    def header(movie: Movie): String = movie.startYear match {
      case Some(year) => movie.primaryTitle + " (" + year + ")"
      case None       => movie.primaryTitle
    }
    def compressParameters(page: Int): String = {
      def process(parameter: String) = {
        parameter.replace(" ", "_")
      }
      "year=" + process(userData.year) + "&order_by=" + process(
        userData.orderBy
      ) + "&genre=" + process(userData.genre) + "&page=" + page
    }
    Ok(
      views.html.leaderboard("Leaderboard")(leaderboardForm)(movies)(header)(
        currentData
      )(userData.page.toInt)(compressParameters)
    )
  }

  def query(parameters: String) = Action {
    implicit request: Request[AnyContent] =>
      val items = parameters.split("&").map(_.split("="))
      if (items.size == 4 && items.forall(pair => pair.size == 2)) {
        if (items(0)(0) == "year"
            && items(1)(0) == "order_by"
            && items(2)(0) == "genre"
            && items(3)(0) == "page") {
          def process(parameter: String) = {
            parameter.replace("_", " ")
          }
          userData = leaderboardForm
            .bind(
              Map(
                "year" -> process(items(0)(1)),
                "orderBy" -> process(items(1)(1)),
                "genre" -> process(items(2)(1)),
                "page" -> items(3)(1).toString()
              )
            )
            .get
          Redirect(
            routes.LeaderboardController
              .leaderboard()
          )
        } else {
          throw new Exception("wrong parameters format")
        }
      } else {
        throw new Exception("wrong parameters for discover query")
      }
  }

}
