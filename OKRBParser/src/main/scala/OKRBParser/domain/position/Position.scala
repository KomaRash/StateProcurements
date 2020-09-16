package OKRBParser.domain.position

/**
 * Должность пользователя,который проводит госзакупками
 * @param positionId - id должности
 * @param positionName - название должности
 * @param militaryUnit - вонинская часть
 * @param user - информация о пользователе
 */
case class Position(
                    positionName:String, // должность
                    militaryUnit:String, // воинская часть
                    positionId:Option[Int]=None,
                    user:Option[User]=None)


