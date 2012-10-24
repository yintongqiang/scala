package crawler.lift.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.http.js.JsCmds._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb._
import http.{S, SHtml}
import net.liftweb.mapper._
import crawler.lift.model.Person

/**
 * Created with IntelliJ IDEA.
 * User: readman
 * Date: 4/20/12
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */

class Ajaxform {
  def render = {
    var first_name = ""
    var last_name = ""
    def process() = {
      Person.create.firstname(first_name).lastname(last_name).save
      val personList = Person.findAllByInsecureSql("select * from person where firstname=':firstname' and lastname=':lastname'".replace(":firstname", "12").replace(":lastname", "33"), new IHaveValidatedThisSQL("aa", "bb"))
      val pageSize = 5
      val offset = pageSize * 1
      val offsetlist = Person.findAll((OrderBySql(" id asc", new IHaveValidatedThisSQL("aa", "bb"))), StartAt(offset), MaxRows(5))
      listing
    }
    def listing = Person.findAll(OrderBy(Person.id, Descending)).map(a => S.notice("id= " + a.id + " firstname= " + a.firstname + " lastname= " + a.lastname))
    "#first_name" #> SHtml.text(first_name, first_name = _) &
      "#last_name" #> SHtml.text(last_name, last_name = _) &
      "#hidden" #> SHtml.hidden(process)
  }
}
