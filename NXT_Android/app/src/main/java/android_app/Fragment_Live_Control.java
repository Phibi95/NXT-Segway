package android_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 08.06.2015.
 */

public class Fragment_Live_Control extends Fragment {

    LinearLayout scrollerLayout;
    Context context;
    ImageButton up, down, left, right;
    ControlHandler ctrl;

    @SuppressLint("ValidFragment")
    public Fragment_Live_Control(Context context, ControlHandler ctrl) {
        this.context = context;
        this.ctrl = ctrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_control, container, false);

        up = (ImageButton) v.findViewById(R.id.drive_forward);
        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    ctrl.forwardAction();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ctrl.stopForwardAction();
                }
                return true;
            }
        });
        down = (ImageButton) v.findViewById(R.id.drive_backward);
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ctrl.backwardAction();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ctrl.stopBackwardAction();
                }
                return true;
            }
        });
        left = (ImageButton) v.findViewById(R.id.drive_left);
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ctrl.leftAction();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ctrl.stopLeftAction();
                }
                return true;
            }
        });
        right = (ImageButton) v.findViewById(R.id.drive_right);
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ctrl.rightAction();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ctrl.stopRightAction();
                }
                return true;
            }
        });

        SeekBar seeker = (SeekBar) v.findViewById(R.id.seekBar2);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ctrl.setSpeed(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }
}
