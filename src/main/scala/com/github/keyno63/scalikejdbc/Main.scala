package com.github.keyno63.scalikejdbc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.github.keyno63.scalikejdbc.app.DoScalikeJdbc

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends scala.App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val repository = new DoScalikeJdbc
  val route =
    path("") {
      get {
        complete(200, "Ok")
      }
    } ~ pathPrefix("scalikejdbc") {
      path("test") {
        get {
          parameter(Symbol("key").?) {
            case Some(key) =>
              key match {
                case "find_test" => {
                  complete(200, repository.findAll().toString())
                }
                case "find_test2" => {
                  complete(
                    200,
                    repository.findReadOnlyAutoCommitFind().toString()
                  )
                }
                case "union" => {
                  complete(200, repository.findUnion().toString())
                }
                case "union1" => {
                  complete(200, repository.findUnion1().toString())
                }
                case "union2" => {
                  complete(200, repository.findUnion2().toString())
                }
                case _ => {
                  complete(200, "Ok, not set key")
                }
              }
            case _ =>
              complete(200, "Ok, not set key")
          }
        }
      }
    }

  // start HTTP Server
  val port = 5000
  val bindingFuture = Http()
    .newServerAt("localhost", port)
    .bind(route)
  println(s"start server. port=[${port}]")

  // To stop server
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
