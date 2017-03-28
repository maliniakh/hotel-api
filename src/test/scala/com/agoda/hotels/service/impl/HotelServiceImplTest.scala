package com.agoda.hotels.service.impl

import com.agoda.hotels.model.{Order, Hotel}
import org.scalatest._

/**
  * Created by maliniak on 3/26/17.
  */
class HotelServiceImplTest extends FunSuite {

  def fixture =
    new {
      val service = new HotelServiceImpl
    }

  test("testFindByCity") {
    val f = fixture
    f.service.loadHotels()

    assert(f.service.findByCity("Amsterdam").size === 6)
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 11))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 14))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 15))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 18))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 1))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 6))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 8))
    assert(f.service.findByCity("Bangkok").exists((h: Hotel) => h.id == 20) === false)

    assert(f.service.city2hotel.get("Berlin") === None)
  }

  test("findByCity should be case-insensitive") {
    val f = fixture
    f.service.loadHotels()
    assert(f.service.findByCity("Amsterdam") ===  f.service.findByCity("AMSTERDAM"))
    assert(f.service.findByCity("Amsterdam") === f.service.findByCity("AmStErDam"))
  }

  test("findByCity should sort results if needed") {
    val f = fixture
    f.service.loadHotels()
    assert(f.service.findByCity("Amsterdam", Order.ASC) ===  f.service.findByCity("amsterdam", Order.DESC).reverse)
  }

  test("testLoadHotels") {
    val f = fixture
    f.service.loadHotels()
    assert(f.service.city2hotel.keySet.size === 3)

  }
}
