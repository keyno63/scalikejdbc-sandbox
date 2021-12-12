package com.github.keyno63.scalikejdbc.app

import scalikejdbc.ConnectionPool.CPFactory
import scalikejdbc._
import scalikejdbc.config.DBs.{ config, setupAll }

class DoScalikeJdbc {

  val config = new Config

  private val boneCpFactory = BoneCPConnectionPoolFactory
  private val c2c = Commons2ConnectionPoolFactory
  private val cc = CommonsConnectionPoolFactory

  implicit val factory: CPFactory = boneCpFactory
  ConnectionPoolFactoryRepository.add("default", factory)

  Class.forName(config.db.driverName)
  ConnectionPool.singleton(
    "jdbc:postgresql://localhost:25432/world",
    config.db.user,
    config.db.password
  )

  println("=== setupALl ===")
  setupAll()

  def findAll(): List[Issue] = {
    DB readOnly { implicit session =>
      {
        println(s"session: ${session}")
        sql"""
           SELECT id, summary, description FROM issues
         """
          .map { rs =>
            Issue(
              rs.int("id"),
              rs.string("summary"),
              rs.string("description")
            )
          }
          .toList
          .apply()
      }
    }

  }

  def findReadOnlyAutoCommitFind()(implicit
    session: DBSession = ReadOnlyAutoSession
  ): List[Issue] = {
    println(s"session: ${session}")
    sql"SELECT id, summary, description FROM issues"
      .map { rs =>
        Issue(
          rs.int("id"),
          rs.string("summary"),
          rs.string("description")
        )
      }
      .toList
      .apply()
  }

  def findUnion() = {
    val i = Issue.syntax("i")

    DB readOnly { implicit session =>
      selectFrom(Issue.as(i))
        .orderBy(i.id)
        .unionAll(
          selectFrom(Issue.as(i)).orderBy(i.id)
        )
        .toSQL
        .map(Issue(_))
        .toList
        .apply()
    }
  }

  def findUnion1() = {
    val i = Issue.syntax("i")

    DB readOnly { implicit session =>
      withSQL {
        selectFrom(Issue.as(i))
          .orderBy(i.id)
          .where
          .eq(i.id, 1)
          .union(
            selectFrom(Issue.as(i)).where
              .eq(i.id, 1)
              .orderBy(i.id)
          )
      }.map(Issue(_))
        .toList
        .apply()

    }
  }

  def findUnion2() = {
    val i = Issue.syntax("i")

    DB readOnly { implicit session =>
      sql"(select * from issues order by id) union (select * from issues order by id)"
        .map(rs =>
          Issue(
            rs.int("id"),
            rs.string("summary"),
            rs.string("description")
          )
        )
        .toList
        .apply()
    }
  }

  def findLimitUnion() = {
    val i = Issue.syntax("i")
    DB readOnly { implicit session =>
      withSQL {
        selectFrom(Issue.as(i))
          .limit(2)
          .union(
            selectFrom(Issue.as(i))
              .limit(2)
          )
      }.map(Issue(_))
        .toList
        .apply()
    }
  }

}
