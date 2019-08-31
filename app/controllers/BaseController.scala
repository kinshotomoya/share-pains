package controllers

import play.api.mvc.Controller

import scala.concurrent.Future

// controllerでの処理を一元管理する
trait BaseController extends Controller
