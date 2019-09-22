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
    // きちんと、master -> router -> routee1, routee2　みたいに負荷分散されているか確認
    for(i <- 0 to 10) {
      updater ! "this is a message!!!!!!" + i
      Thread.sleep(1000)
    }
  }
}
