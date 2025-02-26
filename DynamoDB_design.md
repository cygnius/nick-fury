# DynamoDB Design Overview

This design outlines the schema and relationships for a set of entities (`Client`, `Journal`, `Message`, `Session`, and `Therapist`) in a DynamoDB-based application. The schema leverages DynamoDB's capabilities, including primary keys, global secondary indexes (GSI), and attribute types, to provide a scalable, efficient, and flexible backend for the application's needs.

---

## **Entities and Design Details**

### 1. **Client Table**
- **Primary Key**: `clientId` (Hash Key, auto-generated)  
  - Represents a unique client identifier.
- **Attributes**:
  - `email`: Client's email..
  - `name`: Client's name. Indexed by `NameIndex`.
  - `password`: Hashed password.
  - `description`: Optional client description.
  - `therapists`: List of associated therapist.
  - `role`: Enum, defaulting to `CLIENT`. Indexed by `RoleIndex`.
- **Indexes:**  
  - **GSI Name: `NameIndex`**  
    - **Partition Key:** `name`  
    - **Projection:** ALL attributes  
  - **GSI Name: `RoleIndex`**  
    - **Partition Key:** `role`  
    - **Projection:** ALL attributes  

### 2. **Journal Table**
- **Primary Key**: `journalId` (Hash Key, auto-generated)  
  - Represents a unique journal entry.
- **Attributes**:
  - `clientId`: ID of the associated client. Indexed by `ClientIndex`.
  - `title`: Title of the journal entry. Indexed by `TitleIndex`.
  - `content`: Journal content.
  - `emotions`: List of emotions, converted using a custom converter.
  - `therapists`: List of therapist with access.
- **Indexes:**  
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getJournals`
  - **GSI Name: `TitleIndex`**  
    - **Partition Key:** `title`  
    - **Projection:** ALL attributes
    - **Query methods:**

### 3. **Message Table**
- **Primary Key**: `conversationKey` (Hash key,`user1ID#user2ID` )  
  - A unique identifier for conversation.
- **Sort Key**: `timestamp` (sort key,`timestamp` )  
  - A keeps the messages ordered.
- **Attributes**:
  - `senderId`: Id of the sender. Indexed by `SenderIndex`.
  - `receiverId`: Id of the receiver. Indexed by `ReceiverIndex`.
  - `messageContent`: The message's text content.
  - `timestamp`: Time of message creation.
- **Indexes:**  
  - **GSI Name: `SenderIndex`**  
    - **Partition Key:** `senderId`
    - **Sort Key:** `timestamp`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getAllMessagesOfCurrentUser`
  - **GSI Name: `ReceiverIndex`**  
    - **Partition Key:** `receiverId`
    - **Sort Key:** `timestamp`  
    - **Projection:** ALL attributes      
  - **GSI Name: `SenderReceiverIndex`**  
    - **Partition Key:** `senderId`
    - **Partition Key:** `receiverId`  
    - **Projection:** ALL attributes  
    - **Query methods:**
      -`getConversationOfCurrentUser`

### 4. **Session Table**
- **Primary Key**: `sessionId` (Hash Key, auto-generated)  
  - Unique identifier for each session.
- **Attributes**:
  - `therapistId`: Id  of the therapist. Indexed by `TherapistIdIndex`.
  - `clientId`: Id of the client (optional). Indexed by `ClientIdIndex`.
  - `sessionDate`: Date and time of the session. Indexed by `SessionDateIndex`.
  - `privateNotes`: Confidential notes for the therapist.
  - `sharedNotes`: Notes shared with the client.
  - `isOpen`: Indicates if the session is open for all clients.
- **Indexes:**  
  - **GSI Name: `TherapistIdIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getSessions`
      -`getOrSearchNotes`  
  - **GSI Name: `ClientIdIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getSessions`
      -`getOrSearchNotes`        
  - **GSI Name: `SessionDateIndex`**  
    - **Partition Key:** `sessionDate`  
    - **Projection:** ALL attributes  


### 5. **Therapist Table**
- **Primary Key**: `therapistId` (Hash Key, auto-generated )
  - Unique identifier for each therapist.
- **Attributes**:
  - `email`: Therapist's email.
  - `password`: Hashed password.
  - `name`: Therapist's name. Indexed by `NameIndex`
  - `specialization`: Comma-separated string of specializations. Indexed by `SpecializationIndex`.
  - `role`: Default to `THERAPIST`. Indexed by `RoleIndex`.
  - `availableSlots`: List of available session slots.
  - `clients`: List of client.
- **Indexes:**  
  - **GSI Name: `NameIndex`**  
    - **Partition Key:** `name`  
    - **Projection:** ALL attributes  
  - **GSI Name: `RoleIndex`**  
    - **Partition Key:** `role`  
    - **Projection:** ALL attributes  
  - **GSI Name: `SpecializationIndex`**  
    - **Partition Key:** `specialization`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getTherapist`
      

### 6. **Appointment Table**  
- **Primary Key:** `appointmentId` (Hash Key, auto-generated UUID)  
- **Attributes:**
  - `clientId`: Client ID. Indexed by `ClientIndex`.  
  - `therapistId`: Therapist ID. Indexed by `TherapistIndex`.  
  - `appointmentDate`: Date & time of the appointment. Indexed by `DateIndex`.  
  - `status`: `CONFIRMED`, `CANCELLED`, `COMPLETED`. Indexed by `StatusIndex`.  
  - `sessionId`: If a session was created from this appointment  

- **Indexes:**
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getAppointments`   
  - **GSI Name: `TherapistIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes 
    - **Query methods:**
      -`getAppointments`
  - **GSI Name: `DateIndex`**  
    - **Partition Key:** `appointmentDate`  
    - **Projection:** ALL attributes  
  - **GSI Name: `StatusIndex`**  
    - **Partition Key:** `status`  
    - **Projection:** ALL attributes  

### 7. **TherapistRequests Table**  
- **Primary Key:** `requestId` (Hash Key, auto-generated UUID)  
- **Attributes:**
  - `type`: `MAPPING`, `JOURNAL_ACCESS`. Indexed by `TypeIndex`.  
  - `clientId`: Client ID . Indexed by `ClientIndex`.  
  - `therapistId`: Therapist ID. Indexed by `TherapistIndex`  
  - `journalId`: Journal ID (Only for `JOURNAL_ACCESS`)  
  - `requestedDate`: Date of requested appointment (Only for `APPOINTMENT`)  
  - `message`: Optional message from sender  
  - `status`: `PENDING`, `APPROVED`, `REJECTED`. Indexed by `StatusIndex`.  

- **Indexes:**
  - **GSI Name: `TypeIndex`**  
    - **Partition Key:** `type`  
    - **Projection:** ALL attributes   
  - **GSI Name: `TherapistIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getTherapistRequests`      
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getTherapistRequests`        
  - **GSI Name: `StatusIndex`**  
    - **Partition Key:** `status`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getTherapistRequests`          

### 8. **ClientRequests Table**  
- **Primary Key:** `requestId` (Hash Key, auto-generated UUID)  
- **Attributes:**
  - `type`: `APPOINTMENT`. Indexed by `TypeIndex`.  
  - `clientId`: Client ID . Indexed by `ClientIndex`.  
  - `therapistId`: Therapist ID. Indexed by `TherapistIndex`  
  - `requestedDate`: Date of requested appointment (Only for `APPOINTMENT`)  
  - `message`: Optional message from sender  
  - `status`: `PENDING`, `APPROVED`, `REJECTED`. Indexed by `StatusIndex`.  

- **Indexes:**
  - **GSI Name: `TherapistIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getClientRequests`              
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getClientRequests`                
  - **GSI Name: `StatusIndex`**  
    - **Partition Key:** `status`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getClientRequests`                

---

## **AWS CLI Commands to Create Tables**

### 1. Create `Client` Table
```bash
aws dynamodb create-table \
    --table-name client \
    --attribute-definitions \
        AttributeName=client,AttributeType=S \
        AttributeName=email,AttributeType=S \
        AttributeName=name,AttributeType=S \
        AttributeName=role,AttributeType=S \
    --key-schema \
        AttributeName=clientId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=EmailIndex,KeySchema=[{AttributeName=email,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=NameIndex,KeySchema=[{AttributeName=name,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=RoleIndex,KeySchema=[{AttributeName=role,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 2. Create `Journal` Table
```bash
aws dynamodb create-table \
    --table-name journal \
    --attribute-definitions \
        AttributeName=journalId,AttributeType=S \
        AttributeName=clientId,AttributeType=S \
        AttributeName=title,AttributeType=S \
    --key-schema \
        AttributeName=journalId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TitleIndex,KeySchema=[{AttributeName=title,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 3. Create `Message` Table
```bash
aws dynamodb create-table \
    --table-name Message \
    --attribute-definitions \
        AttributeName=conversationKey,AttributeType=S \
        AttributeName=timestamp,AttributeType=S \
        AttributeName=senderId,AttributeType=S \
        AttributeName=receiverId,AttributeType=S \
    --key-schema \
        AttributeName=conversationKey,KeyType=HASH \
        AttributeName=timestamp,KeyType=RANGE \
    --global-secondary-indexes \
        "IndexName=SenderIndex,KeySchema=[{AttributeName=senderId,KeyType=HASH},{AttributeName=timestamp,KeyType=RANGE}],Projection={ProjectionType=ALL}" \
        "IndexName=ReceiverIndex,KeySchema=[{AttributeName=receiverId,KeyType=HASH},{AttributeName=timestamp,KeyType=RANGE}],Projection={ProjectionType=ALL}" \
        "IndexName=SenderReceiverIndex,KeySchema=[{AttributeName=senderId,KeyType=HASH},{AttributeName=receiverId,KeyType=RANGE}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 4. Create `Session` Table
```bash
aws dynamodb create-table \
    --table-name session \
    --attribute-definitions \
        AttributeName=sessionId,AttributeType=S \
        AttributeName=therapistId,AttributeType=S \
        AttributeName=clientId,AttributeType=S \
        AttributeName=sessionDate,AttributeType=S \
    --key-schema \
        AttributeName=sessionId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=TherapistIndex,KeySchema=[{AttributeName=therapistId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=SessionDateIndex,KeySchema=[{AttributeName=sessionDate,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 5. Create `Therapist` Table
```bash
aws dynamodb create-table \
    --table-name therapist \
    --attribute-definitions \
        AttributeName=therapistId,AttributeType=S \ 
        AttributeName=email,AttributeType=S \
        AttributeName=specialization,AttributeType=S \
        AttributeName=role,AttributeType=S \
    --key-schema \
        AttributeName=therapistId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=EmailIndex,KeySchema=[{AttributeName=email,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=SpecializationIndex,KeySchema=[{AttributeName=specialization,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=RoleIndex,KeySchema=[{AttributeName=role,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 6. Create `Appointment` Table
```bash
aws dynamodb create-table \
    --table-name appointment \
    --attribute-definitions \
        AttributeName=clientId,AttributeType=S \ 
        AttributeName=therapistId,AttributeType=S \
        AttributeName=appointmentDate,AttributeType=S \
        AttributeName=status,AttributeType=S \
        AttributeName=sessionId,AttributeType=S \
    --key-schema \
        AttributeName=appointmentId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TherapistIndex,KeySchema=[{AttributeName=therapistId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=DateIndex,KeySchema=[{AttributeName=appointmentDate,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=StatusIndex,KeySchema=[{AttributeName=status,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 7. Create `TherapistRequest` Table
```bash
aws dynamodb create-table \
    --table-name TherapistRequests \
    --attribute-definitions \
        AttributeName=requestId,AttributeType=S \
        AttributeName=type,AttributeType=S \
        AttributeName=clientId,AttributeType=S \
        AttributeName=therapistId,AttributeType=S \
        AttributeName=status,AttributeType=S \
    --key-schema \
        AttributeName=requestId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=TypeIndex,KeySchema=[{AttributeName=type,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TherapistIndex,KeySchema=[{AttributeName=therapistId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=StatusIndex,KeySchema=[{AttributeName=status,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 8. Create `ClientRequest` Table
```bash
aws dynamodb create-table \
    --table-name ClientRequests \
    --attribute-definitions \
        AttributeName=requestId,AttributeType=S \
        AttributeName=type,AttributeType=S \
        AttributeName=clientId,AttributeType=S \
        AttributeName=therapistId,AttributeType=S \
        AttributeName=status,AttributeType=S \
    --key-schema \
        AttributeName=requestId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=TypeIndex,KeySchema=[{AttributeName=type,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TherapistIndex,KeySchema=[{AttributeName=therapistId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=StatusIndex,KeySchema=[{AttributeName=status,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```
---

This design efficiently supports the application's requirements for client-therapist interactions, journal entries, messaging, and session management. The indexes enable rapid querying and ensure scalability.
