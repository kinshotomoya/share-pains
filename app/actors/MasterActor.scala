package actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.FromConfig
import play.api.Logger


// NOTE: actorを作成するときは、コンパニオンオブジェクトの中で生成するのが推奨されている
// TODO: メモリリーク関連の理由だが、よくわからないので、再度調べる
// https://akka-ja-2411-translated.netlify.com/scala/actors.html

// あと、このactorが受け取るmessageの肩をコンパニオンオブジェクトの中に宣言していると、
// どんなmessageを受け取るのかわかりやすいので、推奨

object MasterActor {

  def props: Props = Props(new MasterActor)
}

class MasterActor extends Actor with ActorLogging{

  val logger = Logger(this.getClass)

  // ルーター・ルーティ（worker actor）を作成
  // 設定は、application.confに記載している
  // 負荷分散するために、ルーティを作っている
  val worker = context.actorOf(FromConfig.props(WorkerActor.props), "WorkerActor")

  override def receive: Receive = {
    case msg:String => {
//      print("------------------------")
//      print(msg)
//      print("------------------------")
      // worker actorにmessageを送信！
      worker ! msg
    }
  }
}
