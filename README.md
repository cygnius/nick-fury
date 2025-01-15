# Therapy Journaling

## Project Overview

The **Therapy Journaling** project is a web-based application designed to assist individuals in journaling their thoughts, feelings, and experiences related to therapy or mental health. It leverages Spring Boot to manage backend functionality and uses MySQL as the database for storing user data.

The project provides a secure and user-friendly way for users to track their mental health progress, with functionalities like adding, editing, deleting, and viewing journal entries.

## Features
- **User Authentication**: Users can sign up, log in, and manage their accounts securely.
- **Journal Entry Management**: Users can create, view, update, and delete journal entries.
- **Database Integration**: Uses MySQL for storing user data and journal entries.
- **Swagger UI Integration**: API documentation available via Swagger for easy testing and interaction with the API.

## Technologies Used
- **Spring Boot**: Framework for building the backend.
- **MySQL**: Database for storing data.
- **Spring Data JPA**: For managing database operations.
- **Swagger**: For API documentation and testing.
- **JUnit 5**: For unit testing the backend services.

## Assumptions
- The project assumes that users have a basic understanding of how to interact with a REST API.
- The application uses a MySQL database, so MySQL should be set up locally or on a cloud service.
- API calls are authenticated using basic Spring Boot security mechanisms.

## Steps to Run the Project

### Prerequisites
1. **JDK 17** or later installed on your machine.
2. **MySQL** installed and running. You will need to set up a MySQL database for the application.
3. **Maven** installed for building and running the application.

### Setting Up the Project
1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/yourusername/therapy-journaling.git
   cd therapy-journaling
