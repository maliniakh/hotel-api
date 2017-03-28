package com.agoda.hotels.controller

import java.beans.PropertyEditorSupport

import com.agoda.hotels.model.{Order, Hotel}
import com.agoda.hotels.service.HotelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.WebDataBinder
import scala.collection.JavaConverters._
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(path = Array("/hotels"))
class HotelsController (@Autowired val hotelService: HotelService)  {

  // I wasn't able to make it work with scala.list so I converted the result list to the java equivalent
  @RequestMapping(path = Array("search"))
  @RequestBody
  def greeting(@RequestParam("city") city: String,
               @RequestParam(value = "order", required = false) order: Order): java.util.List[Hotel] = {
    hotelService.findByCity(city, order).asJava
  }

  @InitBinder
  def registerOrderEnumConverter(dataBinder: WebDataBinder) = {
    dataBinder.registerCustomEditor(classOf[Order], new PropertyEditorSupport {
      override def setAsText(text: String) = {
        try {
          setValue(Order.valueOf(text.toUpperCase))
        } catch {
          case iae: IllegalArgumentException => setValue(null)
        }
      }
    })
  }

}


