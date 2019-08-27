package service.validator

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.MessagesApi
import play.api.mvc.Controller
import slick.jdbc.JdbcProfile


class SignupValidator @Inject() (val dbConfigProvider: DatabaseConfigProvider)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  def emailValidation(email: String): Boolean = {
    true
  }

}
