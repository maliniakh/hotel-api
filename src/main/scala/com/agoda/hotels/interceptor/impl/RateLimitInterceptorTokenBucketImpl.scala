package com.agoda.hotels.interceptor.impl

import java.time.Clock
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import com.agoda.hotels.auth.{RateLimitEnum, ApiKey}
import com.agoda.hotels.controller.exception.{NotAuthorizedException, RateLimitExceededException}
import com.agoda.hotels.interceptor.RateLimitInterceptor
import com.agoda.hotels.service.ApiKeyStore
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Component

import scala.collection.mutable

/**
  * Interceptor implementing simple token-bucket-like algorithm for limitnig requests rate.
  * TODO: needs synchronization
  */
@Component
class RateLimitInterceptorTokenBucketImpl(@Value("${hotels.rates.low}") val rateLow: Int,
                                          @Value("${hotels.rates.mid}") val rateMid: Int,
                                          @Value("${hotels.rates.high}") val rateHigh: Int,
                                          @Autowired val apiKeyStore: ApiKeyStore,
                                          @Autowired private[impl] val clock: Clock) extends RateLimitInterceptor {
  /**
    * associates api key with its token bucket params
    */
  private val apiKey2paramsMap = mutable.Map[String, TokenBucketParams]()

  override def preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: AnyRef): Boolean = {
    val key: String = req.getHeader("Authorization")
    if(key == null) {
      // arguably validating apiKey could be done in a separate interceptor / filter
      throw new NotAuthorizedException
    }

    val apiKey: ApiKey = apiKeyStore.get(key) match {
        case Some(x) => x
        case None => throw new NotAuthorizedException
    }

    val now = clock.millis()
    // get already exisiting token bucket params or create them and put them into map
    val tbParams: TokenBucketParams = apiKey2paramsMap.get(key) match {
      case Some(x) => x
      case None => {
        val tbParams: TokenBucketParams = new TokenBucketParams(apiKey.rate match {
          case RateLimitEnum.Low => rateLow
          case RateLimitEnum.Mid => rateMid
          case RateLimitEnum.High => rateHigh
        }, now)
        apiKey2paramsMap += (key -> tbParams)
        tbParams
      }
    }

    val timeDiff = now - tbParams.lastCheck
    tbParams.lastCheck = now
    tbParams.allowance += timeDiff * (tbParams.msgsPerSec / 1000) // 1000ms = 1s
    if (tbParams.allowance > tbParams.msgsPerSec) {
      tbParams.allowance = tbParams.msgsPerSec // throttle
    }
    if (tbParams.allowance < 1.0) {
      tbParams.allowance -= 1.0
      throw new RateLimitExceededException
    }

    tbParams.allowance -= 1.0
    true
  }
}

class TokenBucketParams(var msgsPerSec: Double,
                        var lastCheck: Long) {
  var allowance: Double = msgsPerSec
}


