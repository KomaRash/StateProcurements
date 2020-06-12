package OKRBParser.position

case class UserInfo(id: Option[Long] = None,
                    name:String,
                    surname:String,
                    fatherName:String,
                    password:String,
                    email:String,
                    militaryRank:String)
