# OnlineBookstore

OnlineBookstore is a web application that implements the functionality of an online store with a "book" 
product. The application is developed using Spring Boot and Maven.

## Installation and startup instructions

1. Install the required dependencies by running the `mvn install` command.

2. Configure the database connection by changing the appropriate settings in the `application.properties` file as needed.

3. Run the application by running `mvn spring-boot:run`.

4. Open a browser and go to `http://localhost:9090/store`.

WARNING! Only the back-end (server) part of the project is implemented in this repository. For correct work
the application needs to connect it with the front-end part, which is located at the link:
https://github.com/MichaelKaterinnik/OnlineBookstoreFrontend/tree/master

## Used technologies and tools

* Spring Boot
* Spring Security
* Thymeleaf
* HikariCP
* Maven

## Функціональність

У поточній версії при взаємодії з додатком можна реєструвати та зберігати інформацію про книги, письменників,
добірки книг за певними параметрами. Книги можна оцінювати та залишати на них рецензії, але для цього треба мати статус
зареєстрованого користувача. Також зареєстрований користувач може додавати книги до списку бажаних товарів.

На головній сторінці реалізований вивід наявних у продажу книг, відсортованих за популярністю. Крім того, можна 
переглядати добірки книг за різними критеріями (жанрами). Реалізований пошук книг за належністю до певної добірки, за
належністю до певного автора, а також за назвою. Вивід списків книг може бути фільтровано за певним проміжком ціни або 
рейтингів.

Реалізована авторизація та автентифікація - зареєстровані користувачі мають власний 
особистий кабінет, в якому зберігається їх історія замовлень, є доступ до залишених відгуків про книги тавішліста. Проте,
користувачі не можуть додавати/видаляти/оновлювати інформацію щодо книг, авторів чи добірок книг, а також, відповідно 
і зареєстрованих користувачів у базі даних - це прерогатива лише адміністратора.

Розподіл ролей, авторизація та автентифікація реалізована за допомогою Spring Security із застосуванням jwt-технології.

## License

This project is distributed under the MIT license.

## Contacts

Для зв'язку з автором проекту, будь ласка, напишіть на адресу katerinnikmikhail@ukr.net.
