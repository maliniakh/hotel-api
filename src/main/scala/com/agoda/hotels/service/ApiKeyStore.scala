package com.agoda.hotels.service

import com.agoda.hotels.auth.ApiKey

trait ApiKeyStore {
  def get(key: String): Option[ApiKey]
  def put(apiKey: ApiKey)
}
