package OKRBParser.domain.position

import OKRBParser.domain.Err


sealed trait UserError extends Err

case class UserAlreadyExist() extends UserError

case class CorrectUserData() extends UserError

