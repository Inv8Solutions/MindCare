# MindCare Admin Features

## Overview
This document describes the admin functionality added to the MindCare Android application.

## Admin Features

### 1. Admin Dashboard
- **File**: `AdminDashboard.kt`
- **Layout**: `admin_dashboard.xml`
- **Description**: Main admin interface with access to all admin features
- **Features**:
  - Validate user registrations
  - View questionnaire responses
  - Manage LSN (Licensed Social Navigator) users
  - Logout

### 2. Validate User Registrations
- **File**: `ValidateRegistrations.kt`
- **Layout**: `validate_registrations.xml`, `item_pending_user.xml`
- **Description**: Review and approve/reject pending user registrations
- **Features**:
  - View list of pending registrations
  - See user details (name, email, registration date)
  - Approve or reject users
  - Track who approved and when

### 3. View Questionnaire Responses
- **File**: `ViewQuestionnaires.kt`
- **Layout**: `view_questionnaires.xml`, `item_questionnaire.xml`, `dialog_questionnaire_details.xml`
- **Description**: View mental health assessment responses from users
- **Features**:
  - List all questionnaire submissions
  - Show risk level (Low, Medium, High)
  - View detailed responses by tapping on items
  - Color-coded risk indicators
  - Sorted by submission date (newest first)

### 4. Manage LSN Users
- **File**: `ListLSNUsers.kt`
- **Layout**: `list_lsn_users.xml`, `item_lsn_user.xml`
- **Description**: Manage Licensed Social Navigator status for users
- **Features**:
  - View all approved users
  - Toggle LSN status for users
  - Visual indicators for LSN users
  - Sorted to show LSN users first

## Data Models

### User Model
```kotlin
data class User(
    val uid: String,
    val fullName: String,
    val email: String,
    val isApproved: Boolean,
    val isLSN: Boolean,
    val registrationDate: Long,
    val approvedBy: String?,
    val approvedDate: Long?
)
```

### QuestionnaireResponse Model
```kotlin
data class QuestionnaireResponse(
    val uid: String,
    val userName: String,
    val userEmail: String,
    val responses: Map<String, String>,
    val submittedDate: Long,
    val riskLevel: String // Low, Medium, High
)
```

## Firebase Collections

### 1. `admins` Collection
- **Purpose**: Store admin user IDs
- **Structure**: Document ID = User UID
- **Fields**: Can be empty or contain admin metadata

### 2. `users` Collection (Updated)
- **Purpose**: Store user information
- **New Fields**:
  - `isApproved`: Boolean - Whether user registration is approved
  - `isLSN`: Boolean - Whether user is a Licensed Social Navigator
  - `registrationDate`: Long - Timestamp of registration
  - `approvedBy`: String - UID of admin who approved
  - `approvedDate`: Long - Timestamp of approval

### 3. `questionnaires` Collection (New)
- **Purpose**: Store questionnaire responses
- **Document ID**: `{uid}_{timestamp}`
- **Fields**:
  - `uid`: User ID
  - `userName`: User's full name
  - `userEmail`: User's email
  - `responses`: Map of questions and answers
  - `submittedDate`: Timestamp
  - `riskLevel`: Calculated risk level (Low/Medium/High)

## Setup Instructions

### 1. Create Admin User in Firestore
To make a user an admin:
1. Go to Firebase Console
2. Navigate to Firestore Database
3. Create a collection named `admins`
4. Add a document with the user's UID as the document ID
5. Leave the document empty or add any metadata

### 2. Admin Login
- Users can log in as admin by clicking "🔐 Admin Login" on the login screen
- Enter admin credentials (email and password)
- System checks if the user exists in the `admins` collection
- If verified, redirects to Admin Dashboard

### 3. Risk Level Calculation
The questionnaire risk level is automatically calculated based on responses:
- **High Risk**: Any "Yes" to suicide-related questions (questions 14-20)
- **Medium Risk**: More than 10 "Yes" responses overall
- **Low Risk**: 10 or fewer "Yes" responses

## User Flow Updates

### Registration Flow
1. User registers with name, email, and password
2. Account created with `isApproved: false`
3. User appears in "Pending Registrations" for admin
4. Admin approves/rejects the registration
5. Upon approval, user can fully access the app

### Questionnaire Flow
1. User completes self-assessment questionnaire
2. Responses automatically saved to Firestore
3. Risk level calculated automatically
4. Admin can view responses in "View Questionnaires"

## Files Added

### Kotlin Files
- `models/User.kt`
- `models/QuestionnaireResponse.kt`
- `AdminDashboard.kt`
- `ValidateRegistrations.kt`
- `ViewQuestionnaires.kt`
- `ListLSNUsers.kt`

### Layout Files
- `layout/admin_dashboard.xml`
- `layout/validate_registrations.xml`
- `layout/item_pending_user.xml`
- `layout/view_questionnaires.xml`
- `layout/item_questionnaire.xml`
- `layout/dialog_questionnaire_details.xml`
- `layout/list_lsn_users.xml`
- `layout/item_lsn_user.xml`

### Modified Files
- `Login.kt` - Added admin login functionality
- `Register.kt` - Updated to include new user fields
- `SelfAssessment.kt` - Added Firestore saving of questionnaire responses
- `layout/login.xml` - Added admin login link

## Building the Project

After adding these files, rebuild the project:
```bash
./gradlew clean build
```

Or in Android Studio:
- Click "Build" > "Clean Project"
- Then "Build" > "Rebuild Project"

This will generate the necessary R.java files for the new layouts.

## Security Considerations

1. **Admin Access Control**: Only users in the `admins` collection can access admin features
2. **Authentication Required**: All admin operations require Firebase Authentication
3. **Firestore Rules**: Ensure proper Firestore security rules are in place:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Admin collection - only admins can read
    match /admins/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false; // Only create via console
    }
    
    // Users collection
    match /users/{uid} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth != null && 
        (request.auth.uid == uid || 
         exists(/databases/$(database)/documents/admins/$(request.auth.uid)));
    }
    
    // Questionnaires collection
    match /questionnaires/{docId} {
      allow read: if request.auth != null && 
        (resource.data.uid == request.auth.uid || 
         exists(/databases/$(database)/documents/admins/$(request.auth.uid)));
      allow create: if request.auth != null;
      allow update, delete: if request.auth != null && 
        exists(/databases/$(database)/documents/admins/$(request.auth.uid));
    }
  }
}
```

## Future Enhancements

Potential improvements:
1. Add export functionality for questionnaire data
2. Implement filtering and search for users and questionnaires
3. Add statistics dashboard for admins
4. Implement email notifications for high-risk assessments
5. Add admin user management (create/remove admins)
6. Implement pagination for large datasets
7. Add activity logs for admin actions

## Troubleshooting

### "Unresolved reference" errors
- Clean and rebuild the project
- Sync Gradle files
- Invalidate caches and restart Android Studio

### Admin login not working
- Verify admin user exists in `admins` collection
- Check Firestore security rules
- Verify Firebase Auth is properly configured

### Questionnaires not showing
- Ensure SelfAssessment properly saves data
- Check Firestore console for `questionnaires` collection
- Verify user completed questionnaire after code update

