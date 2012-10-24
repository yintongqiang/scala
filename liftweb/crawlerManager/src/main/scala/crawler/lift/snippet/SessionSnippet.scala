package crawler.lift.snippet

import scala.xml.NodeSeq

import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.http.js._


import crawler.lift.service.SessionManager
import crawler.lift.service.SessionManager.theAccountId
import crawler.lift.model.Account
import crawler.lift.web.Y
import provider.HTTPCookie

class SessionSnippet {
  def navCollapse: NodeSeq = theAccountId.is match {
    case Full(accountId) =>
      val account = Account.find(accountId).open_!

      S.appendJs(JsCmds.Run("$('#hidden_reflush_context').click();"))
      S.addCookie(HTTPCookie("1","2"))
      val cssSel =
        "@username" #> account.username &
          ".dropdown-menu" #> <div>
            <li>#其它功能#</li>
            <li class="divider"></li>
            <li>
              <a href="/session/logout">退出</a>
            </li>
          </div>
      cssSel(_navCollapse) ++ Y.template("navbar-comet").open_!

    case _ =>
      val cssSel = "@username" #> "登陆/注册" &
        ".dropdown-menu" #> <div>
          <li>
            <a href="/session/login">登陆</a>
          </li>
          <li>
            <a href="/session/register">注册</a>
          </li>
          <li>
            <a href="/session/register2" title="纯Aja  x注册方案">注册2</a>
          </li>
        </div> &
        ".dropdown-toggle [class]" #> "btn btn-info dropdown-toggle"
      cssSel(_navCollapse)

  }

  private val _navCollapse = Y.template("navbar-session").open_!
}
