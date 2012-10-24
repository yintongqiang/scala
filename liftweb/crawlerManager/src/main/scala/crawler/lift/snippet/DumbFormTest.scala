package crawler.lift.snippet

import net.liftweb.util.PassThru
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.util.Helpers._

/**
 * Created with IntelliJ IDEA.
 * User: yintongqiang
 * Date: 12-10-24
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
class DumbFormTest {
  val inputParam = for {r <- S.request if r.post_? // restricting to POST requests
                        v <- S.param("it")
  } yield v

  def render = inputParam match {
    case Full(x) => println("Input is: " + x)
    "#result *" #> x
    case _ => println("No input present! Rendering input form HTML")
    PassThru
  }
}