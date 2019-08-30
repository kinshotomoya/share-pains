package service.member

import java.net.SocketOption
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import tables.Tables._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

class MemberService @Inject()(
                               val dbConfigProvider: DatabaseConfigProvider
                             )(implicit val ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  // TODO: ほんまはUUIDを更新しないけど、joinの練習と、代替案が思い浮かばなかったからしゃーなし。。。
  def findUUIDByAuthUserEmailAndUpdateUUID(email: String, uuid: String): Unit = {
    // DB.runを二回するには、いいのか？？
    db.run(AuthUser.filter(_.email === email).result.headOption).map {
      authUser => {
        val value = Member.filter(_.authUserId === authUser.get.authUserId).map(_.uuid)
        db.run(value.update(uuid))
      }
    }
  }

  private def getNowTimeStamp: Timestamp = {
    val today: Date = Calendar.getInstance.getTime
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    java.sql.Timestamp.valueOf(timeFormat.format(today))
  }


    // SQL
    // select m.uuid from member m inner join auth_user au on m.auth_user_id = au.auth_user_id where au.email = "email";
//    val query = Member.join(AuthUser).on(_.authUserId === _.authUserId)
//      .filter {
//        case (m, au) =>
//          au.email === email
//      }.map {
//      case (m, au) =>
//        m
//    }.map(_.uuid).update(uuid)
//    val action = query.transactionally
    // Future[Option[String]]をflatMapでOptionの入れ子を無くして、平らにしている

}
