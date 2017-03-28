### Hotel API

REST API written in Scala with Spring Boot and Gradle. Implements token-bucket-like algorithm for limiting requests rate (per API key)

## Running
`./gradlew bootRun`   #runs embedded Tomcat server with the app <br/>
`./gradlew test`      #runs tests<br/>

## Calling with curl

`curl -i -H "Authorization: lowkey" localhost:8080/hotels/search?city=Amsterdam` <br/>
`curl -i -H "Authorization: midkey" localhost:8080/hotels/search?city=Amsterdam&order=desc` <br/>
`curl -i -H "Authorization: highkey" localhost:8080/hotels/search?city=Berlin <br/>`


## Requires
scala >= 2.12


