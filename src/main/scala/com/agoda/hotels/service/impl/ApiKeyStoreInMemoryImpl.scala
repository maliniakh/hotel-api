package com.agoda.hotels.service.impl

import javax.annotation.PostConstruct

import com.agoda.hotels.auth.{RateLimitEnum, ApiKey}
import com.agoda.hotels.service.ApiKeyStore
import org.springframework.stereotype.Service

import scala.collection.mutable

@Service
class ApiKeyStoreInMemoryImpl extends ApiKeyStore {

  val keyMap: mutable.Map[String, ApiKey] = mutable.Map()

  override def get(key: String): Option[ApiKey] = {
    keyMap.get(key)
  }

  override def put(apiKey: ApiKey) = {
    keyMap += (apiKey.key -> apiKey)
  }

  // arguably such test setup could be separated from the code and
  // could be done for example in a configuration file
  @PostConstruct
  def init() = {
    put(new ApiKey("lowkey", RateLimitEnum.Low))
    put(new ApiKey("midkey", RateLimitEnum.Mid))
    put(new ApiKey("highkey", RateLimitEnum.High))
  }
}
