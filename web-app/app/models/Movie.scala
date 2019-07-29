package models

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