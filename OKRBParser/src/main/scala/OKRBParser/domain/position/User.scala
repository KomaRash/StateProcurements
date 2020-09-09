package OKRBParser.domain.position

/**
 *
 * @param userID id пользователя
 * @param name имя пользователя
 * @param surname фамилия
 * @param fatherName отчество
 * @param email эмаил
 * @param password пароль от учетной записи
 * @param militaryRank воинское звание
 */
case class User(userID:Option[Int],
                name:String,
                surname:String,
                fatherName:String,
                email:String,
                password:String,
                militaryRank:String)

