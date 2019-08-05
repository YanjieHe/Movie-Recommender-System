package models
import play.api.libs.json._
case class Search(
    link: String,
    text: String
)

object Search {
  implicit val format: Format[Search] = Json.format

  implicit val implicitWrites = new Writes[Search] {
    def writes(post: Search): JsValue = {
      Json.obj(
        "link" -> post.link,
        "text" -> post.text
      )
    }
  }
}
