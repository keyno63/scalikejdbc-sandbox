package com.github.keyno63.scalikejdbc.app

import scalikejdbc._

case class Issue(id: Int, summary: String, desc: String)

case class IssueId(value: Int)

object Issue extends SQLSyntaxSupport[Issue] {
  implicit val issued = ParameterBinderFactory[IssueId] {
    value => (stmt, idx) => stmt.setInt(idx, value.value)
  }

  override val tableName = "issues"
  val i = Issue.syntax("i")

  def apply(rs: WrappedResultSet): Issue =
    Issue(rs.int("i_on_i"), rs.string("s_on_i"), rs.string("d_on_i"))
}

case class Test(id: Int, summary: String, desc: String)

object Test extends SQLSyntaxSupport[Issue] {
  override val tableName = "test_schema"
  def apply(rs: WrappedResultSet) =
    Issue(rs.int("id"), rs.string("summary"), rs.string("description"))
}
