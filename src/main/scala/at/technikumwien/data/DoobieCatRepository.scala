package at.technikumwien.data

import at.technikumwien.Cat
import at.technikumwien.data.DoobieCatRepository.{createStm, dropStm, insertFrag, updateFrag}
import cats.effect.Sync
import doobie.implicits._
import doobie.{Fragment, LogHandler, Transactor}

import scala.collection.mutable

object DoobieCatRepository {
  val (columns, columnsWithComma) = {
    val columns = mutable.LinkedHashSet[String]("id", "name", "status", "image")
    (columns.toSet, columns.mkString(","))
  }

  val createStm =
    sql"""
    CREATE TABLE cats (
      id SERIAL,
      name VARCHAR NOT NULL,
      status INT NOT NULL,
      image VARCHAR)
  """
  val dropStm = sql"DROP TABLE IF EXISTS cats"
  val insertFrag: Fragment = fr"INSERT INTO cats (" ++ Fragment.const(columnsWithComma) ++ fr")"
  val updateFrag: Fragment = fr"UPDATE cats SET (" ++ Fragment.const(columnsWithComma) ++ fr") = "
}

class DoobieCatRepository[F[_] : Sync](xa: Transactor[F]) extends Repository[F] {

  implicit val han: LogHandler = LogHandler.jdkLogHandler

  override def delete(id: Int): F[Int] = sql"DELETE FROM cats WHERE id = $id".update.run.transact(xa)

  override def update(id: Int, row: Cat): F[Int] = {
    val valuesFrag =
      fr"(${row.id}, ${row.name}, ${row.status}, ${row.image})"

    (updateFrag ++ valuesFrag).update.run.transact(xa)
  }

  override def createSchema(): F[Unit] = createStm.update.run.map(_ => ()).transact(xa)

  def dropSchema(): F[Unit] = dropStm.update.run.map(_ => ()).transact(xa)

  override def insert(row: Cat): F[Int] = {
    val values =
      fr"VALUES (${row.id}, ${row.name}, ${row.status}, ${row.image})"

    (insertFrag ++ values).update.run.transact(xa)
  }

  override def selectAll(page: Int, pageSize: Int, sort: String): F[Seq[Cat]] =
    sql"SELECT * FROM cats"
      .query[Cat]
      .stream
      //.drop(page * pageSize)
      //.take(pageSize)
      .compile
      .to[Seq]
      .transact(xa)

  override def select(id: Int): F[Option[Cat]] =
    sql"SELECT * FROM cats WHERE id = $id"
      .query[Cat]
      .to[List]
      .map(_.headOption)
      .transact(xa)

  def schemaExists(): F[Unit] =
    sql"""
        SELECT 1
        FROM   information_schema.tables
        WHERE  table_catalog = 'cats'
        AND    table_name = 'cats';"""
      .query[Unit]
      .unique
      .transact(xa)

  override def sortingFields: Set[String] = DoobieCatRepository.columns
}