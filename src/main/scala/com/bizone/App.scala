package com.bizone

import com.typesafe.config.ConfigFactory
//import org.apache.spark

/**
 * @author duc.nguyen
 */
object App  {


  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {
    var clsName = "com.bizone.Logic"
    val inst = new InstanceOfLogic(clsName)
    val in = inst.getValue("joinResult")

    clsName = "com.bizone.Logic2"
    val inst1 = new InstanceOfLogic(clsName)
    val out = inst1.getValue("joinResult")

    val tempFaceBook = new InstanceOfTemplate("com.bizone.FaceBook")
    tempFaceBook.execution(in, out)

    val config = ConfigFactory.load
    val env = config.getString("env")

    println(s"This package runs in $env")

  }

}