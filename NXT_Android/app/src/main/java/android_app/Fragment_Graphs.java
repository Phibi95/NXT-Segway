package android_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 10.06.2015.
 */

@SuppressLint("ValidFragment")
public class Fragment_Graphs extends Fragment {

    LinearLayout scrollerLayout;
    WebView webview;
    Context context;

    public Fragment_Graphs(Context context) {
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        webview = (WebView) v.findViewById(R.id.webView);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        String[] items = {"Angle", "Offset", "GyroSpeed", "X"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        webview.loadUrl("http://zezation.me/NXT/display.php?type=line&table=ANGLE");
                        break;
                    case 1:
                        webview.loadUrl("http://zezation.me/NXT/display.php?type=line&table=OFFSET");
                        break;
                    case 2:
                        webview.loadUrl("http://zezation.me/NXT/display.php?type=line&table=GYROSPEED");
                        break;
                    case 3:
                        webview.loadUrl("http://zezation.me/NXT/display.php?type=line&table=X");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        webview.loadUrl("http://zezation.me/NXT/display.php?type=line&table=ANGLE");
        return v;
    }
}
