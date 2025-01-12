# Parth Komawar

## Submission Checklist
*Mark checks as explained on this [link](https://docs.github.com/en/free-pro-team@latest/github/managing-your-work-on-github/about-task-lists#creating-task-lists), and remove this instruction*

- [x] `Steps to run the project` AND a `documentation` have been included in a README.md file at root of your project.
- [x] No `binaries/compressed` files have been added.
- [x] I understand that a submission here is publicly visible.
- [x] I have not plagiarized, or blatantly copied work; and this submission is my original work. (Code of ethics)

## Briefly write about the project that you have submitted from the perspective of the user.
The **Therapy Journaling Application** is designed to help users document their thoughts and emotions in a secure and easy-to-use web interface. Users can create journal entries, view their entries in a list, update existing ones, and delete them. The application is built using **Spring Boot** and uses **MySQL** as the backend database to store journal entries. 

Additionally, the application exposes several REST API endpoints for performing CRUD operations on journal entries, which can be tested using **Swagger UI**. This allows users (and developers) to easily interact with the API and see the available operations on journal entries.

The app is designed to be simple and intuitive, with an emphasis on security and privacy. The entries can be reviewed later to track personal growth and reflections. The user can also modify and delete entries if needed.

## Assumptions you have made for this project?
- The user will have basic knowledge of how to operate web applications.
- The database configuration (e.g., MySQL) is correctly set up.
- The project is intended to be run with **Spring Boot 3.x** and **Java 17+**.
- The API endpoints will be consumed using **Swagger UI** for testing purposes.
- The application is running in a local or cloud environment with internet connectivity for API testing (via Swagger or other means).

## Did you learn anything new while doing this assignment? Please explain.
Yes, I learned several new aspects during this project:
- **Integrating Swagger UI**: I learned how to seamlessly integrate **Swagger UI** with a Spring Boot project, allowing easy interaction with REST APIs and providing automatic documentation.
- **API Testing with JUnit and Mockito**: I learned how to write unit tests for REST API methods using **JUnit** and **Mockito**, including mocking services and repository layers.
- **JPA & Hibernate Integration**: I gained experience working with **JPA** and **Hibernate** for database interactions, such as using **Spring Data JPA** for CRUD operations.
- **Exception Handling and Data Validation**: I understood the importance of implementing proper exception handling and data validation in web applications to provide users with meaningful error messages and to avoid application crashes.

## If you had more time, what enhancements will you make?
If I had more time, I would:
- **Authentication and Authorization**: I would implement **Spring Security** to ensure that users can sign in and only access their own journal entries.
- **Frontend Interface**: Enhance the front-end (e.g., using React or Thymeleaf) to make the application more user-friendly and visually appealing, allowing users to interact with the journal in a more dynamic way.
- **Improved Error Handling**: Add more sophisticated error handling mechanisms, such as custom exception handling and global exception resolvers.
- **Search Functionality**: Implement the ability to search through the journal entries by keywords, dates, or emotional tone.
- **File Uploads**: Add a feature to allow users to upload files, images, or voice notes to supplement their journal entries.
- **Logging and Monitoring**: Integrate logging and monitoring (e.g., using **Spring Boot Actuator**) to provide real-time insight into the application's health and performance.

## Steps to run the project:
To run the **Therapy Journaling Application** locally, follow these steps:

### Prerequisites:
- **Java 17+** installed on your machine.
- **MySQL** or any other relational database configured and running.
- **Maven** installed (to manage project dependencies).
- **Swagger UI** enabled to test the API endpoints.

### Step-by-step instructions:
1. **Clone the repository**:
   ```
   git clone <your-github-repository-url>
   ```

2. **Navigate to the project directory**:
   ```
   cd therapy-journaling
   ```

3. **Configure the `application.properties`**:
   Update the `application.properties` file with the correct MySQL database URL, username, and password.
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/therapy_journal
   spring.datasource.username=<your-database-username>
   spring.datasource.password=<your-database-password>
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Run the project**:
   If you are using an IDE like **Spring Tool Suite (STS)** or **IntelliJ IDEA**, you can simply run the `TherapyJournalingApplication.java` class as a **Spring Boot Application**.

   Or, you can run the project using Maven from the terminal:
   ```
   ./mvnw spring-boot:run
   ```

5. **Access Swagger UI**:
   After running the application, navigate to:
   ```
   http://localhost:8080/swagger-ui/
   ```
   This will allow you to interact with the API and test the REST endpoints.

6. **Test the Application**:
   You can use the Swagger UI to test various endpoints like:
   - `GET /journal/entries`: To fetch all journal entries.
   - `POST /journal/entry`: To add a new journal entry.
   - `PUT /journal/entry/{id}`: To update an existing entry.
   - `DELETE /journal/entry/{id}`: To delete a specific entry.

7. **End the Application**:
   You can stop the application by terminating the process in your IDE or pressing `Ctrl + C` in the terminal.
