package pl.krzysiakg.listazakopow;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment {

    public static final int ADD = 1;
    public static final int MODIFY = 2;
    public List<ListItem> Items = new ArrayList<>();
    public RecyclerView recyclerView;
    public ListAdapter lAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ((MainActivity) getActivity()).lunchSettings();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditItem.class);
                intent.putExtra("requestCode", MainFragment.ADD);
                getActivity().startActivityForResult(intent, MainFragment.ADD);
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        lAdapter = new ListAdapter(Items);
        lAdapter.mainActivity = (MainActivity) getActivity();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lAdapter);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean sort = sharedPref.getBoolean("isSort", false);
        if(sort)
            Collections.sort(Items, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem o1, ListItem o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        lAdapter.notifyDataSetChanged();

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                final ListItem li = new ListItem(result);
                Items.add(li);
                lAdapter.notifyDataSetChanged();

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ((MainActivity)getActivity()).db.listItemDao().insert(li);
                        return null;
                    }
                }.execute();
            }
        }
        else if (requestCode == MODIFY){
            if(resultCode == RESULT_OK){
                String name = data.getStringExtra("result");
                int number = data.getIntExtra("number", -1);
                final ListItem li = Items.get(number);
                li.setName(name);
                lAdapter.notifyDataSetChanged();

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ((MainActivity)getActivity()).db.listItemDao().update(li);
                        return null;
                    }
                }.execute();
            }
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean sort = sharedPref.getBoolean("isSort", false);
        if(sort)
            Collections.sort(Items, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem o1, ListItem o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        lAdapter.notifyDataSetChanged();
    }
}
