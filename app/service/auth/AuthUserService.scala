package service.auth

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject.Inject
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import service.auth.models.SignUpForm
import slick.jdbc.JdbcProfile
import tables.Tables.{AuthUser, _}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


// 認証周りのロジックをかく
// e.g. バリデーションなど
class AuthUserService @Inject()(
                                 val dbConfigProvider: DatabaseConfigProvider
                               )(implicit val ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  def findByEmail(email: String): Future[Option[AuthUserRow]] = {
    // すでに存在するかどうかを確認
    // db.run()でFuture[R]が返ってくる
    // 型の定義が曖昧なので、明示的に型を記述している
    val query = AuthUser.filter(_.email === email.bind)
    val action: DBIO[Option[AuthUserRow]] = query.result.headOption
    val result: Future[Option[AuthUserRow]] = db.run(action)
    result
  }

  def createAuthUser(data: SignUpForm, uuid: String): Future[Unit] = {
    val user = AuthUserRow(0, data.email, doHashPassword(data.password), getNowTimeStamp)
    val action =  (
      for {
        authUserId: Int <- AuthUser returning AuthUser.map(_.authUserId) += user
        _ <- Member += MemberRow(0,  data.nickname, uuid, authUserId, getNowTimeStamp, getNowTimeStamp)
      } yield ()).transactionally
    // TODO: memberも作成する
    // uuidを作る
    // transaction
//    val user = AuthUserRow(0, data.email, doHashPassword(data.password), java.sql.Timestamp.valueOf(getNowTimeStamp))
    // returningで、戻り値を指定できる
    // createしたauthUserのIDを指定ている
//    val action: DBIO[Int] = AuthUser returning AuthUser.map(_.authUserId) += user
    db.run(action)
  }

  def createUuid: String = {
    java.util.UUID.randomUUID().toString
  }

  def loginValidate(email: String, password: String): Future[Boolean] = {
    this.findByEmail(email) flatMap {
      case Some(authUser) => Future { checkPassword(password) }
      case None => Future { false }
    }
  }

  private def doHashPassword(password: String): String = {
    // passwordをハッシュ（ソルト・ストレッチング）
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  private def checkPassword(password: String): Boolean = {
    BCrypt.checkpw(password, doHashPassword(password))
  }

  private def getNowTimeStamp: Timestamp = {
    val today: Date = Calendar.getInstance.getTime
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    java.sql.Timestamp.valueOf(timeFormat.format(today))
  }
}
