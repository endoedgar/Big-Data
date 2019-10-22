import org.apache.spark._
import org.apache.spark.rdd.RDD
import scala.util.control.Exception.allCatch
case class Passenger(name: String, survived: Boolean, sex: Char, age: Double, pClass: Int)

object SparkCore extends App{
  override def  main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark and SparkSql").setMaster("local")
    val sc = new SparkContext(conf)
    def isNumber(s: String): Boolean = (allCatch opt s.toDouble).isDefined
    def getDataFromCSV(path : String): RDD[Array[String]] = {
      val csv = sc.textFile(path)
      val headerAndRows = csv.map(line => line.split(",").map(_.trim))
      val header = headerAndRows.first();
      headerAndRows.filter(_(0) != header(0)).cache();
    }
    def calculateAverageAgeAndVariances(populationGroupedData : RDD[(Int, Iterable[Passenger])]): RDD[(Int, Double, Double)] = {
      val populationAverageAge = populationGroupedData.mapValues(
        v => v.map(i => i.age).sum/v.size
      ).collectAsMap()

      populationGroupedData.map(
        v => (
          v._1,
          populationAverageAge.get(v._1).get,
          v._2.map(i => Math.pow(i.age-populationAverageAge.get(i.pClass).get,2)).sum/v._2.size
        )
      ).cache()
    }

    sc.setLogLevel("WARN")
    val actualData = getDataFromCSV("/home/edgar/Downloads/TitanicSurvival.csv")
    val populationData = actualData.map(l => Passenger((l(0)+ " " +l(1)).replace("\"",""), l(2) == "\"yes\"", l(3).charAt(1), if(isNumber(l(4))) l(4).toDouble else 0 , l(5).substring(1,2).toInt))
    val populationGroupedData = populationData.groupBy(k => k.pClass).cache();
    val populationVariances =  calculateAverageAgeAndVariances(populationGroupedData)
    populationVariances.foreach(println)

    val sampleData = populationData.sample(false, 0.25).cache();

    var i = 0;
    var summedVariances = populationVariances.map(x => (x._1, (0.0d, 0.0d))).collectAsMap()
    val times = 1000
    for(i <- 0 to times) {
      val resampledData = sampleData.sample(true, 1).cache()
      val resampledGroupedData = resampledData.groupBy(k => k.pClass).cache();
      val resampledVariances = calculateAverageAgeAndVariances(resampledGroupedData)
      summedVariances = summedVariances.map(v => (v._1, ( v._2._1+resampledVariances.filter(i => i._1 == v._1).first()._2,v._2._2+resampledVariances.filter(i => i._1 == v._1).first()._3)))
    }
    summedVariances.mapValues(v => (v._1/times, v._2/times)).foreach(println)
  }
}
