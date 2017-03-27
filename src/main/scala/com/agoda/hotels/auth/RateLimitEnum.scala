package com.agoda.hotels.auth

/**
  * Predefined types of requests rates, to be associated with api keys.
  */
object RateLimitEnum extends Enumeration {
  type RateLimitEnum = Value
  val Low, Mid, High = Value
}
