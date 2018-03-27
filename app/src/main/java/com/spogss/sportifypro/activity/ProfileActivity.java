package com.spogss.sportifypro.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ListView;
import android.widget.TextView;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.User;
import com.spogss.sportifypro.gmodel.ListMenuModel;
import com.spogss.sportifypro.gmodel.ListMenuModelAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textView_description;
    private ListView listView_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        User user = (User) getIntent().getSerializableExtra("profile");
        setProfile(user);

    }

    private void initialize(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        textView_description = (TextView) findViewById(R.id.textView_description);
        listView_menu = (ListView) findViewById(R.id.listView_profileMenu);
    }

    private void setProfile(User u){
        collapsingToolbar.setTitle(u.getUsername());
        String bio = u.getBiography();
        bio = (bio == null || bio.isEmpty()) ? getString(R.string.not_specified) : bio;
        Log.i("bio:" , bio);
        textView_description.setText(bio);
        if(isUserPro(u)){
            listView_menu.setVisibility(View.VISIBLE);
            setListMenu();
        }
        else{
            listView_menu.setVisibility(View.GONE);
        }
    }

    private void setListMenu(){

        // if extending Activity
        //setContentView(R.layout.activity_main);

        // 1. pass context and data to the custom adapter
        ListMenuModelAdapter adapter = new ListMenuModelAdapter(getApplicationContext(), generateMenuEntries());

        // if extending Activity 2. Get ListView from activity_main.xml
        //ListView listView = (ListView) findViewById(R.id.listview);

        // 3. setListAdapter
        listView_menu.setAdapter(adapter); // if extending Activity
        //ListMenuModelAdapter(adapter); // if extendint ListActivity
    }

    private ArrayList<ListMenuModel> generateMenuEntries(){
        ArrayList<ListMenuModel> models = new ArrayList<ListMenuModel>();

        models.add(new ListMenuModel("Group Title"));
        models.add(new ListMenuModel(android.R.drawable.ic_menu_mylocation,"Locations",">>"));
        models.add(new ListMenuModel(android.R.drawable.ic_menu_my_calendar,"Plans",">>"));
        models.add(new ListMenuModel(android.R.drawable.ic_menu_camera,"dunno",">>"));

        return models;
    }

    private boolean isUserPro(User u){
        // TODO: 27.03.2018 actual code
        return true;
    }
}
