package service.auth

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.{CanBeQueryCondition, Rep}
import tables.Tables._

import scala.concurrent.{ExecutionContext, Future}


// 認証周りのロジックをかく
// e.g. バリデーションなど
class AuthUserService @Inject()(
                                 val dbConfigProvider: DatabaseConfigProvider
                               )(implicit val ec: ExecutionContext, implicit val wt: CanBeQueryCondition[Any]) extends HasDatabaseConfigProvider[JdbcProfile]{

  def emailValidate(email: String): Future[Option[AuthUser]] = {
    // TODO: AuthUser tableからemailが一致するuserを取り出す
    // db.run()でFuture[R]が返ってくる
    val authUser = db.run {
      AuthUser.filter(authUser => authUser.email === email.bind).result
    }
    authUser.flatMap {
      // TODO: Option[AuthUser]で返す
      case
    }

    Future{Some(AuthUser)}


    db.run().map { user: AuthUser =>
      case (u, Some(a), None) => Option[AuthUser](u)
      case _ => None
    }
  }

}
