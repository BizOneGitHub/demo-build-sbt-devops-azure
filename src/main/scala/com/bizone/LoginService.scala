package com.bizone
// a LoginService must have a 'login' method
trait LoginService {
  def login(name: String, password: String): Boolean
  def loginByUser(name: String, password: String): Option[User]
}
