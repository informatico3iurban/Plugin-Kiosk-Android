package cordova.plugin.kiosk;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Context;
import android.util.Log;

import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

/**
 * This class echoes a string called from JavaScript.
 */
public class Kiosk extends CordovaPlugin {
    private String TAG = "HOTEL_DIGITAL";
    private Context context;
    private int IMMERSIVEMODE = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("chooseLauncher")) {
            this.chooseLauncher(callbackContext);
            return true;
        } if (action.equals("enableImmersiveMode")) {
            this.enableImmersiveMode(callbackContext);
            return true;
        }
        return false;
    }

    public boolean isMyLauncherDefault() {
        PackageManager localPackageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        String str = localPackageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        return str.equals(context.getPackageName());
    }

     public void chooseLauncher(CallbackContext callback) {
        Log.d(TAG, "setting launcher");

        //context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
        //if (!isMyLauncherDefault()) {
            context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
  

            resetPreferredLauncherAndOpenChooser();
        //}
    }

     public void resetPreferredLauncherAndOpenChooser() {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, getMainClass());
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    public void enableImmersiveMode(CallbackContext callback) {
        Log.d(TAG, "enabling immersive mode");
        final View decordView = this.cordova.getActivity().getWindow().getDecorView();
        decordView.setSystemUiVisibility(IMMERSIVEMODE);
        decordView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                decordView.setSystemUiVisibility(IMMERSIVEMODE);
            }
        });
    }

    public Class getMainClass() {
        Class mainActivity = null;
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = launchIntent.getComponent().getClassName();

        try {
            mainActivity = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return mainActivity;
    }

}