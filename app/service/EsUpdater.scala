package service

import actors.MasterActor
import akka.actor.{ActorRef, ActorSystem}
import javax.inject.Inject

class EsUpdater @Inject()(
                           actorSystem: ActorSystem
                         ) {
  // akka systemを作成する
  // actorにes更新の処理をさせるmessageを投げる
  def esUpdate = {
    val updater: ActorRef = actorSystem.actorOf(MasterActor.props, "MasterActor")
    // master actorにmessageを送る
    updater ! "this is a message!!!!!!"
  }
}
