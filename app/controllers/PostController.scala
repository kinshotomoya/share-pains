package controllers

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import service.post.{PostForm, PostService}
import tables.Tables._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext

class PostController @Inject() (postService: PostService) (val dbConfigProvider: DatabaseConfigProvider)(implicit val ec: ExecutionContext) extends Controller with HasDatabaseConfigProvider[JdbcProfile] with BaseController{

  val postForm = Form(
    mapping(
      "content" -> nonEmptyText(maxLength = 1024)
    )(PostForm.apply)(PostForm.unapply)
  )

  def post = Action.async { implicit req =>
    postForm.bindFromRequest().fold(
      formWithError => {
        BadRequest().future
      },
      data => {
        val uuid: String = req.session.get("userInfo").getOrElse(Redirect(routes.LoginController.renderLoginPage()).future)
        postService.create(data, uuid).map {
          case true => Redirect(routes.LoginController.renderLoginPage())
          case false => BadRequest()
        }
      }
    )
  }
}
