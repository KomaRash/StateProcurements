package OKRBParser.domain.position

/**
 * Должность пользователя,который проводит госзакупками
 *
 * @param positionId - id должности
 * @param positionName - название должности
 * @param militaryUnit - воинская часть
 */
case class Position(
                     positionName:String, // должность
                     positionRole:Role,
                     militaryUnit:String, // воинская часть
                     positionId:Option[PositionId]=None,
                    )


