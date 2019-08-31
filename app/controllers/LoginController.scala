package controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, _}
import play.api.mvc.{Action, Controller}
import service.auth.AuthUserService
import service.auth.models.LoginForm
import service.common.Common
import service.member.MemberService

import scala.concurrent.ExecutionContext

@Singleton
class LoginController @Inject()(authUserService: AuthUserService, memberService: MemberService)(implicit ec: ExecutionContext) extends Controller with BaseController with Common {

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 10)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def renderLoginPage = Action { implicit req =>
    // sessionがあったら、index ページに飛ばす
    // なければ、loginページに飛ばす
    // Optionで返って来るのでmatchで存在するかどうかを確認する
    req.session.get("userInfo") match {
      case Some(session) => Redirect(routes.HomeController.index())
      case None => Ok(views.html.login(""))
    }
  }

  def login = Action.async { implicit req =>
    // sessionを削除
    req.session.-("userInfo")
    loginForm.bindFromRequest().fold(
      formWithError => {
        // errorの時
        BadRequest(views.html.login("")).future
      },
      data => {
        // デフォルトのバリデーションが通った時
        // Future[Boolean]
        // Future flatMapの時は、Futureは潰される。なので、Future[Boolean].flatMapの戻り値は、Booleanになる。
        authUserService.loginValidate(data.email, data.password) map {
          case true => {
            // emailからauthuserに紐づいているmemberのuuidを取得する
            // val futureUUID: Future[String] = memberService.findUUIDByAuthUserEmail(data.email)
            val uuid = authUserService.createUuid
            memberService.findUUIDByAuthUserEmailAndUpdateUUID(data.email, uuid)
            Redirect(routes.HomeController.index()).withSession("userInfo" -> uuid)
          }
          case false => BadRequest(views.html.login(""))
        }
      }
    )
  }

  def logout = Action { implicit req =>
    req.session.-("userInfo")
    Redirect(routes.LoginController.renderLoginPage())
  }
}
