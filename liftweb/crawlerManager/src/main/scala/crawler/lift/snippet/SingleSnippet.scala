package crawler.lift.snippet

import scala.xml.{NodeSeq, Text}

import net.liftweb.common._
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.jquery.{JqJsCmds}
import net.liftweb.json.JsonDSL._


import crawler.lift.model.{Account, User}
import crawler.lift.service.{EmailContent, MailService, SessionManager}
import crawler.lift.web.Y

class SingleSnippet {

  private object reqHref extends RequestVar[Box[String]](findReqHref)

  def render: CssSel = {
    //    S.appendJs(hrefHash("yj-a-message" -> "message", "yj-a-temp2" -> "temp2", "yj-a-edit" -> "edit", "yj-a-send_email" -> "send_email"))
    S.appendJs(ready)

    "#yj-sidebar *" #> sidebar &
      "#yj-container-main *" #> reqHref.is.dmap(index) {
        /*        case "message" =>
          message
        case "temp2" =>
          temp2
        case "edit" =>
          edit
        case "send_mail" =>
          sendEmail */
        case _ =>
          index
      }
  }

  def sidebar: NodeSeq = {
    def sidebarLi(id: String, text: String, locationHash: String, template: NodeSeq) =
      <div class="accordion-heading">
        <a class="accordion-toggle btn-info" data-toggle="collapse" data-parent="#accordion2">
          {Y.ajaxA(text, Full("#" + locationHash), () => {
          JsCmds.SetHtml("yj-container-main", template) &
            JE.JsRaw("window.location.hash='%s'" format locationHash).cmd
        }, "id" -> id)}
        </a>
      </div>
    Y.template("single/sidebar").map {
      nodeSeq =>
        val links = sidebarLi("yj-a-message", "用户列表", "href=message", message) ++
          sidebarLi("yj-a-temp2", "任务监控", "href=temp2", temp2)
        //          sidebarLi("yj-a-edit", "编辑", "href=edit", edit) ++
        //        sidebarLi("yj-a-send_email", "发送邮件", "href=send_email", sendEmail)

        (".nav *" #> links).apply(nodeSeq)
    } openOr Text("模板: single/sidebar 未找到")
  }

  private def index: NodeSeq = Y.template("single/index").map {
    nodeSeq =>
      nodeSeq
  } openOr Text("模板: single/index 未找到")

  private def message: NodeSeq = Y.template("single/message").map {
    nodeSeq =>
      nodeSeq
  } openOr Text("模板: single/message 未找到")

  private def temp2: NodeSeq = Y.template("single/temp2").map {
    nodeSeq =>
      nodeSeq
  } openOr Text("模板: single/temp2 未找到")


  /**
   * TODO 文件上传的Ajax功能还没实现
   */
  private object reqEmail extends RequestVar[EmailContent](EmailContent.create)

  private object reqUpload extends RequestVar[Box[FileParamHolder]](Empty)


  def delete(nodeSeq: NodeSeq): NodeSeq = {
    val cssSel = "" #> ""

    cssSel(nodeSeq)
  }

  private def form(account: User, isReg: Boolean = false): CssSel = {
    "@username" #> (if (isReg) account.username.toForm.open_! else Text(account.username.is)) &
      "@email" #> account.password.toForm.open_!

  }


  private def findRecord: Box[User] = {
    for (
      accountId <- SessionManager.theAccountId ?~ "session不存在";
      record <- User.find(accountId) ?~ "用户:%s".format("不存在")
    ) yield record
  }

  private def findReqHref: Box[String] = {
    Empty
  }

  private def ready = JsCmds.Run(
    """
    var hrefHash = window.location.hash.split('=')[1];
    $('#yj-a-' + hrefHash).click();
    """)

  /**
   * (id, hash)
   */
  private def hrefHash(idHashPair: (String, String)*) = JsCmds.Run(idHashPair.map(pair =>
    "if (hrefHash == '%s')  $('#%s').click(); ".format(pair._2, pair._1))
    .mkString("var hrefHash = window.location.hash.split('=')[1]; ", " else ", ""))

}
