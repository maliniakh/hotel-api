package com.agoda.hotels.service

import com.agoda.hotels.model.Hotel

trait HotelService {
  def findByCity(city: String): List[Hotel]
}
