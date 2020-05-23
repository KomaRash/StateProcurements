package OKRBParser

object Main{
  def main(args: Array[String]): Unit={

    import org.apache.spark.sql.SparkSession

    System.setProperty("hadoop.home.dir", "C:\\winutils .exe")
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .master("local[*]")
      .getOrCreate()

    import com.crealytics.spark.excel._
    import spark.implicits._
    val primitiveDS = List(1, 2, 3).toDS()
    primitiveDS.show()
    val df = spark.read
      .excel()
      .load("C:\\Users\\KOma_rash\\Desktop\\Гос закупки\\OKRB.xlsx")
    df.printSchema()
    spark.w
  }

  // For implicit conversions like converting RDDs to DataFrames
}
