package at.technikumwien

final case class Cat(id: Int, name: String, status: Int, image: String)

final case class Cats(cats: Seq[Cat])

final case class CommandResult(count: Int)