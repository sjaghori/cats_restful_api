package at.technikumwien.json

trait GenericJsonWriter[T] {
  def toJsonString(e: T): String
}
