package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.SearchService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class SearchController @Inject()(
    cc: ControllerComponents,
    searchService: SearchService
) extends AbstractController(cc) {

  def search(keyword: String) = Action {
    implicit request: Request[AnyContent] =>
      throw new NotImplementedError()
  }
}
