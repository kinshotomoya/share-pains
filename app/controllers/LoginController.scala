package controllers

import javax.inject.Singleton
import play.api.mvc.{Action, Controller}

@Singleton
class LoginController extends Controller{

  def renderLoginPage = Action { implicit req =>
    // sessionがあったら、index ページに飛ばす
    // なければ、logninページに飛ばす
    // Optionで返って来るのでmatchで存在するかどうかを確認する
    req.session.get("userInfo") match {
      case Some(session) => Redirect(routes.HomeController.index())
      case None => Ok(views.html.login())
    }
  }
}
