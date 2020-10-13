package OKRBParser.domain.position


/**
 *
 * @param positionName
 * @param positionRole
 * @param militaryUnit
 * @param positionId
 */
case class Position(
                     positionName:String, // должность
                     positionRole:Role,
                     militaryUnit:String, // воинская часть
                     positionId:Option[PositionId]=None,
                   )


