package at.technikumwien

trait CatAlg[F[_]] {
  def selectAll(page: Option[Int], pageSize: Option[Int], sort: Option[String]): F[Cats]
  def select(id: Int): F[Option[Cat]]
  def insert(cat: Cat): F[Int]
  def update(id: Int, cat: Cat): F[Int]
  def delete(id: Int): F[Int]
}
