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
import services.PeopleService
import models.Movie
import models.People

@Singleton
class PeopleController @Inject()(
    cc: ControllerComponents,
    peopleService: PeopleService
) extends AbstractController(cc) {

  def people(nameId: Int) = Action { implicit request: Request[AnyContent] =>
    val person = peopleService.read(nameId)
    val movies = peopleService.getKnownForTitles(nameId)
    def header(movie: Movie): String = movie.startYear match {
      case Some(year) => movie.primaryTitle + " (" + year + ")"
      case None       => movie.primaryTitle
    }
    def personHeader(person: People): String = {
      val birthYear = person.birthYear match {
        case Some(year) => year.toString()
        case None       => "?"
      }
      val deathYear = person.deathYear match {
        case Some(year) => year.toString()
        case None       => "?"
      }
      s"${person.primaryName} ($birthYear - $deathYear)"
    }
    Ok(views.html.people(personHeader(person))(movies)(header))
  }
}
