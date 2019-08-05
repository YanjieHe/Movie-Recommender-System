package services

import javax.inject._
import dao.SearchDao

class SearchService @Inject()(searchDao: SearchDao) {
  def searchMovies(keyword: String): List[Search] = {
    searchDao.searchMovies(keyword)
  }
}
