### Hotel API

REST API written in Scala with Spring Boot and Gradle. Implements token-bucket-like algorithm for limiting requests rate (per API key)

## Running
./gradlew bootRun   #runs embedded Tomcat server with the app
./gradlew test      #runs tests

## Calling with curl
curl -i -H "Authorization: lowkey" localhost:8080/hotels/search?city=Amsterdam
curl -i -H "Authorization: midkey" localhost:8080/hotels/search?city=Amsterdam&order=desc
curl -i -H "Authorization: highkey" localhost:8080/hotels/search?city=Berlin

## Requires
scala >= 2.12


