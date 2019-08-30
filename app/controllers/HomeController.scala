package controllers

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import service.post.PostService
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import tables.Tables._
import service.post.models.DisplayPostContent

import scala.concurrent.ExecutionContext


@Singleton
class HomeController @Inject()(val dbConfigProvider: DatabaseConfigProvider, val postService: PostService)(implicit val ec: ExecutionContext) extends Controller with HasDatabaseConfigProvider[JdbcProfile] with BaseController {

  def index = Action.async { implicit request =>
    // .resultでDBIOAction型に変換している
    db.run(Post.sortBy(_.createdAt.desc).join(Member).on(_.memberId === _.memberId).result).map { posts =>
      val contents = postService.tupleDisplayContent(posts)
      Ok(views.html.index(contents))
    }
  }

  def generateTable = Action { implicit request =>
    slick.codegen.SourceCodeGenerator.main(
      Array("slick.jdbc.MySQLProfile", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/sharepains?nullNamePatternMatchesAll=true", "./app", "tables", "user", "password")
    )
    Redirect(routes.HomeController.index())
  }
}
