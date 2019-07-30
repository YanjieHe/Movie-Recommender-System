package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.StringInterpolation
import services.MovieService
import services.PrincipalsService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class MovieController @Inject()(
    cc: ControllerComponents,
    movieService: MovieService,
    principalsService: PrincipalsService
) extends AbstractController(cc) {

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

  def render(imdbId: Int) = Action { implicit request: Request[AnyContent] =>
    def processCategory(category: String) = {
      if (category == "self") {
        "character"
      } else {
        category
      }
    }
    val movie = movieService.read(imdbId)
    val header = movie.startYear match {
      case Some(year) => movie.primaryTitle + " (" + year + ")"
      case None       => movie.primaryTitle
    }
    val posterLink = "/images/posters/" + movie.imdbId + ".jpg"
    val principalsList =
      principalsService.getPrincipalsList(movie.imdbId, 6).map {
        case (name, principal) => (name, processCategory(principal.category))
      }
    val firstRow = principalsList.take(3)
    val secondRow = principalsList.drop(3)

    Ok(
      views.html
        .movie("movie")(movie)(header)("%1.1f".format(movie.avgRating))(posterLink)(firstRow, secondRow)(html"")
    )
  }
}