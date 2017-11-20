# FeedReader

Feed Reader system exposes a set of API's that can be used to create feeds, users and articles. The users can subscribe/unsubscribe to a feed. 

## Pre Requisites
#### Maven Installed
#### JDK 1.8 Version

## Installation
```
git clone https://github.com/srinivas-d-12/feedreader.git
cd feedreader
mvn clean package
```

## Running 
#### Running tests
```
mvn test
```
#### Running application 
```
mvn spring-boot:run
```
#### Or 
```
java -jar ./target/feed-reader-1.0-SNAPSHOT.jar 
```

## FeedReader API

| Endpoint      | Request       | Description  |
| ------------- | ------------- |------------- |
| /users  | GET           | Lists all users |
| /users  | POST           | Add user . Parameters `{"name":"user-name", "email":"user@test.com"}` |
| /users/{id}  | GET           | Properties of the given user. Includes a link to all the feeds and articles the user has subscribed |
| /users/{id}/feeds | GET           | Lists all the feeds the user has subscribed  |
| /users/{id}/articles | GET           | Lists all the articles for the feeds the user has subscribed  |
| /users/{id}/feeds | PUT           | Subscribe to a feed. Parameters `{"id":"1"}` -> feedId  |
| /users/{id}/feeds | DELETE           | Unsubscribe to a feed. Parameters `{"id":"1"}` -> feedId  |
| /feeds  | GET           | Lists all feeds |
| /feeds  | POST           | Add feed . Parameters `{"name":"feed-name", "description":"feed-description"}` |
| /feeds/{id}  | GET           | Properties of the given feed. Includes a link to all articles |
| /feeds/{id}/articles | GET           | Lists all the articles that are added to the feed  |
| /feeds/{id}/articles | POST           | Add article to feed. Parameters `{"name":"article-name"}` |
| /articles  | GET           | Lists all articles |
| /articles/{id}  | GET           | Properties of the given article |

## DEMO 
[![Everything Is AWESOME](https://img.youtube.com/vi/StTqXEQ2l-Y/0.jpg)](https://drive.google.com/file/d/1sGsenOQnra46Y3iQV-Jm7BDQH9A26_o7/view?usp=sharing "Everything Is AWESOME")

## Choices
> I used spring boot as its easy to create a standalone Java Application, which includes an embedded tomcat server. </br>
> It can handle 200 concurrent requests. It reduces the need to write a lot of configuration and boilerplate code. </br>
> Due to time constraint, I went with a simple in memory database h2 which has a very small foot print </br>
> I have add versioning to each entity so that JPA uses optimistic locking.
> For persisting the data over restarts, I dump data to disk.</br>
> I have kept my model simlple. User <-> Feeds(Many to Many) and Feeds <-> Articles (One to Many).</br>
> If the model gets complex, Then I can introduce a subscription Entity class.</br>
> For the initial phase, I am not validating if user, feed or article has the same properties. Although each entity will have a generated Id and be treated as a new object. </br>

## Future Enhancements
#### Code
1.  Add Pagination
2.  Display or find by query parameters, Add Deletion of entities.
3.  Handle Duplicates and  errors
4.  More comments and tests :P. Never enough

#### DataBase 
0. Decouple the database layer from the application. 
1. Choose a database that is scalable and highly available. 
2. It needs to be decentralized and elastic. 
3. Choose Casandra or Hbase which can scale 

#### Conflict Handling 
1. Allow the database to handle it. i.e. last-writer wins 
2. Or Streaming approach, sequence of commit logs and rebuild the ordering sequence and merge the logs

#### High Availablity 
1. Deploy application on n servers based on the load
2. Add a loadbalancer to distribute the load. 

#### Monitoring
1. Use springboot actuator to display health metrics 
2. Use Dropwizard codehale metrics/actuator metrics to get metrics on how the application is performing. 

#### Security 
1. Use MTLS so that access is allowed only to users with permission
2. Can use kerberos too. 

#### RateLimiters
1. Develop or use rate limiters for unruly clients 

#### Cache 
1. Use memcached or any other caching system to reduce the number of times an external data source (such as a database or API) must be read













