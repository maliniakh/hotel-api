package com.agoda.hotels

import java.time.Clock

import com.agoda.hotels.interceptor.RateLimitInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.servlet.config.annotation.{InterceptorRegistry, WebMvcConfigurerAdapter}

object Application extends App {
  SpringApplication.run(classOf[Application], args: _*)
}

@SpringBootApplication
class Application

/**
  * Registers interceptor and defines Clock bean
  */
@Configuration
class Conf extends WebMvcConfigurerAdapter {
  @Autowired
  val rateLimitInterceptor: RateLimitInterceptor = null

  override def addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(rateLimitInterceptor)
  }

  @Bean
  def clock: Clock = Clock.systemDefaultZone()
}

