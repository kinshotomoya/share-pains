package controllers

import play.api.mvc.Controller

import scala.concurrent.Future

trait BaseController extends Controller with FutureOps

trait FutureOps {
  // 引数のresultは、変換の対象である
  implicit class FutureOps[T](result: T) {
    // futureメソッドを呼び出すと、Future型に変換する
    // xxx.futureとすることで、xxxが引数になり、xxxの型を持ったFuture[]が返される
    def future: Future[T] = {
      Future.successful(result)
    }
  }
}
