package services

import javax.inject._
import models.Movie
import dao.MovieDao

class MovieService @Inject()(movieDao: MovieDao) {
  def read(imdbId: Int): Movie = {
    movieDao.read(imdbId) match {
      case Some(movie) => movie
      case None        => throw new Exception("cannot read item")
    }
  }
}
