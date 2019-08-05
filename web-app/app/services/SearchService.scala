package services

import javax.inject._
import dao.SearchDao
import models.Search

class SearchService @Inject()(searchDao: SearchDao) {
  def searchMovies(keyword: String, limit: Int): List[Search] = {
    searchDao.searchMovies(keyword, limit)
  }
}
