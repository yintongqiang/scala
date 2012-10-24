package crawler.lift.snippet

import scala.xml.{NodeSeq, Text}

import net.liftweb.common.{Box, Full, Empty, Failure}
import net.liftweb.http.{SHtml, SessionVar, RequestVar, S}
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.util.Helpers._

import crawler.lift.service.SessionManager.theAccountId
import crawler.lift.service.SessionManager._
import crawler.lift.model.Account
import net.liftweb.http.js.JsCmds.Alert
import crawler.lift.service.SessionManager

object LoginSnippet {

  private object username extends RequestVar[String]("")

  private object password extends RequestVar[String]("")

  def render(nodeSeq: NodeSeq): NodeSeq = {
    /*   if (theAccountId.is.isDefined) {
         S.warning("用户: %s 您已经登陆" format Account.find(theAccountId.is.open_!).open_!.username)
         S.redirectTo("/index")
       }
       var list = List[String]()
       list::="来"
       list::="abc"
       SessionManager.testGoods(list)*/
    val cssSel =
      "@username" #> SHtml.text(username.is, username(_)) &
        "@password" #> SHtml.password(password.is, password(_)) &
        "@login" #> SHtml.submit("登陆", () => {
          Account(username.is, password.is) match {
            case true =>
              saveSessionAndCookie(username.toString)
              S.redirectTo("/index")
            case false =>
              Alert("用户登陆失败")
            case _ =>
              S.error("用户登陆: 系统错误")
          }
        })

    cssSel(nodeSeq)
  }
}
