package service.post

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import service.common.Common
import service.post.models.{DisplayPostContent, PostForm}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import tables.Tables._

import scala.concurrent.{ExecutionContext, Future}

class PostService @Inject()(
                             val dbConfigProvider: DatabaseConfigProvider
                           )(implicit val ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with Common{


  def create(data: PostForm, uuid: String): Future[Boolean] = {
    db.run(Member.filter(_.uuid === uuid).result.headOption).flatMap {
      case Some(member) => {
        println(member.memberId)
        db.run(Post += PostRow(0L, data.content, getNowTimeStamp, member.memberId))
        true.future
      }
      case None => false.future
    }
  }

  def tupleDisplayContent(posts: Seq[(PostRow, MemberRow)]): Seq[DisplayPostContent] = {
    posts.map { post =>
      DisplayPostContent(post._1.content, post._2.nickname, post._1.postId)
    }
  }

  private def getNowTimeStamp: Timestamp = {
    val today: Date = Calendar.getInstance.getTime
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    java.sql.Timestamp.valueOf(timeFormat.format(today))
  }
}
