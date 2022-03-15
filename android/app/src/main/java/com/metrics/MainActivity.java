package com.metrics;

import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;

import expo.modules.ReactActivityDelegateWrapper;
import com.reactlibrary.RNDataUsageLibraryPackage;

public class MainActivity extends ReactActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Set the theme to AppTheme BEFORE onCreate to support
    // coloring the background, status bar, and navigation bar.
    // This is required for expo-splash-screen.
    setTheme(R.style.AppTheme);
    super.onCreate(null);
  }

  /**
   * Returns the name of the main component registered from JavaScript.
   * This is used to schedule rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "main";
  }

  private static final int READ_PHONE_STATE_REQUEST = 37;

  // Network stats manager

  @Override
  protected void onResume() {
    super.onResume();
    requestPermissions();
  }

  private void requestPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!hasPermissionToReadNetworkHistory()) {
        return;
      }

      if (!hasPermissionToReadPhoneStats()) {
        requestPhoneStateStats();
        return;
      }
    }
  }

  private boolean hasPermissionToReadNetworkHistory() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return true;
    }
    final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
    int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(), getPackageName());
    if (mode == AppOpsManager.MODE_ALLOWED) {
      return true;
    }
    appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
        getApplicationContext().getPackageName(),
        new AppOpsManager.OnOpChangedListener() {
          @Override
          @TargetApi(Build.VERSION_CODES.M)
          public void onOpChanged(String op, String packageName) {
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),
                getPackageName());
            if (mode != AppOpsManager.MODE_ALLOWED) {
              return;
            }
            appOps.stopWatchingMode(this);
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            if (getIntent().getExtras() != null) {
              intent.putExtras(getIntent().getExtras());
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
          }
        });
    requestReadNetworkHistoryAccess();
    return false;
  }

  private boolean hasPermissionToReadPhoneStats() {
    if (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
      return false;
    } else {
      return true;
    }
  }

  private void requestReadNetworkHistoryAccess() {
    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    startActivity(intent);
  }

  private void requestPhoneStateStats() {
    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE },
        READ_PHONE_STATE_REQUEST);
  }

  // Continuation of main module
  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new ReactActivityDelegateWrapper(this,
        new ReactActivityDelegate(this, getMainComponentName()));
  }

  /**
   * Align the back button behavior with Android S
   * where moving root activities to background instead of finishing activities.
   * 
   * @see <a href=
   *      "https://developer.android.com/reference/android/app/Activity#onBackPressed()">onBackPressed</a>
   */
  @Override
  public void invokeDefaultOnBackPressed() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
      if (!moveTaskToBack(false)) {
        // For non-root activities, use the default implementation to finish them.
        super.invokeDefaultOnBackPressed();
      }
      return;
    }

    // Use the default back button implementation on Android S
    // because it's doing more than {@link Activity#moveTaskToBack} in fact.
    super.invokeDefaultOnBackPressed();
  }
}
