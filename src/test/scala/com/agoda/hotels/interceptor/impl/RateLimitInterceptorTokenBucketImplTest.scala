package com.agoda.hotels.interceptor.impl

import java.time.Clock
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import com.agoda.hotels.auth.{RateLimitEnum, ApiKey}
import com.agoda.hotels.controller.exception.{NotAuthorizedException, RateLimitExceededException}
import com.agoda.hotels.service.ApiKeyStore
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._

import org.scalatest.FunSuite

class RateLimitInterceptorTokenBucketImplTest extends FunSuite with MockFactory {

  def fixture =
    new {
      val apiKeyStore = stub[ApiKeyStore]
      apiKeyStore.get _ when "lowkey" returning Some(new ApiKey("lowkey", RateLimitEnum.Low))
      apiKeyStore.get _ when "midkey" returning Some(new ApiKey("lowkey", RateLimitEnum.Mid))
      apiKeyStore.get _ when "wrongkey" returning None
      var clock = stub[Clock]
      val reqLowRate = stub[HttpServletRequest]
      val reqMidRate = stub[HttpServletRequest]
      val wrongKeyReq = stub[HttpServletRequest]
      val noKeyReq = stub[HttpServletRequest]
      reqLowRate.getHeader _ when "Authorization" returning "lowkey"
      reqMidRate.getHeader _ when "Authorization" returning "midkey"
      wrongKeyReq.getHeader _ when "Authorization" returning "wrongkey"
      noKeyReq.getHeader _ when "Authorization" returning null

      val interceptor = new RateLimitInterceptorTokenBucketImpl(1, 3, 10, apiKeyStore, clock)
    }

  test("Should allow request within the limit") {
    val f = fixture
    f.clock.millis _ when() returning 0 noMoreThanOnce()
    f.clock.millis _ when() returning 1000 noMoreThanOnce()
    f.clock.millis _ when() returning 2000 noMoreThanOnce()

    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
  }

  test("Should deny requests exceeding the limit") {
    val f = fixture
    f.clock.millis _ when() returning 0 noMoreThanOnce()
    f.clock.millis _ when() returning 500 noMoreThanOnce()
    f.clock.millis _ when() returning 2100 noMoreThanOnce()

    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
    an [RateLimitExceededException] should be thrownBy {
      f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
    }
    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
  }

  test("Requests should be treated independently") {
    val f = fixture
    f.clock.millis _ when() returning 0 noMoreThanOnce()
    f.clock.millis _ when() returning 0 noMoreThanOnce()
    f.clock.millis _ when() returning 0 noMoreThanOnce()
    f.clock.millis _ when() returning 500 noMoreThanOnce()
    f.clock.millis _ when() returning 2100 noMoreThanOnce()

    f.interceptor.preHandle(f.reqMidRate, mock[HttpServletResponse], null)
    f.interceptor.preHandle(f.reqMidRate, mock[HttpServletResponse], null)
    f.interceptor.preHandle(f.reqMidRate, mock[HttpServletResponse], null)
    f.interceptor.preHandle(f.reqLowRate, mock[HttpServletResponse], null)
  }

  test("Should reject requests with no or wrong keys provided") {
    val f = fixture

    an [NotAuthorizedException] should be thrownBy {
      f.interceptor.preHandle(f.wrongKeyReq, mock[HttpServletResponse], null)
    }
    an [NotAuthorizedException] should be thrownBy {
      f.interceptor.preHandle(f.noKeyReq, mock[HttpServletResponse], null)
    }
  }
}
