package com.agoda.hotels.service

import com.agoda.hotels.model.{Order, Hotel}

trait HotelService {
  def findByCity(city: String, order: Order = null): List[Hotel]
}
