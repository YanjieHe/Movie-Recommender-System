import scala.io.Source

case class Rating(userId: Int, movieId: Int, rating: Float)

def parseRating(str: String): Rating = {
  val fields = str.split(",")
  assert(fields.size == 3)
  Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat)
}

val ratings = Source.fromFile("imdbId_with_ratings.csv").getLines.map(parseRating _).toList

val movies = ratings.map(_.movieId).
