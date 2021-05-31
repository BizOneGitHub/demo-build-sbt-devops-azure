package com.bizone

class InstanceOfTemplate(clsName: String) {
  val instance = LoadClass(clsName)

  def execution(in: Any, out: Any){
      val bus = instance.asInstanceOf[TemplateService]
      bus.execution(in, out)
  }
}
