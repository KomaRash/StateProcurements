package OKRBParser.domain.parseExcel

trait RepositoryAlgebra[F[_]] {
  def maxThreadPool(): Int
}
