package com.agoda.hotels.service.impl

import javax.annotation.PostConstruct

import com.agoda.hotels.model.{RoomType, Hotel}
import com.agoda.hotels.service.HotelService
import org.springframework.stereotype.Service

import scala.collection.mutable
import scala.io.Source

@Service
class HotelServiceImpl extends HotelService {

  /**
    * mapping city to hotels makes it perform better at the expense of memory
    */
  private[impl] val city2hotel = new mutable.HashMap[String, mutable.Set[Hotel]] with mutable.MultiMap[String, Hotel]

  override def findByCity(city: String): List[Hotel] = {
    city2hotel.get(city) match {
      case Some(x) => x.toList
      case None => List()
    }
  }

  @PostConstruct
  def loadHotels() = {
    val readmeText : Iterator[String] = Source.fromResource("hoteldb.csv").getLines.drop(1)
    for(line <- readmeText) {
      val values = line.split(",").map(_.trim)
      val hotel = new Hotel(values(0), values(1).toLong, RoomType.getByName(values(2)), BigDecimal(values(3)).setScale(2))

      city2hotel.addBinding(hotel.city, hotel)
    }
  }
}
