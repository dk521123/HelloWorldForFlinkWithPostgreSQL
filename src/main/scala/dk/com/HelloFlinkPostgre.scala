package dk.com

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.connector.jdbc.JdbcExecutionOptions
import org.apache.flink.connector.jdbc.JdbcConnectionOptions
import org.apache.flink.connector.jdbc.JdbcStatementBuilder
import org.apache.flink.connector.jdbc.JdbcSink
import java.util.Properties
import java.sql.PreparedStatement
import scala.language.reflectiveCalls


object HelloFlinkPostgre {
  val LOG = LoggerFactory.getLogger("HelloWorld")

  def main(args: Array[String]): Unit = {
    LOG.info("Start!")

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // val text = env.fromElements(
    //   "To be, or not to be,--that is the question:--",
    //   "Whether 'tis nobler in the mind to suffer",
    //   "The slings and arrows of outrageous fortune",
    //   "Or to take arms against a sea of troubles,"
    // )
    val text = env.readTextFile("input")

    val counts = text.flatMap { _.toLowerCase.split("\\W+") }
      .map { (_, 1) }
      .keyBy(0)
      .sum(1)

    // https://nightlies.apache.org/flink/flink-docs-master/api/java/org/apache/flink/connector/jdbc/JdbcSink.html
    val jdbcSink = JdbcSink.sink(
      // SQL
      "INSERT INTO demo_counter (word, counter) VALUES (?, ?)",
      new JdbcStatementBuilder[(String, Int)] {
        override def accept(statement: PreparedStatement, row: (String, Int)): Unit = {
          statement.setString(1, row._1);
          statement.setInt(2,  row._2);
        }
      },
      JdbcExecutionOptions.builder()
        .withBatchSize(1000)
        .withBatchIntervalMs(200)
        .withMaxRetries(1)
        .build(),
      new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
        .withUrl("jdbc:postgresql://localhost:5431/demo_db")
        .withDriverName("org.postgresql.Driver")
        .withUsername("postgres")
        .withPassword("password")
        .build()
    )
    //counts print
    counts.addSink(jdbcSink)

    env.execute("temp")

    LOG.info("Done...")
  }
}
