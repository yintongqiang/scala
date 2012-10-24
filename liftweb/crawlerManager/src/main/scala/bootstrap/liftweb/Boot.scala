package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import auth.{HttpBasicAuthentication, AuthRole}
import net.liftweb.sitemap._
import crawler.lift.model.{Person, User, Resc}
import Helpers._
import net.liftweb.mapper._
import _root_.java.sql.{Connection, DriverManager}
import net.liftweb.common.Full
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.common.Full
import net.liftweb.sitemap.Loc.{Unless, Link}
import auth._
import net.liftweb.oauth.{MOAuthNonce}


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    Schemifier.schemify(true, Schemifier.infoF _, User, Resc, MOAuthNonce, Person)
    SiteMap.enforceUniqueLinks = false
    // where to search snippet
    LiftRules.addToPackages("crawler.lift")
    val roles = AuthRole("Admin",
      AuthRole("Site-Admin"),
      AuthRole("User-Admin",
        AuthRole("Romania-Admin"),
        AuthRole("play2"),
        AuthRole("UK-Admin")
      )
    )
    /*   LiftRules.httpAuthProtectedResource.append() {
         case (ParsePath("user" :: _, _, _, _)) => roles.getRoleByName("Admin")
       }*/
    import crawler.lift.service.SessionManager._
    def sitemap() = SiteMap(
      Menu("Home") / "index" >> accountAccess,
      Menu("login") / "index" submenus(
        Menu("user") / "user/viewUsers",
        Menu("single", "单页") / "single",
        Menu("session-logout", "退出") / "session/login") /*>> accountLogout*/
      /* Menu("UserList") / "user/viewUsers")*/)
    LiftRules.setSiteMapFunc(sitemap)
    LiftRules.authentication = HttpBasicAuthentication("crawler.lift") {
      case (username, password, req) =>
        println("John is authenticated !")
        if (username == "play2" && password == "123456")
          userRoles(AuthRole("User-Admin"))
        true
    }
    LiftRules.statelessRewrite.append {
      case RewriteRequest(ParsePath(
       "category" :: cid :: "product" :: pid :: Nil, "", true, false),
      GetRequest, http) =>
      RewriteResponse("product" :: Nil,
        Map("cid" -> cid, "pid" -> pid))
    }
    // Build SiteMap


    /*
     * Show the spinny image when an Ajax call starts
     */
    /*
        LiftRules.ajaxStart =
    */
    /*   Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
     LiftRules.ajaxEnd =
       Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
     LiftRules.early.append(makeUtf8)
     LiftRules.loggedInTest = Full(() => User.loggedIn_?)
     S.addAround(DB.buildLoanWrapper)*/
    LiftRules.early.append(makeUtf8)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

  object CouponRaw extends ConnectionIdentifier {
    def jndiName = "coupon_raw"
  }

  object TuanCmc extends ConnectionIdentifier {
    def jndiName = "tuan-cmc"
  }

  object DBVendor extends ConnectionManager {
    Class.forName("com.mysql.jdbc.Driver")

    def newConnection(name: ConnectionIdentifier): Box[Connection] = {
      try {

        name match {
          case CouponRaw => Full(DriverManager.getConnection("jdbc:mysql://localhost:3306/coupon_raw?user=root&password=yintongqiang"))
          case TuanCmc => Full(DriverManager.getConnection("jdbc:mysql://localhost:3306/tuan-cmc?user=root&password=yintongqiang"))
          case DefaultConnectionIdentifier => Full(DriverManager.getConnection("jdbc:mysql://localhost:3306/tuan-cmc?user=root&password=yintongqiang"))
        }
      } catch {
        case e: Exception => e.printStackTrace; Empty
      }
    }

    def releaseConnection(conn: Connection) {
      conn.close
    }
  }

}
