package android_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;
import controller.ControlHandler;
import software_pratikum.myapplication.R;

/**
 * Created by Pascal on 08.06.2015.
 */

public class MainActivity extends FragmentActivity {

    ControlHandler ctrlHandler = new ControlHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hide keybaord
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //add content
        setContentView(R.layout.activity_main);

        /*
        Connect the fragment adapter to the pageview
         */
        List<Fragment> fragments = getFragments();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        Adapter_Fragments adapter_fragments = new Adapter_Fragments(getSupportFragmentManager(), fragments, pager);
        pager.setAdapter(adapter_fragments);
        pager.setCurrentItem(1);


    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_Live_Control(this, ctrlHandler));
        fragments.add(new Fragment_Path_Control(this, ctrlHandler));
        Fragment_Infos infos = new Fragment_Infos();
        infos.setMain(this);
        fragments.add(infos);
        fragments.add(new Fragment_Graphs(this));

        return fragments;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
