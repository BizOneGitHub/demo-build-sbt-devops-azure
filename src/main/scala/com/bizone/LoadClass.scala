package com.bizone

object LoadClass {
  def apply(className: String) = {
    Class.forName(className).newInstance()
  }
}
