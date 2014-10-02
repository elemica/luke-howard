# luke-howard

Amazon's [CloudSearch](http://aws.amazon.com/cloudsearch/) is awesome, but it's query
syntax is not for explaining to end-users. Enter luke-howard. This is a Scala library we
wrote at Elemica to expose some of the advanced query functionality of CloudSearch to
our users in a almost "search-engine-esque" syntax.

It exposes support for:

* Searching for multiple terms.
* Searching only against a specific field.
* Searching a specific field for one of a selection of values.
* Negative searches (or, `not`s, per se).
* Rudamentary support for numeric fields. (This will get better as we iterate.)

The project is named after the British Scientist
[Luke Howard](http://en.wikipedia.org/wiki/Luke_Howard) who is responsible for the
nomenclature that we commonly use for clouds. Much like Mr. Howard gave us some
understandable terms to talk about the clouds in the sky, we hope this project will
give end users some understandable means of asking questions of Amazon's CloudSearch.

## Usage

Using luke-howard is a breeze. Simply import the `QueryBuilder` class from the
`com.elemica.cloudsearch` package and go to down.

```scala
import com.elemica.cloudsearch.QueryBuilder

QueryBuilder("bacon")       // search for bacon
QueryBuilder("field:bacon") // search for bacon in the field named "field"
QueryBuilder("field:-123")  // search for records where field doesn't match 123
```

A complete set of examples can always be found in the [QueryBuilderSpec](https://github.com/elemica/luke-howard/blob/master/src/test/scala/com/elemica/cloudsearch/QueryBuilderSpec.scala).

## License

This project is ©2014 Elemica, and is licensed under the Apache 2.0 License. It is
provided on an as-is basis without warranty of any kind. For more information, please
see the [LICENSE](https://github.com/elemica/luke-howard/blob/master/LICENSE) file.
