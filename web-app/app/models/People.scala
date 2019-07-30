package models

case class People(
    nameId: Int,
    primaryName: String,
    birthYear: Option[Int],
    deathYear: Option[Int]
)