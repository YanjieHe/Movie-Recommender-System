package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import services.SearchService
import models.Search

@Singleton
class SearchController @Inject()(
    cc: ControllerComponents,
    searchService: SearchService
) extends AbstractController(cc) {

  def searchMovies() = Action(parse.json) { request =>
    (request.body \ "keyword")
      .asOpt[String]
      .map { keyword =>
        val result: List[Search] = searchService.searchMovies(keyword, 10)
        println(result)
        Ok(
          Json.arr(
            result
              .map(item => Json.toJson(item))
          )(0)
        )
      }
      .getOrElse {
        BadRequest("Missing parameter [name]")
      }
  }
}
