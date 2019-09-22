package actors

import akka.actor.{Actor, Props}

object WorkerActor {
  def props: Props = Props(new WorkerActor)
}

class WorkerActor extends Actor{

  override def receive: Receive = {
    case msg: String => {
      print("worker--------------------")
      print(msg)
      print("worker--------------------")
    }
  }
}
