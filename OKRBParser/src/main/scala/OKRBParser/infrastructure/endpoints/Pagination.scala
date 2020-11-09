package OKRBParser.infrastructure.endpoints

import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object Pagination {
  /* Necessary for decoding query parameters */
  import QueryParamDecoder._

  /* Parses out the optional offset and page size params */
  object OptionalPageSizeMatcher extends OptionalQueryParamDecoderMatcher[Int]("pageSize")
  object OptionalPageMatcher extends OptionalQueryParamDecoderMatcher[Int]("page")
  object OptionalSearchMatcher extends OptionalQueryParamDecoderMatcher[String]("Search")
}