package tables
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = AuthUser.schema ++ Member.schema ++ Post.schema ++ SchemaVersion.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table AuthUser
   *  @param authUserId Database column auth_user_id SqlType(INT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true)
   *  @param hashedPassword Database column hashed_password SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(DATETIME) */
  final case class AuthUserRow(authUserId: Int, email: String, hashedPassword: String, createdAt: java.sql.Timestamp)
  /** GetResult implicit for fetching AuthUserRow objects using plain SQL queries */
  implicit def GetResultAuthUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[AuthUserRow] = GR{
    prs => import prs._
    AuthUserRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table auth_user. Objects of this class serve as prototypes for rows in queries. */
  class AuthUser(_tableTag: Tag) extends profile.api.Table[AuthUserRow](_tableTag, Some("sharepains"), "auth_user") {
    def * = (authUserId, email, hashedPassword, createdAt) <> (AuthUserRow.tupled, AuthUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authUserId), Rep.Some(email), Rep.Some(hashedPassword), Rep.Some(createdAt)).shaped.<>({r=>import r._; _1.map(_=> AuthUserRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column auth_user_id SqlType(INT), AutoInc, PrimaryKey */
    val authUserId: Rep[Int] = column[Int]("auth_user_id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column hashed_password SqlType(VARCHAR), Length(255,true) */
    val hashedPassword: Rep[String] = column[String]("hashed_password", O.Length(255,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")

    /** Uniqueness Index over (email) (database name email) */
    val index1 = index("email", email, unique=true)
  }
  /** Collection-like TableQuery object for table AuthUser */
  lazy val AuthUser = new TableQuery(tag => new AuthUser(tag))

  /** Entity class storing rows of table Member
   *  @param memberId Database column member_id SqlType(INT), AutoInc, PrimaryKey
   *  @param nickname Database column nickname SqlType(VARCHAR), Length(45,true)
   *  @param uuid Database column uuid SqlType(VARCHAR), Length(55,true)
   *  @param authUserId Database column auth_user_id SqlType(INT)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME) */
  final case class MemberRow(memberId: Int, nickname: String, uuid: String, authUserId: Int, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends profile.api.Table[MemberRow](_tableTag, Some("sharepains"), "member") {
    def * = (memberId, nickname, uuid, authUserId, createdAt, updatedAt) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(nickname), Rep.Some(uuid), Rep.Some(authUserId), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(INT), AutoInc, PrimaryKey */
    val memberId: Rep[Int] = column[Int]("member_id", O.AutoInc, O.PrimaryKey)
    /** Database column nickname SqlType(VARCHAR), Length(45,true) */
    val nickname: Rep[String] = column[String]("nickname", O.Length(45,varying=true))
    /** Database column uuid SqlType(VARCHAR), Length(55,true) */
    val uuid: Rep[String] = column[String]("uuid", O.Length(55,varying=true))
    /** Database column auth_user_id SqlType(INT) */
    val authUserId: Rep[Int] = column[Int]("auth_user_id")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")

    /** Foreign key referencing AuthUser (database name member_ibfk_1) */
    lazy val authUserFk = foreignKey("member_ibfk_1", authUserId, AuthUser)(r => r.authUserId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table Post
   *  @param postId Database column post_id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param content Database column content SqlType(VARCHAR), Length(1024,true)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param memberId Database column member_id SqlType(INT) */
  final case class PostRow(postId: Long, content: String, createdAt: java.sql.Timestamp, memberId: Int)
  /** GetResult implicit for fetching PostRow objects using plain SQL queries */
  implicit def GetResultPostRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Int]): GR[PostRow] = GR{
    prs => import prs._
    PostRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[Int]))
  }
  /** Table description of table post. Objects of this class serve as prototypes for rows in queries. */
  class Post(_tableTag: Tag) extends profile.api.Table[PostRow](_tableTag, Some("sharepains"), "post") {
    def * = (postId, content, createdAt, memberId) <> (PostRow.tupled, PostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(postId), Rep.Some(content), Rep.Some(createdAt), Rep.Some(memberId)).shaped.<>({r=>import r._; _1.map(_=> PostRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column post_id SqlType(BIGINT), AutoInc, PrimaryKey */
    val postId: Rep[Long] = column[Long]("post_id", O.AutoInc, O.PrimaryKey)
    /** Database column content SqlType(VARCHAR), Length(1024,true) */
    val content: Rep[String] = column[String]("content", O.Length(1024,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column member_id SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("member_id")

    /** Foreign key referencing Member (database name post_ibfk_1) */
    lazy val memberFk = foreignKey("post_ibfk_1", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Post */
  lazy val Post = new TableQuery(tag => new Post(tag))

  /** Entity class storing rows of table SchemaVersion
   *  @param installedRank Database column installed_rank SqlType(INT), PrimaryKey
   *  @param version Database column version SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param description Database column description SqlType(VARCHAR), Length(200,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
   *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
   *  @param checksum Database column checksum SqlType(INT), Default(None)
   *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
   *  @param executionTime Database column execution_time SqlType(INT)
   *  @param success Database column success SqlType(BIT) */
  final case class SchemaVersionRow(installedRank: Int, version: Option[String] = None, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]], e4: GR[java.sql.Timestamp], e5: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
    SchemaVersionRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends profile.api.Table[SchemaVersionRow](_tableTag, Some("sharepains"), "schema_version") {
    def * = (installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(installedRank), version, Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column installed_rank SqlType(INT), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)
    /** Database column version SqlType(VARCHAR), Length(50,true), Default(None) */
    val version: Rep[Option[String]] = column[Option[String]]("version", O.Length(50,varying=true), O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INT), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INT) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BIT) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name schema_version_s_idx) */
    val index1 = index("schema_version_s_idx", success)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))
}
