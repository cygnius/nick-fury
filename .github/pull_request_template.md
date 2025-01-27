# Apurva Kalpande

## Submission Checklist
*Mark checks as explained on this [link](https://docs.github.com/en/free-pro-team@latest/github/managing-your-work-on-github/about-task-lists#creating-task-lists), and remove this instruction*

- [x] `Steps to run the project` and `documentation` have been included in a README.md file at the root of your project.
- [x] No `binary/compressed` files have been added.
- [x] I understand that this submission is publicly visible.
- [x] I have not plagiarized or blatantly copied work; this submission is my original work. (Code of ethics)

## Project Overview (User Story)
This project is an API that allows users to perform various CRUD (Create, Read, Update, Delete) operations on a collection of items. The API is designed to manage a list of products, allowing users to add, retrieve, modify, and delete product data. The API is built using AWS Lambda and DynamoDB for storage, with Swagger documentation to make it easy for developers to interact with. Users can send requests via HTTP to interact with the API, and responses are provided in JSON format.

## Assumptions Made for This Project
- The user has basic knowledge of REST APIs and HTTP methods (GET, POST, PUT, DELETE).
- The AWS Lambda function has been properly set up and connected to the API Gateway.
- The DynamoDB table is created with the necessary fields to store product data.
- The API endpoint URLs are correctly set up and publicly accessible.
- Error handling and validations are implemented to some extent but can be improved further.

## What Did You Learn from This Assignment?
Through this assignment, I learned how to integrate AWS services such as Lambda and DynamoDB to build an API. I also gained hands-on experience with Swagger for documenting the API endpoints, making the API easier to understand and use for developers. Additionally, I learned how to structure and manage a Git repository, how to commit and push changes, and the importance of organizing code effectively in a team environment.

## If You Had More Time, What Enhancements Would You Make?
If I had more time, I would enhance the API by adding:
- Authentication and authorization (e.g., using AWS Cognito or JWT tokens).
- Pagination and sorting features to handle large sets of product data.
- More comprehensive error handling and input validation to improve the robustness of the API.
- Unit and integration tests to ensure the API works as expected under different conditions.
- API rate limiting to prevent abuse and ensure fair usage.
