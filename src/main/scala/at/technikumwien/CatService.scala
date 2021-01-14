package at.technikumwien

import at.technikumwien.CatService.{DefaultPage, DefaultPageSize, DefaultSortField}
import at.technikumwien.data.Repository
import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._

class CatService[F[_]](repo: Repository[F])(implicit M: MonadError[F, Throwable]) extends CatAlg[F] {

  override def selectAll(page: Option[Int], pageSize: Option[Int], sort: Option[String]): F[Cats] = {
    val sortBy = sort
      .map(s => repo.sortingFields.find(_ == s).toRight(UnknownSortField(s)))
      .getOrElse(Right(DefaultSortField))

    M.fromEither(sortBy).flatMap { sort =>
      val pageN = page.getOrElse(DefaultPage)
      val size = pageSize.getOrElse(DefaultPageSize)

      repo
        .selectAll(pageN, size, sort)
        .map(Cats)
    }
  }

  override def select(id: Int): F[Option[Cat]] = repo.select(id)

  override def insert(cat: Cat): F[Int] = validateCat(cat).flatMap(_ => repo.insert(cat))

  override def update(id: Int, cat: Cat): F[Int] = validateCat(cat).flatMap(_ => repo.update(id, cat))

  override def delete(id: Int): F[Int] = repo.delete(id)

  private val validateCat: Cat => F[Unit] = {
        // TODO: Validation
    case _ => M.pure(())
  }
}

object CatService {
  val DefaultPage = 0
  val DefaultPageSize = 10
  val DefaultSortField: String = "id"
}
