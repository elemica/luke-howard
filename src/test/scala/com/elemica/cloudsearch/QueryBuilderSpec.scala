package com.elemica.cloudsearch

import org.scalatest._

class QueryBuilderSpec extends WordSpec with ShouldMatchers {
  "CloudSearchQueryBuilder" should {
    "handle single any value terms" in {
      QueryBuilder("bacon") should equal("'bacon'")
    }

    "handle a single specific field value term" in {
      QueryBuilder("field:bacon") should equal("field:'bacon'")
    }

    "handle a multi word value term" in {
      QueryBuilder("'bacon sammich'") should equal("'bacon sammich'")
    }

    "handle a multi word single field value search" in {
      QueryBuilder("field:'bacon sammich'") should equal("field:'bacon sammich'")
    }

    "handle multiple search terms" in {
      QueryBuilder("abc 123 field:'bacon'") should equal("(and 'abc' '123' field:'bacon')")
    }

    "doesn't fail with periods in the search criteria" in {
      QueryBuilder("blah:..1374011854") should equal("blah:'..1374011854'")
    }

    "handles numeric fields" in {
      QueryBuilder("objecttimestamp:2") should equal("objecttimestamp:2")
      QueryBuilder("bacon:3") should equal("bacon:'3'")
    }

    "doesn't fail with dashes in the search criteria" in {
      QueryBuilder("-2334") should equal("'-2334'")
    }

    "handles field specifiers with no query content as regular value searches" in {
      QueryBuilder("field:") should equal("'field:'")
      QueryBuilder("field: abc") should equal("(and 'field:' 'abc')")
    }

    "treats colons in search criteria correctly" in {
      QueryBuilder("'bacon:8080'") should equal("'bacon:8080'")
    }

    "handle fields with multiple possible values" in {
      QueryBuilder("abc:123|456 cde:456") should equal("(and (or abc:'123' abc:'456') cde:'456')")
      QueryBuilder("abc:123|456|789 cde:456") should equal("(and (or abc:'123' abc:'456' abc:'789') cde:'456')")
      QueryBuilder("abc:'123'|'456'|'789' cde:456") should equal("(and (or abc:'123' abc:'456' abc:'789') cde:'456')")
    }

    "should not recognize values separator inside quote" in {
      QueryBuilder("field:'this|is|single|value'") should equal("field:'this|is|single|value'")
    }
  }
}
