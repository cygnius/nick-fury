# DynamoDB Design Overview

This design outlines the schema and relationships for a set of entities (`Client`, `Journal`, `Message`, `Session`, and `Therapist`) in a DynamoDB-based application. The schema leverages DynamoDB's capabilities, including primary keys, global secondary indexes (GSI), and attribute types, to provide a scalable, efficient, and flexible backend for the application's needs.

---

## **Entities and Design Details**

### 1. **Client Table**
- **Primary Key**: `email` (Hash Key)  
  - Represents a unique client identifier.
- **Attributes**:
  - `name`: Client's name. Indexed by `NameIndex`.
  - `password`: Hashed password.
  - `description`: Optional client description.
  - `therapists`: List of associated therapist emails.
  - `role`: Enum, defaulting to `CLIENT`. Indexed by `RoleIndex`.

### 2. **Journal Table**
- **Primary Key**: `journalId` (Hash Key, auto-generated)  
  - Represents a unique journal entry.
- **Attributes**:
  - `clientEmail`: Email of the associated client. Indexed by `ClientEmailIndex`.
  - `title`: Title of the journal entry. Indexed by `TitleIndex`.
  - `content`: Journal content.
  - `emotions`: List of emotions, converted using a custom converter.
  - `therapists`: List of therapist emails with access.

### 3. **Message Table**
- **Primary Key**: `messageKey` (Composite: `senderId#receiverId#timestamp`)  
  - A unique identifier for each message.
- **Attributes**:
  - `senderId`: Email of the sender. Indexed by `SenderIndex`.
  - `receiverId`: Email of the receiver. Indexed by `ReceiverIndex`.
  - `messageContent`: The message's text content.
  - `timestamp`: Time of message creation. Indexed by `TimeStampIndex`.

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

### 5. **Therapist Table**
- **Primary Key**: `email` (Hash Key)  
  - Unique identifier for each therapist.
- **Attributes**:
  - `password`: Hashed password.
  - `name`: Therapist's name.
  - `specialization`: Comma-separated string of specializations. Indexed by `SpecializationIndex`.
  - `role`: Default to `THERAPIST`. Indexed by `RoleIndex`.
  - `availableSlots`: List of available session slots.
  - `clients`: List of client emails.

---

## **AWS CLI Commands to Create Tables**

### 1. Create `Client` Table
```bash
aws dynamodb create-table \
    --table-name client \
    --attribute-definitions \
        AttributeName=email,AttributeType=S \
        AttributeName=name,AttributeType=S \
        AttributeName=role,AttributeType=S \
    --key-schema \
        AttributeName=email,KeyType=HASH \
    --global-secondary-indexes \
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
        AttributeName=email,AttributeType=S \
        AttributeName=specialization,AttributeType=S \
        AttributeName=role,AttributeType=S \
    --key-schema \
        AttributeName=email,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=SpecializationIndex,KeySchema=[{AttributeName=specialization,KeyType=HASH}],Projection={ProjectionType=ALL}" \
        "IndexName=RoleIndex,KeySchema=[{AttributeName=role,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST
```

---

This design efficiently supports the application's requirements for client-therapist interactions, journal entries, messaging, and session management. The indexes enable rapid querying and ensure scalability.
