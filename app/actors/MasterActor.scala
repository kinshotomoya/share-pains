package actors

import akka.actor.{Actor, ActorLogging, Props}


// NOTE: actorを作成するときは、コンパニオンオブジェクトの中で生成するのが推奨されている
// TODO: メモリリーク関連の理由だが、よくわからないので、再度調べる
// https://akka-ja-2411-translated.netlify.com/scala/actors.html

// あと、このactorが受け取るmessageの肩をコンパニオンオブジェクトの中に宣言していると、
// どんなmessageを受け取るのかわかりやすいので、推奨

object MasterActor {

  def props: Props = Props(new MasterActor)
}

class MasterActor extends Actor with ActorLogging{

  override def receive: Receive = {
    case msg:String => {
      print("------------------------")
      print(msg)
      print("------------------------")
    }
  }
}
