//package crawler.lift.service
//
//import java.util.Date
//
//import scala.xml.{NodeSeq, Text}
//import akka.actor.{Actor, ActorSystem, Props, ActorRef, Cancellable}
//import akka.actor.Terminated
//
//import net.liftweb.common.{Box, Empty, Full, Failure}
//import net.liftweb.actor.LiftActor
//
//case class RefreshOnlineStatus(liftActors: List[LiftActor])
//
//case class OnlineStatus(accountIds: Set[String])
//
//case class AccountLogin(accountId: String)
//
//case class AccountLogout(accountId: String)
//
//case class SubscribeOnlineStatus(liftActor: LiftActor, accountId: Box[String])
//
//case class UnsubscribeOnlineStatus(liftActor: LiftActor, accountId: Box[String])
//
//case class CometStatus(liftActor: LiftActor, accountId: Box[String])
//
//object ContextSystem {
//  @volatile
//  var onlineAccountIds: Set[String] = Set()
//
//  object s {
//    lazy val
//    context = System.system.actorOf(Props[ContextDispatcher], "context-dispatcher")
//  }
//
//  def shutdown() {
//    s.context ! Terminated
//  }
//
//  class ContextDispatcher extends Actor {
//    private var liftListeners = Set[LiftActor]()
//    private var scheduleCancel: Option[Cancellable] = None
//
//    override def preStart() {
//      // TODO 是否需要每半分钟强制刷新
//      scheduleCancel = Full(System.system.scheduler.schedule(30 seconds, 30 seconds) {
//        self ! RefreshOnlineStatus(Nil)
//      })
//    }
//
//    override def postStop() {
//      for (s <- scheduleCancel if !s.isCancelled) {
//        s.cancel()
//      }
//      scheduleCancel = None
//    }
//
//    def receive = {
//      case RefreshOnlineStatus(Nil) =>
//        liftListeners = for (listener <- liftListeners if listener ne null) yield {
//          listener ! OnlineStatus(onlineAccountIds)
//          listener
//        }
//
//        if (IMSystem.isRunning) {
//          // TODO 改IMSystem订阅self 的消息 !
//          IMSystem.main ! OnlineStatus(onlineAccountIds)
//        }
//
//      case RefreshOnlineStatus(liftActors) =>
//        liftActors.foreach(_ ! OnlineStatus(onlineAccountIds))
//        liftListeners ++= liftActors
//
//        if (IMSystem.isRunning) {
//          IMSystem.main ! OnlineStatus(onlineAccountIds)
//        }
//
//      case a@AccountLogin(accountId) =>
//        onlineAccountIds += accountId
//        self ! RefreshOnlineStatus(Nil)
//
//        liftListeners = for (listener <- liftListeners if listener ne null) yield {
//          listener ! a
//          listener
//        }
//
//      case a@AccountLogout(accountId) =>
//        onlineAccountIds -= accountId
//        self ! RefreshOnlineStatus(Nil)
//
//        liftListeners = for (listener <- liftListeners if listener ne null) yield {
//          listener ! a
//          listener
//        }
//
//      case a@SubscribeOnlineStatus(liftActor, accountIdBox) =>
//        liftListeners += liftActor
//        for (accountId <- accountIdBox if !onlineAccountIds.contains(accountId)) {
//          onlineAccountIds += accountId
//          self ! RefreshOnlineStatus(liftActor :: Nil)
//        }
//
//      case a@UnsubscribeOnlineStatus(liftActor, accountIdBox) =>
//        liftListeners -= liftActor
//        for (accountId <- accountIdBox if onlineAccountIds.contains(accountId)) {
//          onlineAccountIds -= accountId
//          self ! RefreshOnlineStatus(liftActor :: Nil)
//        }
//
//      case CometStatus(liftActor, Full(accountId)) =>
//        onlineAccountIds += accountId
//        self ! RefreshOnlineStatus(liftActor :: Nil)
//
//    }
//  }
//
//}
