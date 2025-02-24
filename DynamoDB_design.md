# DynamoDB Design Overview

This design outlines the schema and relationships for a set of entities (`Client`, `Journal`, `Message`, `Session`, and `Therapist`) in a DynamoDB-based application. The schema leverages DynamoDB's capabilities, including primary keys, global secondary indexes (GSI), and attribute types, to provide a scalable, efficient, and flexible backend for the application's needs.

---

## **Entities and Design Details**

### 1. **Client Table**
- **Primary Key**: `clientId` (Hash Key, auto-generated)  
  - Represents a unique client identifier.
- **Attributes**:
  - `email`: Client's email. Indexed by `EmailIndex`.
  - `name`: Client's name. Indexed by `NameIndex`.
  - `password`: Hashed password.
  - `description`: Optional client description.
  - `therapists`: List of associated therapist emails.
  - `role`: Enum, defaulting to `CLIENT`. Indexed by `RoleIndex`.
- **Indexes:**  
  - **GSI Name: `EmailIndex`**  
    - **Partition Key:** `email`  
    - **Projection:** ALL attributes  
    - **Query methods:**
      -`authenticate`
      -`getClientByEmail`
      -`updateClient`
      -`updateClientEmail`
      -`deleteClient`
      -`mapTherapistClient`
      -`unmapTherapistClient` 
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
  - `clientEmail`: Email of the associated client. Indexed by `ClientEmailIndex`.
  - `title`: Title of the journal entry. Indexed by `TitleIndex`.
  - `content`: Journal content.
  - `emotions`: List of emotions, converted using a custom converter.
  - `therapists`: List of therapist emails with access.
- **Indexes:**  
  - **GSI Name: `ClientEmailIndex`**  
    - **Partition Key:** `clientEmail`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getAllJournalsByClient`
      -`findAccessibleJournals`
  - **GSI Name: `TitleIndex`**  
    - **Partition Key:** `title`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getAllJournalsByTitle`
      -`findAccessibleJournals`

### 3. **Message Table**
- **Primary Key**: `messageKey` (Hash key, auto-generated)  
  - A unique identifier for each message.
- **Attributes**:
  - `senderId`: Email of the sender. Indexed by `SenderIndex`.
  - `receiverId`: Email of the receiver. Indexed by `ReceiverIndex`.
  - `messageContent`: The message's text content.
  - `timestamp`: Time of message creation. Indexed by `TimeStampIndex`.
- **Indexes:**  
  - **GSI Name: `SenderIndex`**  
    - **Partition Key:** `senderId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getMessagesSentByCurrentUser`
      -`getConversationOfCurrentUser`
      -`getAllMessagesOfCurrentUser`
  - **GSI Name: `ReceiverIndex`**  
    - **Partition Key:** `receiverId`  
    - **Projection:** ALL attributes  
    - **Query methods:**
      -`getMessagesReceivedByCurrentUser`
      -`getConversationOfCurrentUser`
      -`getAllMessagesOfCurrentUser`
  - **GSI Name: `TimeStampIndex`**  
    - **Partition Key:** `timestamp`  
    - **Projection:** ALL attributes  
    - **Query methods:**
      -`getMessagesReceivedByCurrentUser`
      -`getConversationOfCurrentUser`
      -`getAllMessagesOfCurrentUser`

### 4. **Session Table**
- **Primary Key**: `sessionId` (Hash Key, auto-generated)  
  - Unique identifier for each session.
- **Attributes**:
  - `therapistEmail`: Email of the therapist. Indexed by `TherapistEmailIndex`.
  - `clientEmail`: Email of the client (optional). Indexed by `ClientEmailIndex`.
  - `sessionDate`: Date and time of the session. Indexed by `SessionDateIndex`.
  - `privateNotes`: Confidential notes for the therapist.
  - `sharedNotes`: Notes shared with the client.
  - `isOpen`: Indicates if the session is open for all clients.
- **Indexes:**  
  - **GSI Name: `TherapistEmailIndex`**  
    - **Partition Key:** `therapistEmail`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`findSessionByTherapistEmail`
      -`findSessionByClientAndTherapist`  
      -`searchNotesForTherapist`
  - **GSI Name: `ClientEmailIndex`**  
    - **Partition Key:** `clientEmail`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`findOpenSessions`
      -`findSessionByClientEmail`
      -`findSessionByClientAndTherapist`
      -`searchSharedNotes`      
  - **GSI Name: `SessionDateIndex`**  
    - **Partition Key:** `sessionDate`  
    - **Projection:** ALL attributes  


### 5. **Therapist Table**
- **Primary Key**: `therapistId` (Hash Key, auto-generated )
  - Unique identifier for each therapist.
- **Attributes**:
  - `email`: Therapist's email. Indexed by `EmailIndex`.
  - `password`: Hashed password.
  - `name`: Therapist's name. Indexed by `NameIndex`
  - `specialization`: Comma-separated string of specializations. Indexed by `SpecializationIndex`.
  - `role`: Default to `THERAPIST`. Indexed by `RoleIndex`.
  - `availableSlots`: List of available session slots.
  - `clients`: List of client emails.
- **Indexes:**  
  - **GSI Name: `EmailIndex`**  
    - **Partition Key:** `email`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`authenticate`
      -`getTherapistByEmailforPublic`
      -`deleteTherapistByEmail`
      -`addClientToTherapist`
      -`removeClientToTherapist`
      -`updateSpecialization`
      -`updateAvailableSlots`
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
      -`findTherapistsBySpecialization`
      

### 6. **Appointment Table**  
- **Primary Key:** `appointmentId` (Hash Key, auto-generated UUID)  
- **Attributes:**
  - `clientId`: Client ID **(Indexed by ClientIndex)**  
  - `therapistId`: Therapist ID **(Indexed by TherapistIndex)**  
  - `appointmentDate`: Date & time of the appointment **(Indexed by DateIndex)**  
  - `status`: `CONFIRMED`, `CANCELLED`, `COMPLETED` **(Indexed by StatusIndex)**  
  - `sessionId`: If a session was created from this appointment  

- **Indexes:**
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`getAppointmentsByClient`   
  - **GSI Name: `TherapistIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes 
    - **Query methods:**
      -`getAppointmentsByTherapist`
  - **GSI Name: `DateIndex`**  
    - **Partition Key:** `appointmentDate`  
    - **Projection:** ALL attributes  
  - **GSI Name: `StatusIndex`**  
    - **Partition Key:** `status`  
    - **Projection:** ALL attributes  

### 7. **Requests Table**  
- **Primary Key:** `requestId` (Hash Key, auto-generated UUID)  
- **Attributes:**
  - `type`: `MAPPING`, `JOURNAL_ACCESS`, or `APPOINTMENT` **(Indexed by TypeIndex)**  
  - `clientId`: Client ID (nullable for some requests) **(Indexed by ClientIndex)**  
  - `therapistId`: Therapist ID **(Indexed by TherapistIndex)**  
  - `journalId`: Journal ID (Only for `JOURNAL_ACCESS`)  
  - `requestedDate`: Date of requested appointment (Only for `APPOINTMENT`)  
  - `message`: Optional message from sender  
  - `status`: `PENDING`, `APPROVED`, `REJECTED` **(Indexed by StatusIndex)**  

- **Indexes:**
  - **GSI Name: `TypeIndex`**  
    - **Partition Key:** `type`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`approvingTherapistClientMapping`
      -`approvingJournalAccess`
      -`approvingAppointment`   
  - **GSI Name: `TherapistIndex`**  
    - **Partition Key:** `therapistId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`approvingAppointment`      
  - **GSI Name: `ClientIndex`**  
    - **Partition Key:** `clientId`  
    - **Projection:** ALL attributes
    - **Query methods:**
      -`approvingTherapistClientMapping`
      -`approvingJournalAccess`  
  - **GSI Name: `StatusIndex`**  
    - **Partition Key:** `status`  
    - **Projection:** ALL attributes  


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
        AttributeName=clientEmail,AttributeType=S \
        AttributeName=title,AttributeType=S \
    --key-schema \
        AttributeName=journalId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=ClientEmailIndex,KeySchema=[{AttributeName=clientEmail,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TitleIndex,KeySchema=[{AttributeName=title,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 3. Create `Message` Table
```bash
aws dynamodb create-table \
    --table-name message \
    --attribute-definitions \
        AttributeName=messageKey,AttributeType=S \
        AttributeName=senderId,AttributeType=S \
        AttributeName=receiverId,AttributeType=S \
        AttributeName=timestamp,AttributeType=S \
    --key-schema \
        AttributeName=messageKey,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=SenderIndex,KeySchema=[{AttributeName=senderId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=ReceiverIndex,KeySchema=[{AttributeName=receiverId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TimeStampIndex,KeySchema=[{AttributeName=timestamp,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

### 4. Create `Session` Table
```bash
aws dynamodb create-table \
    --table-name session \
    --attribute-definitions \
        AttributeName=sessionId,AttributeType=S \
        AttributeName=therapistEmail,AttributeType=S \
        AttributeName=clientEmail,AttributeType=S \
        AttributeName=sessionDate,AttributeType=S \
    --key-schema \
        AttributeName=sessionId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=TherapistEmailIndex,KeySchema=[{AttributeName=therapistEmail,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=ClientEmailIndex,KeySchema=[{AttributeName=clientEmail,KeyType=HASH}],Projection={ProjectionType=ALL}" \
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

### 7. Create `Request` Table
```bash
aws dynamodb create-table \
    --table-name request \
    --attribute-definitions \
        AttributeName=clientId,AttributeType=S \ 
        AttributeName=therapistId,AttributeType=S \
        AttributeName=requestedDate,AttributeType=S \
        AttributeName=status,AttributeType=S \
        AttributeName=sessionId,AttributeType=S \
        AttributeName=message,AttributeType=S \
        AttributeName=type,AttributeType=S \
        AttributeName=journalId,AttributeType=S \
    --key-schema \
        AttributeName=requestId,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=ClientIndex,KeySchema=[{AttributeName=clientId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TherapistIndex,KeySchema=[{AttributeName=therapistId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=TypeIndex,KeySchema=[{AttributeName=type,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=StatusIndex,KeySchema=[{AttributeName=status,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

---

This design efficiently supports the application's requirements for client-therapist interactions, journal entries, messaging, and session management. The indexes enable rapid querying and ensure scalability.
