package models

case class Principals(
    imdbId: Int,
    nameId: Int,
    ordering: Int,
    category: String,
    job: Option[String]
)
