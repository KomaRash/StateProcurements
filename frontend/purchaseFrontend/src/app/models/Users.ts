export interface User {
  position: Position,
  name: string,
  surname: string,
  fatherName: string,
  email: string,
  password: string,
  militaryRank: string,
  userID?:number

}

export interface Position {
  positionName: string,
  positionRole: string,
  militaryUnit: string,
  positionId?: number
}
