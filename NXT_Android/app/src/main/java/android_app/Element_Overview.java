package android_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 10.06.2015.
 */
public class Element_Overview extends LinearLayout {
    LinearLayout scrollerLayout;
    int degree;
    int range;

    public Element_Overview(Context context, int degree, String range) {
        super(context);
        this.degree = degree;
        this.range = Integer.parseInt(range);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_screen, this);

        ImageButton imageButton = (ImageButton) findViewById(R.id.directionButton);
        imageButton.setRotation(degree);

        Button textButton = (Button) findViewById(R.id.rangeButton);
        textButton.setText(range);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Pressed");
                hideThis();
            }
        });
    }

    private void rotateDirection() {

    }

    private void hideThis(){
        ((ViewManager)getParent()).removeView(this);
    }
}
