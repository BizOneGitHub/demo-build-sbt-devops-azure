package com.bizone

class InstanceOfLogic(clsName: String) {
  val instance = LoadClass(clsName)

  def getValue(fieldName:String): Any = {
      val bus = instance.getClass()
      val field = bus.getDeclaredField(fieldName)
          field.setAccessible(true)
      return field.get(instance)
  }
}
