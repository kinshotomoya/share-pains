package service.auth

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import tables.Tables.{AuthUser, _}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


// 認証周りのロジックをかく
// e.g. バリデーションなど
class AuthUserService @Inject()(
                                 val dbConfigProvider: DatabaseConfigProvider
                               )(implicit val ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  def emailValidate(email: String): Future[Boolean] = {
    // すでに存在するかどうかを確認
    // db.run()でFuture[R]が返ってくる
    db.run {
      AuthUser.filter(authUser => authUser.email === email.bind).exists.result
    }
  }
}
