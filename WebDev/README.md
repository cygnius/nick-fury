## Web Development challenge assignments for Cygnius Consulting

### Introduction
The goal of this challenge is to test your familiarity with concepts in Java & Web API design.
To read more about the recruitment process and open opportunities at Cygnius Consulting Pvt. Ltd., click [here](https://bit.ly/31O42C1)

*Note: This is a public submission. Any work on this assignment does not bring Cygnius Consulting or its employees any moat; and by design, is made to assess skills we need. Stories provided are unrelated to Cygnius Consulting's core business.* 

### Assignment

*   You can select any one of the stories mentioned in the [stories](../stories/) folder, and develop an API service using Swagger & AWS for it. 
*   Follow [Instructions](#instructions) and [Submission Process](#submission-process) to complete the assignment.
*   For success, ensure you get as many checks as in our [Assessment Criteria](#assessment-criteria)
*   We typically take 5-6 days to evaluate this submission.

### Instructions

*   **Model**: Design database schema and implement using:
    * MySQL+Hibernate. Helpful links:
        * [Getting Started with Java and Hibernate](https://thorben-janssen.com/hibernate-getting-started/)
    * DynamoDB model. Helpful links:
        * [Understanding DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html)
        * [Getting started and Playing around with CLI with DDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStartedDynamoDB.html)
        * [Getting started with DDB & Java](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.html)
*   **View**: API Design
    * The app is supposed to be API driven & API need to be written in Java.
    * Written with [Swagger OpenAPI Specifications](https://swagger.io/specification/). [Getting Started with Swagger](https://swagger.io/docs/specification/about/) [Swagger Tools](https://swagger.io/tools/)
    * The Java APIs need to be deployable as a AWS API Gateway + Lambda solution as a AWS SAM application. Helpful links:
        * [Deploying a Hello-World API Gateway + Lambda application](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-getting-started-hello-world.html)
        * [Adding Swagger template to your APIGateway application](https://medium.com/carsales-dev/api-gateway-with-aws-sam-template-c05afdd9cafe)
        * Another blog about [Building a APIGateway + Lambda application with DDB in NodeJS](https://thenewstack.io/build-a-serverless-api-with-aws-gateway-and-lambda/)
        * Another blog about [Swagger + APIGateway + Lambda application](https://swagger.io/blog/api-development/swagger-amazon-api-gateway-and-lambda/)
    * (Optional) Dependency injection through Dagger/Guice.
    * (Optional) Documentation (as a [Postman collection](https://www.postman.com/collection/))
    * All stories should be executable over these APIs.
*   **View**: UI Design (*Optional*, for brownie points, in addition to API Design)
    *   An HTML frontend with ANY framework of choice. (functional/just wireframes)
*   Include **Java docs** for all functions and classes.
*   Document **list of assumptions** made, which are not covered in the stories but have been used in building the app.
*   Ask us anything – hiring at cygniusconsulting dot com.


### Assessment Criteria
1. Meeting ALL criteria (which are not explicitly marked as optional) in the [Instructions](#instructions).
2. Maintainability and organisation of code .
3. Balancing between speed and quality of code - we understand that candidates can only spend limited time on these challenges. If you do make any trade-offs in favour of saving time, do mention them in comments. We do value calculated & well reasoned assumptions.
4. Ownership - going beyond the exact job description to deliver a finished product. Optional elements above provide you a way to get more brownie points assuring success.
5. No submissions to be made with compressed/binary files - all source code should be version controlled and visible in plain text in github. This is essential to our idealogy of public submission by interview candidates, not creating a competitive advantage for Cygnius employees or organization.
6. How much the candidate stays in touch with us during the process of the project.
7. Follow [best practices](https://github.community/t/best-practices-for-pull-requests/10195) for pull requests.
8. [Guidelines](https://gist.github.com/turbo/efb8d57c145e00dc38907f9526b60f17) for clean commit messages.
9. Ability to learn and use version control via Git

### Submission Process
1. Fork this repository to your personal Github profile
2. Remove all files and folders in the forked repository and start developing your project in it
3. Add a readme to the root location of your repository that details on exactly how to run the code
4. Fill the [Pull Request template](https://github.com/cygnius/nick-fury/blob/master/.github/pull_request_template.md) while you are raising a [Pull Request](https://docs.github.com/en/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
    *   Check all the boxes that your Pull Request fulfills.
    *   Answer ALL questions in the template

### Code of Ethics
Please be original, all your submissions are public via GitHub.
Cite any external piece of code you're using, preferably with a link to the source.

### Learning Resources
1. [Version control with git](https://try.github.io/)
2. [Getting Started with Java and Hibernate](https://thorben-janssen.com/hibernate-getting-started/)
3. [Understanding DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html)
4. [Getting started and Playing around with CLI with DDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStartedDynamoDB.html)
5. [Getting started with DDB & Java](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.html)
6. [Swagger OpenAPI Specifications](https://swagger.io/specification/) 
7. [Getting Started with Swagger](https://swagger.io/docs/specification/about/) 
8. [Swagger Tools](https://swagger.io/tools/)
9. [Deploying a Hello-World API Gateway + Lambda application](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-getting-started-hello-world.html)
10. [Adding Swagger template to your APIGateway application](https://medium.com/carsales-dev/api-gateway-with-aws-sam-template-c05afdd9cafe)
11. Another blog about [Building a APIGateway + Lambda application with DDB in NodeJS](https://thenewstack.io/build-a-serverless-api-with-aws-gateway-and-lambda/)
12. Another blog about [Swagger + APIGateway + Lambda application](https://swagger.io/blog/api-development/swagger-amazon-api-gateway-and-lambda/)
