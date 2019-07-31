import scala.io.Source
import scala.collection.mutable
import java.io._

// case class Rating(userId: Int, movieId: Int, rating: Float)

// object Main extends App {

//   def parseRating(str: String): Rating = {
//     val fields = str.split(",")
//     assert(fields.size == 3)
//     Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat)
//   }

//   def similarity(
//       table: mutable.HashMap[Int, mutable.HashMap[Int, Float]],
//       m1: Int,
//       m2: Int
//   ): Double = {
//     val r1 = table(m1)
//     val r2 = table(m2)
//     val users = r1.keySet.intersect(r2.keySet)
//     val s = users.map { user =>
//       val t: Double = r1(user) * r2(user)
//       t * t
//     }.sum
//     val magnitude1 = users.map { user =>
//       val t: Double = r1(user)
//       t * t
//     }.sum
//     val magnitude2 = users.map { user =>
//       val t: Double = r2(user)
//       t * t
//     }.sum
//     s / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2))
//   }
//   override def main(args: Array[String]): Unit = {
//     println("start program")

//     val ratings = Source
//       .fromFile("imdbId_with_ratings.csv")
//       .getLines
//       .map { line =>
//         parseRating(line)
//       }
//       .toList

//     val table = new mutable.HashMap[Int, mutable.HashMap[Int, Float]]()
//     val movies = ratings.map(_.movieId).distinct

//     println("len(movies) = " + movies.length)
//     for (movie <- movies) {
//       table(movie) = new mutable.HashMap[Int, Float]()
//     }

//     for (rating <- ratings) {
//       table(rating.movieId)(rating.userId) = rating.rating
//     }

//     val pw = new PrintWriter("similarity.csv")
//     pw.write("Movie_1,Movie_2,Similarity\n")

//     val total: Double = (0 until movies.length).sum.toDouble
//     println("total = " + total)
//     var count = 0
//     for (i <- 0 until movies.length) {
//       for (j <- (i + 1) until movies.length) {
//         val m1 = movies(i)
//         val m2 = movies(j)
//         val sim = similarity(table, m1, m2)
//         pw.write(m1 + "," + m2 + "," + sim + "\n")
//         count = count + 1
//         if (count % 1000 == 0) {
//           print("\r" + (count / total) + "            ")
//         }
//       }
//     }
//     pw.close()
//     println("finished!")
//   }

// }

case class Movie(imdbId: Int, genres: Array[Int])

object Main extends App {
  def parseMovie(line: String): Movie = {
    val items = line.split(",")
    val imdbId = items(0).toInt
    val genres = items.drop(1).map(_.toInt).toArray
    Movie(imdbId, genres)
  }

  var records: mutable.HashMap[Int, Double] = null

  def similarity(m1: Movie, m2: Movie): Double = {
    def compute(m: Movie): Double = {
      if (records.contains(m.imdbId)) {
        records(m.imdbId)
      } else {
        val res = Math.sqrt(m.genres.map(i => i * i).sum)
        records(m.imdbId) = res
        res
      }
    }
    var s: Double = 0
    for (i <- (0 until m1.genres.length)) {
      s += m1.genres(i) * m2.genres(i)
    }
    val magnitude1: Double = compute(m1)
    val magnitude2: Double = compute(m2)
    s / (magnitude1 * magnitude2)
  }

  override def main(args: Array[String]): Unit = {
    records = new mutable.HashMap[Int, Double]()
    val movies =
      Source.fromFile("genres.csv").getLines().drop(1).map(parseMovie _).toArray

    val pw = new PrintWriter("similarity-20.csv")
    pw.write("Movie_1,Movie_2,Similarity\n")
    val n = movies.length
    val total = (0 until n).sum
    var count = 0
    val similarityArray = new Array[(Int, Double)](n)
    for (i <- 0 until n) {
      println(i)
      for (j <- 0 until n) {
        val m1 = movies(i)
        val m2 = movies(j)
        val sim = similarity(m1, m2)
        similarityArray(j) = (m2.imdbId, sim)
      }
      val res = similarityArray.sortBy { case (movieId, sim) => sim }.take(20)
      for ((movieId, sim) <- res) {
        pw.write(movies(i).imdbId.toString() + "," + movieId + "," + sim + "\n")
      }
    }
    pw.close()
    println()
    println("finished!")
  }
}
