package OKRBParser.infrastructure.repository.Postgres

import doobie.util.fragment.Fragment
import doobie.implicits._
import doobie.util.Read
import doobie.util.query.Query0
import doobie._

object Pagination {
  def offset[A:Read](offset:Int,pageSize:Int)(q: Query0[A]): Query0[A] =
    (q.toFragment ++ fr"LIMIT $pageSize OFFSET $offset").query

}
