package com.agoda.hotels.service.impl

import com.agoda.hotels.auth.{RateLimitEnum, ApiKey}
import org.scalatest.FunSuite

/**
  * Created by maliniak on 3/27/17.
  */
class ApiKeyStoreInMemoryImplTest extends FunSuite {

  def fixture =
    new {
      val store = new ApiKeyStoreInMemoryImpl
    }

  test("test get and put") {
    val f = fixture
    f.store.put(new ApiKey("key1", RateLimitEnum.Low))
    f.store.put(new ApiKey("key2", RateLimitEnum.High))

    assert(f.store.get("key1").isDefined)
    assert(f.store.get("key1").get.key === "key1")
    assert(f.store.get("key1").get.rate === RateLimitEnum.Low)

    assert(f.store.get("key2").isDefined)
    assert(f.store.get("key2").get.key === "key2")
    assert(f.store.get("key2").get.rate === RateLimitEnum.High)

    assert(f.store.get("non-existent-key") === None)
  }
}
