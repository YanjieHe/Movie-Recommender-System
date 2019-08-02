package models

case class Movie(
    imdbId: Int,
    primaryTitle: String,
    originalTitle: String,
    rating: Float,
    numVotes: Int,
    startYear: Option[Int],
    runtimeMinutes: Int,
    posterLink: Option[String],
    overview: Option[String]
)