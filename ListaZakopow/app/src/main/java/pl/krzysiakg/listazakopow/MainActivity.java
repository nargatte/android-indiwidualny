package pl.krzysiakg.listazakopow;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;
    SettingsFragment settingsFragment;
    boolean IsSettings = false;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsFragment = new SettingsFragment();
        mainFragment = MainFragment.newInstance();

        db = Room.databaseBuilder(this, AppDatabase.class, "database").build();

        new AsyncTask<Void, Void, Void>(){

            List<ListItem> Items;

            @Override
            protected Void doInBackground(Void... voids) {
                Items = db.listItemDao().getAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mainFragment.Items.addAll(Items);
                mainFragment.lAdapter.notifyDataSetChanged();
                super.onPostExecute(aVoid);
            }
        }.execute();

        lunchMain();

    }

    public void lunchSettings(){

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, settingsFragment, "mainFragment")
                .commit();

        IsSettings = true;

    }

    public void lunchMain(){

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, mainFragment, "mainFragment")
                .commit();
        IsSettings = false;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(IsSettings == false)
            mainFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(IsSettings){
            lunchMain();
        }
        else {
            super.onBackPressed();
        }
    }
}
