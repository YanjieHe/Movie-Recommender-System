package services

import javax.inject._
import dao.PeopleDao
import dao.MovieDao
import models.Movie
import models.People

class PeopleService @Inject()(peopleDao: PeopleDao, movieDao: MovieDao) {
  def read(nameId: Int): People = {
    peopleDao.read(nameId) match {
      case Some(person) => person
      case None        => throw new Exception("cannot read the person's information")
    }
  }
  def getKnownForTitles(nameId: Int): List[Movie] = {
    val movieIdList = peopleDao.knownForTitles(nameId)
    return movieIdList.map(movieDao.read).map {
      case Some(movie) => movie
      case None        => throw new Exception("movie not found")
    }
  }
}
