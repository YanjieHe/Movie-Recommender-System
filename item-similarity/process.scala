// import scala.io.Source

// val movies = Source.fromFile("genres.csv").getLines.drop(1).map(line=>line.split(",").head.toInt).toArray

// case class Similarity(movie1: Int, movie2: Int, similarity: Double)

// def parseSimilarity(line: String): Similarity = {
//     val items = line.split(",")
//     Similarity(items(0).toInt, items(1).toInt, items(2).toDouble)
// }
// val data = spark.read.textFile("similarity.csv").filter(_.head != 'M').map(parseSimilarity _).toDF()

// sc.parallelize(movies).map {
// movieId =>
//     data.filter(row => row(0) == movieId || row(1) == movieId)
//         .orderBy("similarity").take(20)
// }

// sc.parallelize(movies).map {
// movieId =>
//     data.filter(row => row(0) == movieId || row(1) == movieId).take(10)
// }
