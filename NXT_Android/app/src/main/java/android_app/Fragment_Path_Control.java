package android_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import commands.DriveCMD;
import commands.PathCMD;
import commands.TurnCMD;
import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 08.06.2015.
 */

public class Fragment_Path_Control extends Fragment{

    LinearLayout scrollerLayout;
    Context context;
    SeekBar seeker;
    ImageButton direction;
    ControlHandler ctrl;
    ArrayList<PathCMD> path;

    @SuppressLint("ValidFragment")
    public Fragment_Path_Control(Context context, ControlHandler ctrl) {
        this.ctrl = ctrl;
        this.context = context;
        path = new ArrayList<PathCMD>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_path_control, container, false);
        scrollerLayout = (LinearLayout) v.findViewById(R.id.scrollViewLayout);
        final TextView textEdit = (TextView) v.findViewById(R.id.editText);

        direction = (ImageButton) v.findViewById(R.id.up);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Pressed");
                scrollerLayout.addView(new Element_Overview(context, seeker.getProgress() - 180, "" + textEdit.getText()));
            }
        });

        seeker = (SeekBar) v.findViewById(R.id.seekBar);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                direction.setRotation(i - 180);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button startPath = (Button) v.findViewById(R.id.button_start);
        startPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Path Started");

                for(int i=1; i< scrollerLayout.getChildCount(); i++){
                    Element_Overview temp = (Element_Overview) scrollerLayout.getChildAt(i);
                    path.add(new TurnCMD(temp.degree));
                    path.add(new DriveCMD(temp.range));
                }

                ctrl.startPath(path);
            }
        });

        Button stopPath = (Button) v.findViewById(R.id.button_stop);
        stopPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Path paused");
                ctrl.pausePath();
            }
        });

        Button uploadPath = (Button) v.findViewById(R.id.button_upload);
        uploadPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Path upload");
            }
        });

        return v;
    }
}
