package com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.CommonLoginActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.MainActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.UserTypeChoiceActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.services.NotificationService;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.ChefUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.CustomerUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;

/**
 * Class to manage user session
 * like: logout immediately
 */
public abstract class SessionUtil {

    private static final String TAG = "SessU-debug";

    /*
     logout
     */
    public static void logoutNow(Context context, Authentication auth) {

        clearSharedPreferences(context);

        closeNotificationService(context);

        auth.signOut();

        Intent intent = new Intent(context, UserTypeChoiceActivity.class);

        // clear out all activities on the back stack and open LoginActivity
        // so that back press from this point on closes the app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(intent);
    }

    private static void clearSharedPreferences(Context context) {

        ChefUserProfileSharedPref.build(context).clearAllSharedPreferences();

        CustomerUserProfileSharedPref.build(context).clearAllSharedPreferences();

        UserAuthSharedPref.build(context).clearAllSharedPreferences();
    }

    private static void closeNotificationService(Context context) {

        context.stopService(new Intent(context, NotificationService.class));
    }

}
