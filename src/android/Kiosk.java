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

/**
 * This class echoes a string called from JavaScript.
 */
public class Kiosk extends CordovaPlugin {
    private String TAG = "HOTEL_DIGITAL";
    private Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("chooseLauncher")) {
            //String message = args.getString(0);
            this.chooseLauncher(callbackContext);
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
        context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
        if (!isMyLauncherDefault()) {
            context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
            Log.d(TAG, "setting launcher");

            resetPreferredLauncherAndOpenChooser();
        }
    }

     public void resetPreferredLauncherAndOpenChooser() {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, CordovaActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }
}
