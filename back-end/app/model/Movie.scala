package model
import play.api.libs.json._

case class Movie(
    imdbId: Int,
    primaryTitle: String,
    originalTitle: String,
    avgRating: Float,
    numVotes: Int,
    startYear: Option[Int],
    runtimeMinutes: Int,
    posterLink: Option[String]
)

object Movie {
    implicit val format: Format[Movie] = Json.format

    // implicit val implicitWrites = new Writes[Movie] {
    //     def writes(movie: Movie): JsValue = {
    //         Json.obj(
    //             "imdbId" -> movie.imdbId,
    //             "primaryTitle" -> movie.primaryTitle,
    //             "originalTitle" -> movie.originalTitle,
    //             "avgRating" -> movie.avgRating,
    //             "numVotes" -> movie.numVotes,
    //             "startYear"->movie.startYear
    //         )
    //     }
    // }
}
