package com.bizone

import com.bizone.{AuthenService, Logic, LoginService, User}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class AuthedServiceTests extends FunSuite with BeforeAndAfter with MockitoSugar {

  var service: LoginService = _
  var logic: Logic = _
  var authenService: AuthenService = _
  before {
    service = mock[LoginService]
    logic = mock[Logic]
    authenService = new AuthenService(service, logic)
  }

  test("test loginServiceReturnBoolean") {

    // (2) setup: when someone logs in as "johndoe", the service should work;
    //            when they try to log in as "joehacker", it should fail.
    when(service.login("johndoe", "secret")).thenReturn(true)
    when(service.login("joehacker", "secret")).thenReturn(false)

    // (3) access the service
    val johndoe = authenService.auth("johndoe", "secret")
    val joehacker = authenService.auth("joehacker", "secret")

    // (4) verify the results
    assertEquals("OK", johndoe)
    assertEquals("NG", joehacker)

  }

  test("test loginServiceReturnUser") {

    // (2) setup: when someone logs in as "johndoe", the service should work;
    //            when they try to log in as "joehacker", it should fail.
    when(service.loginByUser(null, "secret")).thenReturn(Some(User("johndoe")))
    when(service.loginByUser("joehacker", "secret")).thenReturn(None)

    // (3) access the service
    val johndoe = authenService.authByUser(null, "secret")
    val joehacker = authenService.authByUser("joehacker", "secret")

    // (4) verify the results
    assertEquals(johndoe.get, User("johndoe"))
    assertEquals(joehacker, None)

  }

  test("test checkNumberFail") {

    when(logic.total(1, 3)).thenReturn(4)
    // (3) access the service
    val checkNumberFalse = authenService.checkNumber(1, 3)
    // (4) verify the results
    assertFalse(checkNumberFalse)
  }

  test("test checkNumberOK") {

    when(logic.total(1, 5)).thenReturn(11)
    val checkNumberTrue = authenService.checkNumber(1, 5)
    assertTrue(checkNumberTrue)
  }

  test("test Throws an exception") {
//    when(logic.div(1, 2)).thenReturn()
    doThrow(new IllegalArgumentException).when(logic).div(1, 2)
//    assertThrows[ArithmeticException] {
//      val checkNumberFalse = authenService.checkDiv(1, 4, 2)
//    }
    assertThrows[IllegalArgumentException] {
      val checkNumberFalse = authenService.checkDiv(1, 2, 2)
    }
  }

  test("test doNothong") {
    // Sometimes used in void return methods or method that does not have side effects, or are not related to the unit testing you are doing
    doNothing.when(logic).show(1)
    authenService.checkShow(1)
  }

  test("test verify exception error_code_011") {
    // Sometimes used in void return methods or method that does not have side effects, or are not related to the unit testing you are doing
    doThrow(new Exception).when(logic).div(1, 2)
    val checkNumberFalse = authenService.checkThrowDiv(1, 2)
    assertEquals("error_code_011", checkNumberFalse)
  }

}
