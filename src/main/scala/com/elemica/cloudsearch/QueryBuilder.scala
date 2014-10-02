package com.elemica.cloudsearch

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharSequenceReader

object CloudSearchQueryTerm {
  val numericFields = "objecttimestamp" :: Nil
}
sealed trait CloudSearchQueryTerm {
  def serializedTerm: String
}
case class AnyFieldContentQuery(query: String) extends CloudSearchQueryTerm {
  val serializedTerm = "'" + query + "'"
}
case class SingleFieldContentQuery(fieldName: String, query: String) extends CloudSearchQueryTerm {
  val serializedTerm = {
    if (CloudSearchQueryTerm.numericFields.contains(fieldName))
      fieldName + ":" + query
    else
      fieldName + ":'" + query + "'"
  }
}

sealed trait CloudSearchQueryClause extends CloudSearchQueryTerm {
  lazy val serializedTerm = serializedClause
  def serializedClause: String
}
case class PlainQueryClause(term: CloudSearchQueryTerm) extends CloudSearchQueryClause {
  val serializedClause = term.serializedTerm
}

sealed trait CondensingQueryClause extends CloudSearchQueryClause {
  def clauseOperator: String
  def clauses: List[CloudSearchQueryClause]

  lazy val serializedClause = clauses match {
    case Nil =>
      ""

    case singleTerm :: Nil =>
      singleTerm.serializedClause

    case _ =>
      "(" + clauseOperator + " " + clauses.map(_.serializedClause).mkString(" ") + ")"
  }
}
case class AndQueryClause(clauses: List[CloudSearchQueryClause]) extends CondensingQueryClause {
  val clauseOperator = "and"
}
case class OrQueryClause(clauses: List[CloudSearchQueryClause]) extends CondensingQueryClause {
  val clauseOperator = "or"
}

object QueryBuilder extends RegexParsers with ImplicitConversions {

  // all non-whitespace characters except '|'
  private lazy val nakedTerm: Parser[String] = "[\\S&&[^\\|]]+".r

  // 'in quotes' or "in quotes"
  private lazy val quoteTerm: Parser[String] = {
    "('|\")[^']*('|\")".r ^^ {
      case token => token.drop(1).dropRight(1)
    }
  }

  private lazy val fieldSpecifierTerm: Parser[String] = "\\w+".r <~ ":(?=\\S)".r

  private lazy val fieldValuesTerm: Parser[List[String]] = rep1sep(quoteTerm | nakedTerm, "|")

  lazy val fieldSpecifierQueryTerm: Parser[CloudSearchQueryTerm] = {
    phrase(rep(opt(fieldSpecifierTerm) ~ fieldValuesTerm)) ^^ {
      case termSequence =>
        val terms = termSequence.collect({

          case Some(fieldSpecifier) ~ (fieldValue :: Nil) =>
            PlainQueryClause(SingleFieldContentQuery(fieldSpecifier, fieldValue))

          case Some(fieldSpecifier) ~ fieldValues =>
            OrQueryClause(fieldValues.map(value => PlainQueryClause(SingleFieldContentQuery(fieldSpecifier, value))))

          case None ~ (term :: Nil) =>
            PlainQueryClause(AnyFieldContentQuery(term))
        })

        AndQueryClause(terms)
    }
  }

  def apply(input: String): String = {
    val reader: Input = new CharSequenceReader(input, 0)
    fieldSpecifierQueryTerm.apply(reader).map(_.serializedTerm).getOrElse("")
  }
}
