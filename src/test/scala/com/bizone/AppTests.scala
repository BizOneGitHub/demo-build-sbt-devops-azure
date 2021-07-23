package com.bizone

import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class AppTests extends FunSuite with BeforeAndAfter with MockitoSugar {


  test("test loadConfig") {
    App.main(null)
  }
}
