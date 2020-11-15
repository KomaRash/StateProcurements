package OKRBParser.domain.okrb

case class OKRBProduct(section: Int,
                       productClass: Int,
                       subCategories: Int,
                       grouping: Int,
                       name: String,
                       okrbId: Option[Int] = None)
