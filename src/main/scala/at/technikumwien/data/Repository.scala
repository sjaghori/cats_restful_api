package at.technikumwien.data

import at.technikumwien.Cat

trait Repository[F[_]] {
  def delete(id: Int): F[Int]

  def update(id: Int, row: Cat): F[Int]

  def createSchema(): F[Unit]

  def insert(row: Cat): F[Int]

  def selectAll(page: Int, pageSize: Int, sort: String): F[Seq[Cat]]

  def select(id: Int): F[Option[Cat]]

  def sortingFields: Set[String]
}
