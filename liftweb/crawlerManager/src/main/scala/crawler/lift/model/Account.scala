package crawler.lift.model

import net.liftweb.common.{Box, Full, Empty, Failure}
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.util.Helpers

object Account {
  val cookieName = "learn.lift"

  def create(): AccountImpl = {
    // new AccountImpl(User.createRecord)
    new AccountImpl(new User)
  }

  def apply(cookie: Box[HTTPCookie]): Box[AccountImpl] = {
    def unpackToken(token: String): Box[(String, String)] = token.split(":").toList match {
      case username :: token :: Nil => Full(username -> token)
      case _ => Empty
    }

    def tokenEq(token1: String, token2: String) =
      if (token1 == token2) Full(true)
      else Empty

    for (
      HTTPCookie(cookieName, Full(token), _, _, _, _, _, _) <- cookie ?~ "Cookie不存在";
      (username, _) <- unpackToken(token) ?~ "Cookie读取错误";
      record <- User.find("username" -> new String(Helpers.base64Decode(username))) ?~ "用户不存在";
      _ <- tokenEq(record.token.is, token) ?~ "Token不匹配"
    ) yield {
      new AccountImpl(record, true)
    }

  }

  /*  def apply(username: String, password: String): Box[AccountImpl] = {
      def passwordEq(record: User) =
      /*if (record.password.isMatch(password))*/ Full(true)
      /*else Failure("密码错误")*/

      for (
        record <- findName(username) ?~ "用户不存在";
        _ <- passwordEq(record)
      ) yield {
        new AccountImpl(record)
      }


    }*/
  def apply(username: String, password: String): Boolean = {
    findName(username, password)
  }

  def findName(userName: String, password: String): Boolean = {
    val have = User.findAll().filter(_.username == userName).head
    if (have.password == password) true
    else false
  }

  def httpCookie(accountId: String): Box[HTTPCookie] = {
    find(accountId).map(_.httpCookie)
  }

  /**
   * 对于find 方法，可缓存
   */
  def find(accountId: String): Box[AccountImpl] = User.find(accountId).map(new AccountImpl(_))

  def findByUsername(username: String): Box[AccountImpl] = User.find("username" -> username).map(new AccountImpl(_))

  def findAll: List[AccountImpl] = User.findAll.map(new AccountImpl(_))

  // 基于casbah的原子操作，避免每次都聚会整个文档
  def addUnreadInfomationId(id: String*) {
  }

  def removeUnreadInfomationId(id: String*) {
  }
}

import org.apache.commons.codec.digest.DigestUtils

case class Account(id: Long, username: String, password: String, status: Int, descn: String)

class AccountImpl(record: User, var remember: Boolean = false) {
  def httpCookie: HTTPCookie = {
    val maxAge = 60 * 60 * 24 * 14 // 保存两周

    // 更新token。将value值做为token保存下来，用户下次使用自动登陆功能时用以进行验证
    // TODO 应判断maxAge过期时间来决定是否需要更新db中的token值
    if (record.token.is == "") {
      val value = Helpers.base64Encode(record.username.is.getBytes) + ":" + DigestUtils.shaHex(new java.util.Date().toString)
      record.token(value).save
    }

    new HTTPCookie(Account.cookieName, Full(record.token.is), Empty, Full("/"), Full(maxAge), Empty, Empty)
  }

  @throws(classOf[IllegalArgumentException])
  def save = {
    /*if (User.count("username" -> username) > 0)
      throw new IllegalArgumentException("用户名: %s 已存在" format username)*/

    record.save
    this
  }

  def immutable = {
    Account(id, username, password, status, descn)
  }

  val id: Long = record.id.is

  def username: String = record.username.is

  def password: String = record.password.is

  def status: Int = record.status.is

  def descn: String = record.descn.is


  def setUsername(u: String) = {
    record.username(u)
    this
  }

  def setPassword(p: String) = {
    record.password(p)
    this
  }


  def setStatus(s: Int) = {
    record.status(s)
    this
  }

  def setDescn(d: String) = {
    record.descn(d)
    this
  }

  override def toString() = "[id: %s, username: %s]" format(id, username)
}
