package crawler.lift.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with KeyedMetaMapper[Long, User] {
  override def dbTableName = "user"

  /*  override def loginXhtml = {
      <lift:surround with="default" at="leftContent">
        <lift:UserSnippet.generateXml/>
      </lift:surround>
    }

    override def signupXhtml(user: User) = {
      <lift:surround wih="default" at="content">

      </lift:surround>
    }*/
}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User extends KeyedMapper[Long, User] {

  def getSingleton = User

  def primaryKeyField = id

  object id extends MappedLongIndex(this)

  object token extends MappedString(this, 100)

  object username extends MappedString(this, 50)

  object password extends MappedString(this, 50)

  object status extends MappedInt(this)

  object descn extends MappedString(this, 200)

}
