package controllers

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import tables.Tables._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val dbConfigProvider: DatabaseConfigProvider)(implicit val ec: ExecutionContext) extends Controller with HasDatabaseConfigProvider[JdbcProfile] with BaseController {

  def index = Action.async { implicit request =>
    // .resultでDBIOAction型に変換している
    db.run(Post.sortBy(_.createdAt.desc).result).map { posts =>
      Ok(views.html.index(posts))
    }
  }

  def generateTable = Action { implicit request =>
    slick.codegen.SourceCodeGenerator.main(
      Array("slick.jdbc.MySQLProfile", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/sharepains?nullNamePatternMatchesAll=true", "./app", "tables", "user", "password")
    )
    Redirect(routes.HomeController.index())
  }
}
