# Software Development hiring assignments for EffDog
To read more about the recruitment process and open opportunities at EffDog, click [here](https://bit.ly/31O42C1)

## Introduction
We are giving you a business problem and you have to translate it into a technical solution - develop backend APIs (Java + Lambda + Swagger + DynamoDB) fulfilling all business requirements. 

The goal of this hiring challenge is to test: 
* your problem solving ability to think through and clarify an ambiguous business problem.
* your ability to understand a business problem and translate them into a technical solution.
* your ability to learn new technologies/frameworks and apply them in your project.
* your familiarity with concepts in Java & Web API design.

*Note: This is a public submission. Any work on this assignment does not bring EffDog or its employees any moat; and by design, is made to assess skills we need. Assignment provided is unrelated to EffDog's core business.* 

## Assignment
*   The busines problem is located [here](../stories/therapy.md).
*   The end submission expected from you is code for the backend API service which uses Swagger & is deployed on AWS.
*   The service should be built on top of the exact framework mentioned following all the best practices described in this [Instructions link](#instructions).
*   Follow  and [Submission Process](#submission-process) to submit your code.
*   For success, ensure you get as many checks as in our [Assessment Criteria](#assessment-criteria)
*   Deadline for the assignment: 1 week from the date you start working on the assignment.
*   We typically take 5-6 days to evaluate this submission.

## Training & Learning
*   Prequisite: You should be able to read & write basic java.
*   We expect and understand that you will NOT know all the technologies and frameworks required for this assignment. 
*   We expect you to learn them using the links provided in this file and then solve the assignment. 
*   You can also contact Prateek (contact details given below) if you have questions and review your API design/DB schema/thought process. This is an excellent opportunity for you to get mentorship and learn from the very senior people in the industry (12+ years of experience @ Amazon). Use it to your advantage. The more you ask questions, the more we understand your thought process and we will be able to assess you better.

## Instructions
### **Step 1**
* **API Design**: Design and express your APIs using Swagger. Get it reviewed with Prateek (Review details given below). The app is supposed to be API driven & API need to be written in Java. Trainings:
    * You will need to understand & write Swagger APIs. In case, you dont know about them, you can learn it here:
        * CRUD Design:
            * https://www.youtube.com/watch?v=lsMQRaeKNDk
            * https://www.youtube.com/watch?v=_YlYuNMTCc8
            * https://www.youtube.com/watch?v=ByuhQncSuAQ
        * Swagger:
            * [Option 1] Section 1 to Section 5 of this Udemy Course - [Course about Design First approach with OpenAPI(Swagger) specs and tools](https://www.udemy.com/course/swagger-tools-openapi/). Credentials at the bottom of the page. Please note that other candidates are also using this account, so the Udemy app will not be able to remember which chapter/lecture you were at in the previous learning session. Please note down the lecture number and play time in the lecture when you finish your learning session.
            * [Option 2] Youtube videos:
                * https://www.youtube.com/watch?v=7LQrTQTS_R0
                * https://www.youtube.com/watch?v=87ZFvJ7_-n0
                * https://www.youtube.com/watch?v=rkk2h6Tra9A
                * MindMap for Swagger file properties: https://drive.google.com/file/d/14gVFeB9wI-g8ZWQ6J_3KrCkWAgYNVTKc/view?usp=drive_link
    * Tool used: Postman.
    * **For submitting a review:**
        * Creata a github account if you haven't already.
        * Create a NEW git repository in your OWN account with 2 branches, "main" and "assignment".
        * Push all your changes to the "assignment" branch and then create a pull request ([relevant doc](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request)) from the "assignment" branch to the main branch.
        * Then give access to three email ids: founder@cygniusconsulting.com, rajmishra@cygniusconsulting.com, shubham@cygniusconsulting.com so that we can give comments on that pull request.
        * Share the link to this PR link with Prateek (contact details given below)  
    * Tips to defining your APIs:
        * Identify each core resource in the business problem - client, therapist, session, journal, journal entry, mapping, etc.
        * You must define CRUDL Operations for each Core Resources.
            * When you have a core resource like “client,” start by defining its core CRUDL methods (Create, Read, Update, Delete, List) -
            * Create (POST /clients)
            * Read (GET /clients/{clientId})
            * Update (PUT /clients/{clientId})
            * Delete (DELETE /clients/{clientId})
            * List (GET /clients)
            * These fundamental operations form the backbone of your API for that resource. After establishing these, you can add any specialized endpoints (like search) that extend the resource’s functionality.
        * Organize API Paths by Root Objects
            * When defining OpenAPI paths, group them under their “root” objects in a logical order. 
            * For example, start with /clients, then follow with any subpaths like /clients/{clientId}, /clients/{clientId}/therapist, etc. Once you finish all the /clients-related paths, move on to the next root object—such as /therapists—and list all those paths. This way, your specification stays organized, easy to read, and follows a clear hierarchy for each root object before switching to a new one.
        * Align API Methods With Resource Scope
            * When defining paths, keep the methods relevant to the resource they represent. 
            * For example, in /clients/{clientId}, include GET, POST, PUT, or DELETE operations specifically for managing client data (e.g., fetching client details or updating client information). 
            * Don’t mix unrelated operations (like therapist-client associations) in this path. This keeps each route focused on a single resource and makes your API more intuitive.
        * Use Components Section
            * Use the OpenAPI Components section for schemas, requestBodies and responses for better code readability and reusability.
     * More resources. Refer only if you want to really dig deep.
        * [Getting Started with Swagger](https://swagger.io/docs/specification/about/) 
        * [Swagger OpenAPI Specifications. Very elaborate documentation. Only refer when needed.](https://swagger.io/specification/)
        * [Swagger Tools](https://swagger.io/tools/)


### **Step 2**
* **Model**: Database for the project needs to be Dynamo DB. Design the Dynamo DB schema for the assignment. Get it reviewed with Prateek (contact details given below). Helpful guides:
   * [Understanding DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html)
   * [Getting started and Playing around with CLI with DDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStartedDynamoDB.html)
   * [Getting started with DDB & Java](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.html)
   * https://aws.amazon.com/dynamodb/getting-started/
   * [Best Practices of using DDB. Imp Read!](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/best-practices.html)
   * **For submitting a review:**
       * Creata a github account if you haven't already.
       * Create a NEW git repository in your OWN account with 2 branches, "main" and "assignment".
       * Push all your changes to the "assignment" branch and then create a pull request ([relevant doc](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request)) from the "assignment" branch to the main branch.
       * Then give access to three email ids: founder@cygniusconsulting.com, rajmishra@cygniusconsulting.com, shubham@cygniusconsulting.com so that we can give comments on that pull request.
       * Share the link to this PR link with Prateek (contact details given below)
   * **Tips for submitting a good review:**
       * It can be a txt file.
       * Define each object, its table.
       * For each DDB table, List each attribute and its data type. Which attribute is the hash partition key and range key? 
       * For each DDB table, what are additional GSI and LSI, what are the hash partition key and range key? 
       * For each of the APIs defined in Step 1, how are you constructing the query and which LSI/GSI are you using? 

### **Step 3**
* **Implementation**: Once the API design and the database design is finalized and reviewed, you will start with the implementation. The Java APIs need to be deployable as a AWS API Gateway + Lambda solution as a AWS CDK application. You should be testing your APIs through Postman. Make a [Postman collection](https://www.postman.com/collection/) containing all your API calls. All functionality should be executable over these APIs. Important Points:
    * Remember that we are looking ONLY for properly functioning backend APIs which can be demo-ed using Postman. We DO NOT expect you to make the frontend for the same.
    * You **DO NOT** need to implement ALL APIs. Please implement ONLY the following APIs-
      * **Therapy Session** - Create/Read/Update/Delete/List
      * **Mappings** (therapist-client mapping and journal access requests) - All the APIs in the OpenAPI YAML File
      * **Messages** - All the APIs in the OpenAPI YAML File
    * If you don't know about AWS, backend API, serverless paradigm of computing, you can learn it here:
        * You can learn about basics of AWS and how to create one account here by going through first 17 mins of this [youtube video](https://www.youtube.com/watch?v=ubCNZRNjhyo).
        * You can learn about AWS IAM basics from the first 19m 15s of this [youtube video](https://www.youtube.com/watch?v=GjVFf83dcE8).
        * Udemy Course - [Serverless using AWS Lambda for Java Developers](https://www.udemy.com/course/serverless-programming-using-aws-lambda-for-java-developers/). Credentials at the bottom of the page. Please note that other students are also using this account, so the Udemy app will not be able to remember which chapter/lecture you were at in the previous learning session. Please note down the lecture number and play time in the lecture when you finish your learning session.
    *  Extra training for reference and in-depth knowledge of AWS:
        * [Free Coursera course](https://www.coursera.org/learn/building-modern-java-applications-on-aws)
        * You can skip week 4 & week 6 as the course material taught in those weeks aren't relevant to the assignment.
    * Read up about [AWS CDK](https://docs.aws.amazon.com/cdk/v2/guide/home.html). Go through homepage, videos, Core conceepts and Getting Start in Java.  
        * Here is the [CDK 101 workshop](https://catalog.us-east-1.prod.workshops.aws/workshops/10141411-0192-4021-afa8-2436f3c66bd8/en-US).   
    * **Starting Point - Very very important**
        * Read Up & understand each and every line of [this blog](https://medium.com/i-love-my-local-farmer-engineering-blog/a-serverless-java-solution-for-deliveries-2a29497ca272)
        * [Code project from this blog](https://github.com/aws-samples/i-love-my-local-farmer/tree/main/DeliveryApi)
        * You can fork out this Github Project and use it as a starting point. It will act as the starting point for your application. You can modify this to build your assignment.
        * This projects uses RDS as the backend database. You need to change it to DDB. Exmaple projects in java reading and writing to dynamo db:
            * https://github.com/aws-samples/lambda-java8-dynamodb/tree/master
            * https://github.com/amazon-archives/realworld-serverless-application/blob/master/backend/src/main/java/software/amazon/serverless/apprepo/api/impl/ApplicationsService.java
    * For reference, another much [simpler github](https://github.com/aws-samples/cdk-lambda-packaging-java) repo to demonstrate how to deploy java code as lambda in AWS.
    * (Optional) Dependency injection through Dagger/Guice
* Document **list of assumptions** made, which are not covered in the stories but have been used in building the app.
* Tips for a good submission:
  * You **DO NOT** need to implement ALL APIs. Please implement ONLY the following APIs-
    * **Therapy Session** - Create/Read/Update/Delete/List
    * **Mappings** (therapist-client mapping and journal access requests) - All the APIs in the OpenAPI YAML File
    * **Messages** - All the APIs in the OpenAPI YAML File
  * What are we looking for in your submission -
    * Understanding of AWS concepts and ability to develop the code, deploy the infrastructure and debug it.
    * Clarity in CDK & IaaC Concepts.
    * Ability to write clear and concise code handling different edge cases and returning correct response codes.
    * Ability to organize code into coherent, logical classes & interface methods.
  * Important points:
    * **1. CDK Concepts and Code Clarity Must-Haves-**
      * **Remove RDS CDK code:** You don't need RDS for our assignment. We are using DDB as the storage engine. If it’s not strictly required—streamlines your infrastructure.
      * **Lambda-Related Resources:**
        * **One Function per API**: A simpler approach is to implement one Lambda function per API call as given in the Starting Point code. You must have clear & consistent naming conventions and error handling.
        * **Use a Helper/Factory Class for Lambda Creation:** You should abstract out repeated steps (like environment variables, function config) in one place, so your main stack is cleaner and changes are easier to maintain. This underscores good software design—keeping code DRY (Don’t Repeat Yourself) and well-organized.
        * By having a clean and well-defined lambda CDK code, you can make sure that your lambda function is well equipped with all the necessary details like environment and roles with required permissions which in turn helps the application code (lambda handler code).
      * **API Gateway Related Resources-**
        * **OpenAPI Integration:** The OpenAPI Specifications created in Step 1 should be integrated with the API Gateway CDK Object. This ensures your API Gateway automatically reflects the documented paths, models, and validations from your OpenAPI spec. The Starting point code uses JSON format for Open API spec. You can convert your YAML to JSON (tools available online) or you can change the code to read YAML.
        * **Connection with Lambda:** Make sure that your API Gateway is properly connected with its corresponding lambda to ensure that all the API Requests are directed to the lambda without any errors/delays.
        * **Logging and Monitoring:** You should enable access logging for your API Gateway. It gives you insight into request/response details, latency, and error rates.
        * By handling your API Gateway resources systematically—defining each resource path, method, and integration clearly—you’ll create an API that’s easy to follow, maintain, and debug.
      * **DynamoDB (DDB) Tables:**
        * They should reflect your database schema accurately (as defined in Step 2), with well-defined keys and Global Secondary Indexes (GSIs).
        * For efficient usage of DynamoDB Tables in your lambda handler code, make sure that the CDK Objects of DynamoDB Table and Lambda are well integrated with all the permissions in check.
      * **Organize CDK Resources:** While separating logically similar CDK resources into different classes is fine, you will get brownie points if you group related resources into their own CDK constructs for clarity and reusability.

    * **2. Application Code Must-Haves-**
      * **One Lambda Handler per API:** Maintain a separate handler java class for each operation. This ensures a clear separation of concerns and simplifies testing. The handler typically parses inputs, validates them, and constructs appropriate responses.
      * **Handle Edge Cases Thoroughly:** For example, respond with suitable HTTP error codes if incoming parameters are null, empty, or invalid.
      * **Dedicated DynamoDB Interaction Class:** Group read/write logic in a separate repository-like class. This helps you get and put data consistently and simplifies future modifications.
      * Try to organize code into separate classes so that no class is more than 200-300 lines of code.

    * **In Summary:**
      * Focus on readability, maintainability, and a clear separation of concerns. Keep AWS infrastructure definitions (CDK) well-structured, and write your Lambda handlers to address specific use cases with robust validation and error handling. Manage DynamoDB interactions in a dedicated service layer for clarity. Lastly, there are extra brownie points if your solution has strong observability — logging and monitoring — so you’re not just delivering functionality but a production-grade experience. \n
We look forward to seeing your implementation!


### Assessment Criteria
* Maintainability and organisation of code.
* Balancing between speed and quality of code - we understand that candidates can only spend limited time on these challenges. If you do make any trade-offs in favour of saving time, do mention them in comments. We do value calculated & well reasoned assumptions.
* Ownership - going beyond the exact job description to deliver a finished product. Optional elements above provide you a way to get more brownie points assuring success.
* No submissions to be made with compressed/binary files - all source code should be version controlled and visible in plain text in github. This is essential to our idealogy of public submission by interview candidates, not creating a competitive advantage for EffDog employees or organization.
* How much the candidate stays in touch with us during the process of the project. What is the quality of clarifying questions they asked. How clear is their thought process. 
* Follow [best practices](https://github.community/t/best-practices-for-pull-requests/10195) for pull requests.
* Ability to learn and use version control via Git

### Submission Process
1. Fork this repository to your personal Github profile
2. Remove all files and folders in the forked repository and start developing your project in it
3. Add a readme to the root location of your repository that details on exactly how to run the code
4. Fill the [Pull Request template](https://github.com/cygnius/nick-fury/blob/master/.github/pull_request_template.md) while you are raising a [Pull Request](https://docs.github.com/en/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)
    *   Check all the boxes that your Pull Request fulfills.
    *   Answer ALL questions in the template

### Code of Ethics
* Please be original, all your submissions are public via GitHub.
* Using ChatGPT is encouraged. Make sure you go through the code generated before using it blindly.
* Cite any external piece of code you're using, preferably with a link to the source.

### More Learning Resources
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

### Credentials for Udemy
* Login Link: https://www.udemy.com/join/passwordless-auth/
* Username: founder@cygniusconsulting.com
* It will send an OTP on this email. Whatsapp HR @ 8585924494 for the code.

### Contact Details for Prateek
* Text at Whatsapp: +91 9625596336
* Email: prateek@cygniusconsulting.com
* Calls only after 8 pm.
