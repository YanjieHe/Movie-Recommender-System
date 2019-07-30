import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.DataFrame
import scala.collection.mutable.WrappedArray
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.Row
import java.io._

case class Rating(userId: Int, movieId: Int, rating: Float)

def parseRating(str: String): Rating = {
  val fields = str.split(",")
  assert(fields.size == 3)
  Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat)
}

val ratings = spark.read.textFile("imdbId_with_ratings.csv").map(parseRating _).toDF()

val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

// Build the recommendation model using ALS on the training data
val als = new ALS() .setMaxIter(5) .setRegParam(0.01) .setUserCol("userId") .setItemCol("movieId") .setRatingCol("rating")

val model = als.fit(training)

// Evaluate the model by computing the RMSE on the test data
// Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
model.setColdStartStrategy("drop")
val predictions = model.transform(test)

val evaluator = new RegressionEvaluator() .setMetricName("rmse") .setLabelCol("rating") .setPredictionCol("prediction")
val rmse = evaluator.evaluate(predictions)
println(s"Root-mean-square error = $rmse")

// Generate top 10 movie recommendations for each user
val userRecs = model.recommendForAllUsers(10)
// Generate top 10 user recommendations for each movie
val movieRecs = model.recommendForAllItems(10)

def recommendationsToCsv(filePath: String, header: String, recommendations: DataFrame): Unit = {
    val pw = new PrintWriter(new File(filePath))
    pw.write(header)
 def processRow(row: Row): Unit = {
    val id = row(0).asInstanceOf[Int]
    val array = row(1).asInstanceOf[WrappedArray[GenericRowWithSchema]]
    array.foreach {
        item =>
        val anotherId = item(0).asInstanceOf[Int]
        val score = item(1).asInstanceOf[Float]
        pw.write(id.toString())
        pw.write(',')
        pw.write(anotherId.toString())
        pw.write(',')
        pw.write(score.toString())
        pw.write('\n')
    }
 }
 recommendations.rdd.collect.foreach(processRow _)
 pw.close
}

recommendationsToCsv("movieRecs.csv", "Movie_1,Movie_2,Score\n", movieRecs)
