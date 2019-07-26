package controller

import javax.inject.Inject
import javax.inject.{Inject, Provider}
import play.api.MarkerContext

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

import play.api.libs.json._

import post.PostControllerComponents
import post.PostBaseController

import dao.MovieResourceHandler

case class MovieFormInput(imdbId: String)

class MovieController @Inject()(cc: PostControllerComponents)(
    implicit ec: ExecutionContext
) extends PostBaseController(cc) {
  private val logger = Logger(getClass)

  private val form: Form[MovieFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "imdbId" -> nonEmptyText
      )(MovieFormInput.apply)(MovieFormInput.unapply)
    )
  }

  def getMovieInfo(imdbId: String): Action[AnyContent] = PostAction.async {
    implicit request =>
      logger.trace(s"getMovieInfo: imdbId = $imdbId")
      val res = new MovieResourceHandler().read(imdbId.toInt)
      Future {
        res match {
          case Some(movie) => Ok(Json.toJson(movie))
          case None        => throw new Exception("imdbId error")
        }
      }
  }
}
