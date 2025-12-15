# Fixing "Unresolved reference 'AdminDashboard'" Error

## ⚠️ The Error You're Seeing

```
e: file:///C:/Users/Orillos/Desktop/Websites/MindCare/app/src/main/java/com/inv8solutions/mindcare/Login.kt:101:63 
Unresolved reference 'AdminDashboard'.
```

## 📋 Why This Happens

This error occurs because:
1. New Kotlin files were added to the project
2. Android Studio/Gradle hasn't compiled them yet
3. The IDE doesn't know about the new `AdminDashboard` class yet

**This is NORMAL and EXPECTED when adding new files!**

## ✅ Solution (Choose ONE method)

### Method 1: Using Android Studio (RECOMMENDED)

1. **Sync Gradle Files**
   - Click: `File` → `Sync Project with Gradle Files`
   - Wait for sync to complete (watch progress bar at bottom)

2. **Clean Project**
   - Click: `Build` → `Clean Project`
   - Wait for it to finish

3. **Rebuild Project**
   - Click: `Build` → `Rebuild Project`
   - This may take 1-3 minutes
   - Watch the Build output at the bottom

4. **Verify**
   - The red underlines should disappear
   - You can now run the app

### Method 2: Using the Rebuild Script

1. **Close Android Studio** (important!)

2. **Run the rebuild script**
   - Double-click: `rebuild.bat` in the MindCare folder
   - Wait for it to complete (may take 2-5 minutes)

3. **Reopen Android Studio**
   - Open the project again
   - Wait for indexing to complete

### Method 3: Using Command Line

1. **Open PowerShell in the project folder**
   ```powershell
   cd C:\Users\Orillos\Desktop\Websites\MindCare
   ```

2. **Clean the project**
   ```powershell
   .\gradlew clean
   ```

3. **Build the project**
   ```powershell
   .\gradlew build
   ```

4. **Reopen Android Studio** and sync

### Method 4: Nuclear Option (if nothing else works)

1. **Close Android Studio**

2. **Delete these folders manually**:
   - `app/build` folder
   - `.gradle` folder (in project root)
   - `.idea` folder (in project root)

3. **Reopen Android Studio**
   - It will re-index everything
   - Click: `File` → `Sync Project with Gradle Files`
   - Click: `Build` → `Rebuild Project`

## 🔍 Verifying the Files Exist

All these files should exist. Check manually:

### Required Kotlin Files
- ✅ `app/src/main/java/com/inv8solutions/mindcare/AdminDashboard.kt`
- ✅ `app/src/main/java/com/inv8solutions/mindcare/ValidateRegistrations.kt`
- ✅ `app/src/main/java/com/inv8solutions/mindcare/ViewQuestionnaires.kt`
- ✅ `app/src/main/java/com/inv8solutions/mindcare/ListLSNUsers.kt`
- ✅ `app/src/main/java/com/inv8solutions/mindcare/models/User.kt`
- ✅ `app/src/main/java/com/inv8solutions/mindcare/models/QuestionnaireResponse.kt`

### Required Layout Files
- ✅ `app/src/main/res/layout/admin_dashboard.xml`
- ✅ `app/src/main/res/layout/validate_registrations.xml`
- ✅ `app/src/main/res/layout/item_pending_user.xml`
- ✅ `app/src/main/res/layout/view_questionnaires.xml`
- ✅ `app/src/main/res/layout/item_questionnaire.xml`
- ✅ `app/src/main/res/layout/dialog_questionnaire_details.xml`
- ✅ `app/src/main/res/layout/list_lsn_users.xml`
- ✅ `app/src/main/res/layout/item_lsn_user.xml`

## 🚨 If AdminDashboard.kt is Empty

If you open `AdminDashboard.kt` and it's empty:

1. **Delete the empty file**
2. **Recreate it** with this content:

```kotlin
package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboard : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        checkAdminAccess()

        findViewById<CardView>(R.id.cardValidateUsers).setOnClickListener {
            startActivity(Intent(this, ValidateRegistrations::class.java))
        }

        findViewById<CardView>(R.id.cardViewQuestionnaires).setOnClickListener {
            startActivity(Intent(this, ViewQuestionnaires::class.java))
        }

        findViewById<CardView>(R.id.cardListLSN).setOnClickListener {
            startActivity(Intent(this, ListLSNUsers::class.java))
        }

        findViewById<CardView>(R.id.cardLogout).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun checkAdminAccess() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLogin()
            return
        }

        firestore.collection("admins")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Access Denied: Admin privileges required", Toast.LENGTH_LONG).show()
                    redirectToLogin()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error checking admin access", Toast.LENGTH_SHORT).show()
                redirectToLogin()
            }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
```

3. **Save and rebuild** using Method 1 above

## ⏱️ Expected Timeline

- **Sync Gradle Files**: 10-30 seconds
- **Clean Project**: 5-15 seconds
- **Rebuild Project**: 1-3 minutes (first time), 30 seconds (subsequent times)
- **Total Time**: ~2-4 minutes

## ✅ How to Know It's Fixed

After rebuilding, you should see:
- ✅ No red underlines in Login.kt
- ✅ "Build Successful" message at the bottom
- ✅ Green "Run" button is enabled
- ✅ No errors in the "Build" tab

## 🎯 Quick Checklist

- [ ] All 6 Kotlin activity files exist and have content
- [ ] All 8 XML layout files exist
- [ ] Synced Gradle files
- [ ] Cleaned project
- [ ] Rebuilt project
- [ ] No red underlines in Login.kt
- [ ] Build shows "successful"

## 💡 Prevention for Future

Whenever you add new files to an Android project:
1. Always sync Gradle after adding files
2. Clean and rebuild if you see "Unresolved reference" errors
3. This is normal Android Studio behavior!

## 🆘 Still Having Issues?

If after trying all methods above you still see errors:

1. **Check Android Studio version**
   - Needs to be at least version 2023.1 or newer

2. **Check Gradle version**
   - Look in `gradle/wrapper/gradle-wrapper.properties`
   - Should be at least Gradle 8.0

3. **Check Java/JDK version**
   - File → Project Structure → SDK Location
   - Should be JDK 11 or newer

4. **Check internet connection**
   - Gradle needs to download dependencies

5. **Check disk space**
   - Need at least 5GB free for build cache

## 📞 Emergency Recovery

If nothing works and you need to start fresh:

1. **Backup these files** (copy to desktop):
   - All admin `.kt` files
   - All admin `.xml` files

2. **Close Android Studio**

3. **Delete these folders**:
   - `app/build`
   - `.gradle`
   - `.idea`

4. **Reopen project in Android Studio**

5. **Let it re-index** (wait for progress bar)

6. **Sync → Clean → Rebuild**

## 🎉 Success!

Once you see "BUILD SUCCESSFUL", you're ready to:
1. Run the app on emulator/device
2. Test the admin login feature
3. Create your first admin in Firebase

---

**Remember**: The error is TEMPORARY and is caused by the build system not knowing about new files yet. A simple rebuild fixes it!

