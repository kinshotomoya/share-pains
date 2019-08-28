package controllers

import controllers.SignupController.SignUpForm
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.mvc.{Action, Controller}
import service.auth.AuthUserService

import scala.concurrent.ExecutionContext

@Singleton
class LoginController @Inject() (authUserService: AuthUserService) (implicit ec: ExecutionContext) extends Controller with BaseController {

  import LoginController._

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 10)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def renderLoginPage = Action { implicit req =>
    // sessionがあったら、index ページに飛ばす
    // なければ、logninページに飛ばす
    // Optionで返って来るのでmatchで存在するかどうかを確認する
    req.session.get("userInfo") match {
      case Some(session) => Redirect(routes.HomeController.index())
      case None => Ok(views.html.login())
    }
  }

  def customLogin = Action.async { implicit req =>
    loginForm.bindFromRequest().fold(
      formWithError => {
        // errorの時
        BadRequest(views.html.login()).future
      },
      data => {
        // デフォルトのバリデーションが通った時
        authUserService.loginValidate(data.email, data.password) flatMap {
          case true => Redirect(routes.HomeController.index()).future
          case false => BadRequest(views.html.login()).future
        }
      }
    )
  }
}

object LoginController {
  case class LoginForm(email: String, password: String)
}
