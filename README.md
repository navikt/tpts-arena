### Run locally

Get Docker ([Desktop](https://www.docker.com/products/docker-desktop)).

Start a stack with Zookeeper, Kafka and a topic initializer.
   ```
   > docker compose -f .docker/docker-compose-localhost.yaml up
   ``` 
Wait for a message saying "_Ready to go!_"

Start application with localhost profile, e.g.
   ```
   > mvn -D'spring-boot.run.arguments'='--spring.config.name=localhost' spring-boot:run
   ```
or preferably using a Spring Boot run configuration in IntelliJ with program arguments `--spring.config.name=localhost`.

### Todo
* Reduce logging in localhost Kafka stack.