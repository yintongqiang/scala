package crawler.lift.model

import net.liftweb.mapper._

/**
 * Created with IntelliJ IDEA.
 * User: yintongqiang
 * Date: 12-10-11
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */
object Resc extends Resc with KeyedMetaMapper[Long, Resc] {
  override def dbTableName = "resc"
}

class Resc extends KeyedMapper[Long, Resc] {

  def getSingleton = Resc

  def primaryKeyField = id

  // what's the "meta" server
  object id extends MappedLongIndex(this)

  object name extends MappedString(this, 50)

  object res_type extends MappedString(this, 50)

  object res_string extends MappedString(this, 200)

  object priority extends MappedInt(this)

  object descn extends MappedString(this, 200)

  object father_id extends MappedInt(this)

  object show_url extends MappedString(this, 64)

  object show_menu extends MappedInt(this)

  object is_last_node extends MappedInt(this)

  object order_node extends MappedInt(this)

}