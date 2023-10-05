# OnlineBookstore

OnlineBookstore is a web application that implements the functionality of an online store with a "book" 
product. The application is developed using Spring Boot and Maven.

## Installation and startup instructions

1. Install the required dependencies by running the `mvn install` command.

2. Configure the database connection by changing the appropriate settings in the `application.properties` file as needed.

3. Run the application by running `mvn spring-boot:run`.

4. Open a browser and go to `http://localhost:9090/store`.

WARNING! Only the back-end (server) part of the project is implemented in this repository. For correct work
the application needs to connect it with the front-end part, which is located at the link (*not completed yet, the repository is private):
https://github.com/MichaelKaterinnik/OnlineBookstoreFrontend/tree/master

## Used technologies and tools

* Spring Boot
* Spring Security
* HikariCP
* Maven

## Functionality

In the current version, when interacting with the application, it is possible to register and save information about books, writers, book collections according to certain parameters. You can rate books and leave reviews on them, but for this you need to have the status of a registered user. A registered user can also add books to the wish list.

On the main page there is a list of books available for sale, sorted by popularity. In addition, you can view selections of books by various criteria (genres). The search for books by belonging to a certain selection, by belonging to a certain author, as well as by title has been implemented. The output of book lists can be filtered by a certain range of price or ratings.

Authorization and authentication are implemented - registered users have their own personal account in which their order history is stored, there is access to the reviews left about the books on the list. However, users cannot add/delete/update information about books, authors or book collections, as well as registered users in the database - this is the prerogative of the administrator only.

Distribution of roles, authorization and authentication is implemented using Spring Security using jwt technology.

## License

This project is distributed under the MIT license.

## Contacts

Для зв'язку з автором проекту, будь ласка, напишіть на адресу katerinnikmikhail@ukr.net.
