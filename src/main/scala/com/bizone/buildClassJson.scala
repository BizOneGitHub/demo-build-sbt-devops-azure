package com.bizone

import java.io.PrintWriter

object buildClassJson {
  val yourPath = "src\\main\\scala\\com\\bizone\\logic2.scala"
  def main(args: Array[String]) {
    new PrintWriter(yourPath) {
      write(
        s"package com.bizone \n\n class Logic2 { \n var a = 1 \n var b = 5 \nval joinResult = a+b \n}"
      )
      close()
    }
  }
}
