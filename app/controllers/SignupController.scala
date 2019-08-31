package controllers

import javax.inject.{Inject, _}
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.MessagesApi
import play.api.mvc._
import service.auth.AuthUserService
import service.auth.models.SignUpForm
import service.common.Common
import slick.jdbc.JdbcProfile

// slickの文法にするimplicitメソッドなどを定義している
import scala.concurrent.ExecutionContext


@Singleton
class SignupController @Inject()(val messagesApi: MessagesApi,
                                 val authService: AuthUserService
                                )(implicit ec: ExecutionContext)
  extends Controller with BaseController with Common{

  val signUpForm = Form(
    mapping(
      // 電子メールの正規表現を使用する
      "nickname" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText(maxLength = 20, minLength = 10)
    )(SignUpForm.apply)(SignUpForm.unapply)
  )

  def rendersignupPage = Action { implicit req =>
    Ok(views.html.signup(""))
  }

  def signup = Action.async { implicit req =>
    signUpForm.bindFromRequest().fold(
      formWithError => {
        // Future型を返さないといけない
        BadRequest(views.html.signup("適切なemail passwordを入力してください。")).future
      },
      data =>
        // mapすることで、戻り値のFuture{Option[AuthUser]}のOption[AuthUser]が、mapの中で利用できる
        // flatMapを使う必要はない。flatMapはmap flattenをしているだけで、複数の中身を潰して平らにしたいとkに
        authService.findByEmail(data.email) map {
          case None => {
            val uuid: String = authService.createUuid
            authService.createAuthUser(data, uuid)
            Redirect(routes.HomeController.index()).withSession("userInfo" -> uuid)
          }
          // すでに存在したら
          case Some(authUser) => {
            // サインアップ画面にリダイレクトする
            BadRequest(views.html.signup("そのemailはすでに存在しています。"))
          }
        }
    )
  }
}
