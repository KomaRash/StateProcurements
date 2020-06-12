package OKRBParser.position

case class Position(id:Option[Long]=None,
                    positionName:String,
                    militaryUnit:String,
                    userInfo: Option[UserInfo])

