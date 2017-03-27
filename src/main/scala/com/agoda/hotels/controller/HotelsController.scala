package com.agoda.hotels.controller

import com.agoda.hotels.model.Hotel
import com.agoda.hotels.service.HotelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import scala.collection.JavaConverters._
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(path = Array("/hotels"))
class HotelsController (@Autowired val hotelService: HotelService)  {

  // I wasn't able to make it work with scala.list so I converted the result list to java equivalent
  @RequestMapping(path = Array("search"))
  @RequestBody
  def greeting(@RequestParam("city") city: String): java.util.List[Hotel] = {
    hotelService.findByCity(city).asJava
  }

  /**
    * Exception handler for reading reason value of ResponseStatus annotation and setting it as a message
    */
  @ExceptionHandler
  @ResponseBody
  private[controller] def handle(ex: Exception): ResponseEntity[_] = {
    val rs: ResponseStatus = ex.getClass.getAnnotation(classOf[ResponseStatus])
    new ResponseEntity[String]("{message: \"" + rs.reason + "\"}", rs.value)
  }


}


