package com.agoda.hotels.auth

import com.agoda.hotels.auth.RateLimitEnum.RateLimitEnum

/**
  * API key with allowed requests rate
  */
class ApiKey (val key: String,
              val rate: RateLimitEnum)
