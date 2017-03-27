package com.agoda.hotels.model

import scala.beans.BeanProperty

class Hotel ( @BeanProperty val city: String,
              @BeanProperty val id: Long,
              @BeanProperty val roomType: RoomType,
              @BeanProperty val price: BigDecimal) {

}
