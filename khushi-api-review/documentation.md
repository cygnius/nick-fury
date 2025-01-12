# Therapy Journaling API

The **Therapy Journaling API** is designed to manage therapy sessions, client-therapist interactions, journaling, messaging, and appointments. This serverless API is built using AWS services such as **API Gateway**, **Lambda**, and **DynamoDB** for scalable and efficient handling of therapy-related data.

## Table of Contents

- [Overview](#overview)
- [Components](#components)
- [Steps to Run the Project](#steps-to-run-the-project)
- [API Endpoints](#api-endpoints)
- [Error Handling](#error-handling)
- [Testing](#testing)

## Overview

The Therapy Journaling API facilitates the management of therapy sessions, journaling entries, client-therapist interactions, messages, and appointments in a secure and scalable manner. The API is built using AWS CDK to define and deploy the infrastructure, which includes API Gateway for routing HTTP requests to various Lambda functions responsible for the core logic.

## Components

### 1. **API Gateway**
   - Manages HTTP requests and integrates with Lambda functions.
   - Each resource (e.g., `/auth`, `/clients`, `/therapists`) corresponds to a specific functionality within the system.

### 2. **Lambda Functions**
   - **AuthLambda**: Handles user registration, authentication, and session management.
   - **ClientLambda**: Manages client data, including journal entries and session details.
   - **TherapistLambda**: Manages therapist-related actions and access to client data.
   - **SessionLambda**: Manages therapy sessions, including creation, updates, and access.
   - **MessageLambda**: Handles communication between clients and therapists through messages.
   - **AppointmentLambda**: Manages therapy appointments and status.

### 3. **DynamoDB**
   - Stores data for users, sessions, messages, and appointments.
   - Each entity (client, therapist, session) is stored as a separate table.

### 4. **Swagger Documentation**
   - The API is documented using Swagger (`swagger.yaml`), providing detailed information about each endpoint, methods, request/response formats, and error codes.

## Steps to Run the Project

1. **Clone the Repository**:
   Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   cd <project-directory>



## Flow of the Api/Application


![alt text](<uml img.jpg>)