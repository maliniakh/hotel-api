package com.agoda.hotels.controller.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "Request rate exceeded")
class RateLimitExceededException extends RuntimeException
