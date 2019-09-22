package actors

import akka.actor.{Actor, Props}

object WorkerActor {
  def props: Props = Props(new WorkerActor)

  sealed trait NewResultCase
  case class NewOddNum(num: Int, message: String) extends NewResultCase
  case class NewEvenNum(num: Int, message: String) extends NewResultCase
}

class WorkerActor extends Actor{

  override def receive: Receive = {
    case msg: WorkerActor.NewResultCase=> {
      print("--------------------")
      print(msg)
      print("--------------------")
    }
  }
}
