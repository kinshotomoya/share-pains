package controllers

import javax.inject._
import play.api.data.{Form, Mapping}
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.MessagesApi
import play.api.mvc._
import play.api.db.slick._
import service.auth.AuthUserService
import service.validator.SignupValidator
import slick.jdbc.{JdbcBackend, JdbcProfile}
import slick.driver.JdbcProfile
import tables.Tables._

import scala.concurrent.Future


@Singleton
class SignupController @Inject() (val dbConfigProvider: DatabaseConfigProvider,
                                  val messagesApi: MessagesApi,
                                  val authService: AuthUserService
                                 )
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
      formWithError =>
        BadRequest(views.html.signup(signUpForm)).future,
      data =>
        authService.emailValidate(data.email) match {
          case true => {
            // TODO: ユーザー保存する
          }
          case false => {
            // サインアップ画面にリダイレクトする
          }
        }
    )
  }

//  def signup = Action.async { implicit req =>
    // signup処理を書く

//    signUpForm.bindFromRequest().fold(
//      error => {
//        // バリデーションerrorの場合
//        println("fail")
//        println(error.errors)
//        Redirect(routes.SignupController.rendersignupPage())
//      },
//      form => {
//        // 成功の場合
//        println("success")
//        println(form)
//        // formdataを保存する
//        Redirect(routes.HomeController.index())
//      }
//    )
//  }
}

// コンパニオンオブジェクト
// controllerに定義するのはちゃう物をここに切り出す
object SignupController {
  case class SignUpForm(email: String, password: String)

}
