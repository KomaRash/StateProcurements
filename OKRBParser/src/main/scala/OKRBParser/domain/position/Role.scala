package OKRBParser.domain.position

import cats.Eq
import cats.implicits._
import tsec.authorization.{AuthGroup, SimpleAuthEnum}

sealed case class Role(roleRepr: String)

object Role extends SimpleAuthEnum[Role, String] {

  //val Administrator: Role = Role("Administrator")
  val Unauthorized: Role = Role("Unauthorized")
  val User: Role = Role("User")
  val Director: Role = Role("Director")

  implicit val E: Eq[Role] = Eq.fromUniversalEquals[Role]
  protected val values: AuthGroup[Role] = AuthGroup(User, Director)

  def getRepr(t: Role): String = t.roleRepr

}




