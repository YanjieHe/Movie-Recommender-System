package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.StringInterpolation
import services.MovieService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class MovieController @Inject()(
    cc: ControllerComponents,
    movieService: MovieService
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
    val movie = movieService.read(imdbId)
    val header = movie.startYear match {
      case Some(year) => movie.primaryTitle + " (" + movie.startYear + ")"
      case None       => movie.primaryTitle
    }
    val posterLink = "/home/yanjie/Downloads/posters/" + movie.imdbId + ".jpg"
    Ok(views.html.movie("movie")(movie)(header)(posterLink)(html""))
  }
}
