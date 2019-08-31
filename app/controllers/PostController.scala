package controllers


import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import service.common.Common
import service.post.PostService
import service.post.models.PostForm
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class PostController @Inject()(postService: PostService)(val dbConfigProvider: DatabaseConfigProvider)(implicit val ec: ExecutionContext) extends Controller with HasDatabaseConfigProvider[JdbcProfile] with BaseController with Common {

  val postForm = Form(
    mapping(
      "content" -> nonEmptyText(maxLength = 1024)
    )(PostForm.apply)(PostForm.unapply)
  )

  def renderPostPage = Action { implicit req =>
    req.session.get("userInfo") match {
      case Some(uuid) => Ok(views.html.post(""))
      case None => Redirect(routes.LoginController.renderLoginPage())
    }
  }

  def post = Action.async { implicit req =>
    postForm.bindFromRequest().fold(
      formWithError => {
        BadRequest(views.html.post("")).future
      },
      data => {
        // TODO: sessionがないときにエラー起きるので、getOrElseなどで条件分けて回避したい
        val uuid: String = req.session.get("userInfo").get
        postService.create(data, uuid).map {
          case true => Redirect(routes.HomeController.index())
          case false => BadRequest(views.html.post(""))
        }
      }
    )
  }
}
