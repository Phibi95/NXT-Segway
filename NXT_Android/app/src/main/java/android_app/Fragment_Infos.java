package android_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 08.06.2015.
 */

public class Fragment_Infos extends Fragment {

    LinearLayout scrollerLayout;
    public static TextView k1, k2, k3, k4, alpha, offset, angle, angle_vel, posX, posY, posDeg;
    public static Activity main;

    public Fragment_Infos() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_infos, container, false);

        //get all the text edits to show the values in live mode
        alpha = (TextView) v.findViewById(R.id.alpha_edit);
        offset = (TextView) v.findViewById(R.id.offset_edit);
        angle = (TextView) v.findViewById(R.id.angle_edit);
        angle_vel = (TextView) v.findViewById(R.id.angle_velo_edit);
        posX = (TextView) v.findViewById(R.id.posX_edit);
        posY = (TextView) v.findViewById(R.id.posY_edit);
        return v;
    }

    public static void setAngle(final String value) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                angle.setText(value);
            }
        });
    }
    public static void setAngle_vel(final String value) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                angle_vel.setText(value);
            }
        });
    }
    public static void setPosX(final String value) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                posX.setText(value);
            }
        });
    }
    public static void setPosY(final String value) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                posY.setText(value);
            }
        });
    }
    public static void setOffset(final String value) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                offset.setText(value);
            }
        });
    }

    public void setMain(Activity main){
        this.main = main;
    }
}
