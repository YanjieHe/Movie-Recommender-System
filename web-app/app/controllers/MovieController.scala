package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.StringInterpolation
import services.MovieService
import services.PrincipalsService
import models.Movie

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

  def movie(imdbId: Int) = Action { implicit request: Request[AnyContent] =>
    val movie = movieService.read(imdbId)
    val header = movie.startYear match {
      case Some(year) => movie.primaryTitle + " (" + year + ")"
      case None       => movie.primaryTitle
    }
    val posterLink = "/images/posters/" + movie.imdbId + ".jpg"
    val principalsList =
      principalsService.getPrincipalsList(movie.imdbId, 6).map {
        case (name, principal) => (name, principal)
      }
    val firstRow = principalsList.take(3)
    val secondRow = principalsList.drop(3)

    val similarMovies = movieService.getSimilarMovies(imdbId, 10)
    val firstRecsRow = similarMovies.take(5)
    val secondRecsRow = similarMovies.drop(5)

    Ok(
      views.html
        .movie("movie")(movie)(header)("%1.1f".format(movie.rating))(
          firstRow,
          secondRow
        )(firstRecsRow, secondRecsRow)(html"")
    )
  }

  def popularMovies() = Action { implicit request: Request[AnyContent] =>
    val movies = movieService
      .filterMovies("2010s", "popularity descending", "any genre", 1)
      def header(movie: Movie): String = movie.startYear match {
        case Some(year) => movie.primaryTitle + " (" + year + ")"
        case None       => movie.primaryTitle
      }
    Ok(views.html.popular(movies)(header))
  }
}
