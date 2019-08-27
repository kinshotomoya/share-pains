package controllers

import java.sql.Timestamp
import java.text.SimpleDateFormat

import javax.inject._
import play.api.data.{Form, Mapping}
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.MessagesApi
import play.api.mvc._
import play.api.db.slick._
import play.core.routing.Route
import service.auth.AuthUserService
import service.validator.SignupValidator
import slick.jdbc.{JdbcBackend, JdbcProfile}
import slick.driver.JdbcProfile
import tables.Tables._
import java.time.LocalDateTime
import java.util.{Calendar, Date}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SignupController @Inject() (val dbConfigProvider: DatabaseConfigProvider,
                                  val messagesApi: MessagesApi,
                                  val authService: AuthUserService
                                 )(implicit ec: ExecutionContext)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  import SignupController._

  val signUpForm = Form(
    mapping(
      // 電子メールの正規表現を使用する
      "email" -> email,
      "password" -> nonEmptyText(maxLength = 20, minLength = 10)
    )(SignUpForm.apply)(SignUpForm.unapply)
  )

  def rendersignupPage = Action { implicit req =>
    Ok(views.html.signup(signUpForm))
  }

  def signup = Action.async { implicit req =>
    signUpForm.bindFromRequest().fold(
      formWithError => {
        // Future型を返さないといけない
        // TODO: implicit classで、暗黙的にFuture型を返すメソッドを定義する
        Future.successful(BadRequest(views.html.signup(formWithError)))
      },
      data =>
        // flatMapすることで、戻り値のFuture{Option[AuthUser]}のOption[AuthUser]が、flatMapの中で利用できる
        authService.emailValidate(data.email).flatMap {
          case None => {
            val today: Date = Calendar.getInstance.getTime
            val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val now: String = timeFormat.format(today)
            val authUser = AuthUserRow(0, data.email, data.password, java.sql.Timestamp.valueOf(now))
            // TODO:　よーわからん
            db.run(AuthUser += authUser).map { _ =>
              Redirect(routes.HomeController.index())
            }
          }
            // すでに存在したら
          case Some(user) => {
            // サインアップ画面にリダイレクトする
            Future.successful(BadRequest(views.html.signup(signUpForm)))
          }
        }
    )
  }
}

// コンパニオンオブジェクト
// controllerに定義するのはちゃう物をここに切り出す
object SignupController {
  case class SignUpForm(email: String, password: String)

}
