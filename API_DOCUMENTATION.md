# Employee Leave Management System (ELMS) - API Documentation

Base URL: `http://localhost:8080`

## 1. Authentication

### 1.1 Sign Up (Register User)
**Endpoint:** `POST /api/auth/signup`

**Description:** Registers a new user (employee, admin, or manager).

**Request Body (JSON):**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "ROLE_USER" 
}
```
*Note: Roles can be `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MANAGER`*

**Response (200 OK):**
```
User registered successfully!
```

**Response (400 Bad Request):**
```
Error: Username is already taken!
```
or
```
Error: Email is already in use!
```

---

### 1.2 Sign In (Login)
**Endpoint:** `POST /api/auth/signin`

**Description:** Authenticates a user and returns a JWT token.

**Request Body (JSON):**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": [
    "ROLE_USER"
  ]
}
```

---

## 2. Leave Types (Admin Only)

### 2.1 Create Leave Type
**Endpoint:** `POST /api/leave-types`
**Authorization:** Admin only
**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "typeName": "Annual Leave",
  "maxDays": 20,
  "description": "Annual vacation leave"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "typeName": "Annual Leave",
  "maxDays": 20,
  "description": "Annual vacation leave"
}
```

---

### 2.2 Get All Leave Types
**Endpoint:** `GET /api/leave-types`
**Authorization:** All authenticated users

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "typeName": "Annual Leave",
    "maxDays": 20,
    "description": "Annual vacation leave"
  },
  {
    "id": 2,
    "typeName": "Sick Leave",
    "maxDays": 10,
    "description": "Medical leave"
  }
]
```

---

### 2.3 Update Leave Type
**Endpoint:** `PUT /api/leave-types/{id}`
**Authorization:** Admin only

**Request Body:** Same as Create

---

### 2.4 Delete Leave Type
**Endpoint:** `DELETE /api/leave-types/{id}`
**Authorization:** Admin only

---

## 3. Leave Requests

### 3.1 Apply for Leave
**Endpoint:** `POST /api/leaves/apply`
**Authorization:** All authenticated users

**Request Body:**
```json
{
  "leaveTypeId": 1,
  "startDate": "2026-03-01",
  "endDate": "2026-03-05",
  "reason": "Family vacation"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "user": { ... },
  "leaveType": { ... },
  "startDate": "2026-03-01",
  "endDate": "2026-03-05",
  "reason": "Family vacation",
  "status": "PENDING",
  "totalDays": 5,
  "appliedDate": "2026-02-03T11:00:00"
}
```

**Response (400 Bad Request):**
```
Leave request overlaps with existing approved leave
```
or
```
Insufficient leave balance. Available: 3 days
```

---

### 3.2 Get My Leaves
**Endpoint:** `GET /api/leaves/my-leaves`
**Authorization:** All authenticated users

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "leaveType": { "typeName": "Annual Leave" },
    "startDate": "2026-03-01",
    "endDate": "2026-03-05",
    "status": "PENDING",
    "totalDays": 5
  }
]
```

---

### 3.3 Get All Pending Leaves (Admin/Manager)
**Endpoint:** `GET /api/leaves/pending`
**Authorization:** Admin or Manager only

**Response:** List of all pending leave requests

---

### 3.4 Get All Leaves (Admin/Manager)
**Endpoint:** `GET /api/leaves`
**Authorization:** Admin or Manager only

**Response:** List of all leave requests

---

### 3.5 Approve/Reject Leave (Admin/Manager)
**Endpoint:** `PUT /api/leaves/{id}/process`
**Authorization:** Admin or Manager only

**Request Body:**
```json
{
  "status": "APPROVED",
  "remarks": "Approved for vacation"
}
```
*Note: status can be "APPROVED" or "REJECTED"*

**Response (200 OK):**
```json
{
  "id": 1,
  "status": "APPROVED",
  "remarks": "Approved for vacation",
  "processedBy": { ... },
  "processedDate": "2026-02-03T11:30:00"
}
```

---

### 3.6 Cancel Leave
**Endpoint:** `DELETE /api/leaves/{id}/cancel`
**Authorization:** Owner of the leave request

**Response (200 OK):**
```
Leave request cancelled successfully
```

---

## 4. Leave Balances

### 4.1 Get My Leave Balances
**Endpoint:** `GET /api/leave-balances/my-balances`
**Authorization:** All authenticated users

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "leaveType": {
      "id": 1,
      "typeName": "Annual Leave",
      "maxDays": 20
    },
    "totalDays": 20,
    "usedDays": 5,
    "remainingDays": 15
  },
  {
    "id": 2,
    "leaveType": {
      "id": 2,
      "typeName": "Sick Leave",
      "maxDays": 10
    },
    "totalDays": 10,
    "usedDays": 0,
    "remainingDays": 10
  }
]
```

---

## Authorization Header Format

For all protected routes, include the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Business Rules

1. **Leave Overlap**: Cannot apply for leave that overlaps with existing approved leave
2. **Leave Balance**: Must have sufficient leave balance to apply
3. **Approval Authority**: Only Admin and Manager can approve/reject leaves
4. **Cancellation**: Users can only cancel their own leave requests
5. **Balance Update**: Leave balance is automatically deducted when leave is approved
6. **Balance Restoration**: Leave balance is restored when approved leave is cancelled
