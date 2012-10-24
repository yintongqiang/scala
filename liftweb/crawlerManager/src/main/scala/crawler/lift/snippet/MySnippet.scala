package crawler.lift.snippet

import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._

/**
 * Created with IntelliJ IDEA.
 * User: yintongqiang
 * Date: 12-10-24
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
class MySnippet {
  def multi = {
    case class Item(id: String, name: String)
    val inventory = Item("a", "火爆了撒") :: Item("b", "走远了撒") :: Item("c", "只想说两个字") :: Nil
    val options: List[(String, String)] = inventory.map(i => (i.id -> i.name))
    val default = inventory.head.id :: Nil
    "#opts *" #> SHtml.multiSelect(options, default, xs => println("Selected: " + xs))
  }
}