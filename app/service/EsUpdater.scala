package service

import actors.MasterActor
import actors.MasterActor.{EvenNum, OddNum, ResultNumCase}
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
    for {
      i <- 0 to 10
      msg <- checkOddOrEven(i)
    } yield {
      updater ! msg
      Thread.sleep(5000)
    }
  }

  private[this] def checkOddOrEven(i: Int): Seq[ResultNumCase] = {
    // forの中はListを返さないといけないので、Seqで囲んでいる
    if (i % 2 == 0) Seq(EvenNum(i, "偶数です。")) else Seq(OddNum(i, "奇数です。"))
  }
}


