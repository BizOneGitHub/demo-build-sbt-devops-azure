package com.bizone

// the code for our real/live LoginService
class RealLoginService extends LoginService {
  def login(userName: String, password: String): Boolean = {
    if (userName.equals("root") && password.equals("admin123")) {
      return true
    } else return false
  }

  override def loginByUser(name: String, password: String): Option[User] = {
    return Some(User(name, password))
  }
}
