package com.bizone

class FaceBook extends TemplateService {
  override def execution(in: Any, out: Any){
    println(s"execution template $in - $out")
  }
}
