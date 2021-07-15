package com.bizone

import java.util

// the code for our real/live LoginService
class AuthenService(service: LoginService, logic: Logic) {

  def this() = this(new RealLoginService, new Logic)

  def auth(userName: String, password: String): String = {
    val isLogin = service.login(userName, password)
    var result = _
    if(isLogin){
      result = "OK"
    }else{
      result = "NG"
    }
    return result
  }

  def authByUser(userName: String, password: String): Option[User] = {
    return service.loginByUser(userName, password)
  }

  def checkNumber(a: Int, b: Int): Boolean = {
    return logic.total(a, b) > 10
  }

  def checkDiv(a: Int, b: Int, c: Int): Int = {
    return logic.total(a, b) / c
  }

  def checkShow(a: Int) {
    logic.show(a)
  }

  def checkThrowDiv(a: Int, b: Int): Float = {
    return logic.div(a, b)
  }
}
