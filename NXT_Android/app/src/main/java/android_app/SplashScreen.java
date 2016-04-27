package android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 08.07.2015.
 */
public class SplashScreen extends Activity {

    Activity me;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 200;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        me=this;

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        ControlHandler ctrlHandler = new ControlHandler();
        ctrlHandler.connect();

        while(!ctrlHandler.isConnected()){

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(me, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
