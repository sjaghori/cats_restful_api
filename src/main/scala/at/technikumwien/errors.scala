package at.technikumwien

sealed trait UserError extends Exception
case class InvalidCat(cat: Cat, msg: String) extends UserError
case class UnknownSortField(field: String) extends UserError
