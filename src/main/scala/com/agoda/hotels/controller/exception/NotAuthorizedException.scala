package com.agoda.hotels.controller.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong or not present API key in Authorization header")
class NotAuthorizedException extends RuntimeException {

}

