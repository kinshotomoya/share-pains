package controllers

import java.text.SimpleDateFormat

import javax.inject._
import play.api.data.Form
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import service.auth.AuthUserService
import tables.Tables._
import java.util.{Calendar, Date}

import play.api.mvc._
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import javax.inject.Inject
import slick.jdbc.JdbcProfile
// slickの文法にするimplicitメソッドなどを定義している
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SignupController @Inject() (val dbConfigProvider: DatabaseConfigProvider,
                                  val messagesApi: MessagesApi,
                                  val authService: AuthUserService
                                 )(implicit ec: ExecutionContext)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] with BaseController {

  import SignupController._

  val signUpForm = Form(
    mapping(
      // 電子メールの正規表現を使用する
      "email" -> email,
      "password" -> nonEmptyText(maxLength = 20, minLength = 10)
    )(SignUpForm.apply)(SignUpForm.unapply)
  )

  def rendersignupPage = Action { implicit req =>
    Ok(views.html.signup(signUpForm, ""))
  }

  def signup = Action.async { implicit req =>
    signUpForm.bindFromRequest().fold(
      formWithError => {
        // Future型を返さないといけない
        // TODO: implicit classで、暗黙的にFuture型を返すメソッドを定義する
        BadRequest(views.html.signup(formWithError, "適切なemail passwordを入力してください。")).future
      },
      data =>
        // flatMapすることで、戻り値のFuture{Option[AuthUser]}のOption[AuthUser]が、flatMapの中で利用できる
        authService.emailValidate(data.email) flatMap {
          case false => {
            // TODO: とりあえず、AuthUserのemailをセッションに格納している。
            // createしたauthUserのIDを取得したい
            val user = AuthUserRow(0, data.email, authService.doHashPassword(data.password), java.sql.Timestamp.valueOf(getNowTime))
            db.run(AuthUser += user).map { _ =>
              Redirect(routes.HomeController.index()).withSession("userInfo" -> user.email)
            }
          }
            // すでに存在したら
          case true => {
            // サインアップ画面にリダイレクトする
            BadRequest(views.html.signup(signUpForm, "そのemailはすでに存在しています。")).future
          }
        }
    )
  }

  private def getNowTime: String = {
    val today: Date = Calendar.getInstance.getTime
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    timeFormat.format(today)
  }
}

// コンパニオンオブジェクト
// controllerに定義するのはちゃう物をここに切り出す
// TODO: 特にコンパニオンオブジェクトにする必要はないので、case classを他のファイルに切り出す
object SignupController {
  case class SignUpForm(email: String, password: String)
}
