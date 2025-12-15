# Build Instructions for MindCare Admin Features

## Quick Start

After all files have been added, you need to rebuild the project to generate the R.java files for the new layouts.

### Option 1: Using Android Studio
1. Click **File** > **Sync Project with Gradle Files**
2. Click **Build** > **Clean Project**
3. Click **Build** > **Rebuild Project**

### Option 2: Using Command Line (PowerShell)
```powershell
cd C:\Users\Orillos\Desktop\Websites\MindCare
.\gradlew clean build
```

### Option 3: Using Command Line (Windows CMD)
```cmd
cd C:\Users\Orillos\Desktop\Websites\MindCare
gradlew.bat clean build
```

## Expected Outcome

After rebuilding, all "Unresolved reference" errors should be resolved as the Android build system generates:
- R.java file with references to all layouts
- BuildConfig files
- Data binding classes (if enabled)

## If Build Fails

1. **Check Gradle Sync**: Ensure Gradle sync completed successfully
2. **Check SDK**: Verify Android SDK is properly installed
3. **Check Dependencies**: Ensure all Firebase dependencies are downloaded
4. **Invalidate Caches**: In Android Studio: File > Invalidate Caches / Restart

## Setting Up Firebase Admin

Before testing admin features, create an admin user in Firestore:

1. Go to Firebase Console: https://console.firebase.google.com
2. Select your project
3. Navigate to **Firestore Database**
4. Click **Start Collection**
5. Collection ID: `admins`
6. Document ID: Enter the UID of the user you want to make admin
   - You can find a user's UID in the **Authentication** tab
7. Leave fields empty or add custom metadata
8. Click **Save**

## Testing Admin Features

1. **Test Admin Login**:
   - Run the app
   - Click "🔐 Admin Login" on login screen
   - Enter admin credentials
   - Should redirect to Admin Dashboard

2. **Test User Validation**:
   - Register a new test user (regular registration)
   - Login as admin
   - Navigate to "Validate Registrations"
   - Should see the new user in pending list

3. **Test Questionnaire Viewing**:
   - Complete a questionnaire as a regular user
   - Login as admin
   - Navigate to "View Questionnaires"
   - Should see the submitted questionnaire

4. **Test LSN Management**:
   - Login as admin
   - Navigate to "Manage LSN Users"
   - Toggle LSN status for approved users

## Summary of Changes

### New Activities (8 files)
- AdminDashboard.kt
- ValidateRegistrations.kt
- ViewQuestionnaires.kt
- ListLSNUsers.kt

### New Models (2 files)
- models/User.kt
- models/QuestionnaireResponse.kt

### New Layouts (8 files)
- admin_dashboard.xml
- validate_registrations.xml
- item_pending_user.xml
- view_questionnaires.xml
- item_questionnaire.xml
- dialog_questionnaire_details.xml
- list_lsn_users.xml
- item_lsn_user.xml

### Modified Files (4 files)
- Login.kt (added admin login)
- Register.kt (added new user fields)
- SelfAssessment.kt (added Firestore saving)
- login.xml (added admin login button)

### Documentation (2 files)
- ADMIN_FEATURES.md
- BUILD_INSTRUCTIONS.md (this file)

## Troubleshooting

### Issue: "Unresolved reference" errors persist
**Solution**: 
- Delete `app/build` folder manually
- Restart Android Studio
- Click Build > Rebuild Project

### Issue: Firebase not initialized
**Solution**:
- Verify `google-services.json` is in the `app/` directory
- Check that `com.google.gms.google-services` plugin is applied in `app/build.gradle.kts`

### Issue: Admin login always fails
**Solution**:
- Verify admin document exists in Firestore `admins` collection
- Check document ID matches the user's UID exactly
- Check Firestore security rules allow reading from `admins` collection

### Issue: Questionnaires not saving
**Solution**:
- Verify Firebase Firestore is enabled in Firebase Console
- Check internet connectivity
- Review Logcat for error messages
- Verify user is authenticated before completing questionnaire

## Next Steps

After successful build:
1. Run the app on emulator or device
2. Create admin user in Firestore (see above)
3. Test all admin features
4. Review and update Firestore security rules as needed
5. Consider implementing additional features from ADMIN_FEATURES.md

## Need Help?

- Check Logcat for detailed error messages
- Review Firebase Console for data verification
- Ensure all dependencies are properly synced
- Verify Android Studio is up to date

