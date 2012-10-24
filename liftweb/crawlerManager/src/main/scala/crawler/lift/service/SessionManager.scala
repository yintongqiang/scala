package crawler.lift.service

import net.liftweb.common.{Box, Empty, Full, Failure}
import net.liftweb.http._
import net.liftweb.sitemap.Loc.TestAccess
import crawler.lift.model.Account


object SessionManager {

  object theAccountId extends SessionVar[Box[String]](Empty)

  object testGoods extends SessionVar[List[String]](List(""))

  def accountAccess = TestAccess {
    () =>
      theAccountId.is match {
        case Full(theAccount) => Empty
        case _ =>
          Account(S.findCookie(Account.cookieName)) match {
            case Full(account) =>
              saveSessionAndCookie(account.id.toString)
              Empty
            case Failure(msg, _, _) =>
              // logger.error("用户自动登陆失败: " + msg)
              S.deleteCookie(Account.cookieName)
              Full(RedirectResponse("/session%2Flogin"))
            case Empty =>
              Full(RedirectResponse("/session%2Flogin"))
          }
      }

  }

  def accountLogout = TestAccess {
    () =>
    /*    if (theAccountId.is.isDefined) {
          ContextSystem.s.context ! AccountLogout(theAccountId.is.open_!)
          theAccountId.remove()
        }*/
      theAccountId.remove()
      S.deleteCookie(Account.cookieName)
      Full(RedirectResponse("/session%2Flogin"))
  }

  def saveSessionAndCookie(accountId: String, beReset: Boolean = false) {
    /*
        S.session.foreach(_.addSessionCleanup(_ => {
          ContextSystem.s.context ! AccountLogout(accountId)
        }))
    */

    theAccountId(Full(accountId))

    /*    ContextSystem.s.context ! AccountLogin(accountId)
        logger.debug("ContextSystem.main ! AccountLogin(accountId)")*/

    if (beReset) {
      S.addCookie(Account.httpCookie(accountId).open_!) // 此处可安全打开
    } else {
      S.deleteCookie(Account.cookieName)
    }

  }
}
