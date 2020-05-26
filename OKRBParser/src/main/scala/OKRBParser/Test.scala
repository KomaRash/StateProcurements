package OKRBParser

/*
import cats.effect.{Blocker, IO}
import doobie.util.transactor.Transactor

object Test extends App {
//"jdbc:mysql://localhost:3306/ StateProcurements?useUnicode=true&serverTimezone=UTC"
  import doobie.util.ExecutionContexts

  // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
  // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // A transactor that gets connections from java.sql.DriverManager and executes blocking operations
  // on an our synchronous EC. See the chapter on connection handling for more info.
  val xa = Transactor.fromDriverManager[IO](
    "com.mysql.jdbc.Driver",     // driver classname
    "jdbc:mysql://localhost:3306/ StateProcurements?useUnicode=true&serverTimezone=UTC",     // connect URL (driver-specific)
    "RootKoma",                  // user
    "root",                          // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )

}
*/
import OKRBParser.Database.MySqlRepository
import cats.effect._
import doobie._
import doobie.hikari._

object HikariApp extends IOApp {

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[IO, HikariTransactor[IO]] =
  for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    be <- Blocker[IO]    // our blocking EC
    xa <- HikariTransactor.newHikariTransactor[IO](
      "com.mysql.jdbc.Driver",     // driver classname
      "jdbc:mysql://localhost:3306/ StateProcurements?useUnicode=true&serverTimezone=UTC",     // connect URL (driver-specific)
      "RootKoma",                  // user
      "root",                                     // password
      ce,                                     // await connection here
      be                                      // execute JDBC operations here
    )
  } yield xa


  def run(args: List[String]): IO[ExitCode] = {

    val list = List(OKRBProduct(1, 2, 2, 312, "q2eqwewqe"), OKRBProduct(1, 21, 12, 111, "212qqq12"))
    transactor.use { tx =>
      val database = new MySqlRepository(tx)
      for {
        db <- database.saveList(list)
        _ <- IO(println(db))
      } yield ExitCode.Success
    }

  }
}
