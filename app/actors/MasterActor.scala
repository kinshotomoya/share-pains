package actors

import actors.MasterActor.{EvenNum, OddNum}
import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.FromConfig
import play.api.Logger


// NOTE: actorを作成するときは、コンパニオンオブジェクトの中で生成するのが推奨されている
// TODO: メモリリーク関連の理由だが、よくわからないので、再度調べる
// https://akka-ja-2411-translated.netlify.com/scala/actors.html

object MasterActor {

  def props: Props = Props(new MasterActor)

  // あと、このactorが受け取るmessageの肩をコンパニオンオブジェクトの中に宣言していると、
  // どんなmessageを受け取るのかわかりやすいので、推奨
  sealed trait ResultNumCase
  case class OddNum(number: Int, message: String) extends ResultNumCase
  case class EvenNum(number: Int, message: String) extends ResultNumCase
}

class MasterActor extends Actor with ActorLogging{

  val logger = Logger(this.getClass)

  // ルーター・ルーティ（worker actor）を作成
  // 設定は、application.confに記載している
  // 負荷分散するために、ルーティを作っている
  val worker = context.actorOf(FromConfig.props(WorkerActor.props), "WorkerActor")

  override def receive: Receive = {
    case OddNum(num, message) => {
      // worker actorにmessageを送信！
      worker ! WorkerActor.NewOddNum(num*1000, message)
    }
    case EvenNum(num, message) => {
      worker ! WorkerActor.NewEvenNum(num, message)
    }
    case _ => {
      // ここがくるハズがない
      print("error")
    }
  }
}
