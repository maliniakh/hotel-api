package com.agoda.hotels.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
  * It needs to be an interceptor in order to handle exceptions and their response codes appropriately.
  */
trait RateLimitInterceptor extends HandlerInterceptorAdapter