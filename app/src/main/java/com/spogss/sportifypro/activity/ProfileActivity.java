package com.spogss.sportifypro.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.Database;
import com.spogss.sportifypro.data.User;
import com.spogss.sportifypro.gmodel.ListMenuModel;
import com.spogss.sportifypro.gmodel.ListMenuModelAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textView_description;
    private ListView listView_menu;
    private FloatingActionButton fab_editOrFollow;

    private Database database;
    private User displayedUser;

    private enum UserCorrelation {
        NOT_FOLLOWING,
        FOLLOWING,
        OWN
    }
    private UserCorrelation correlationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        database = Database.getInstance();

        User user = (User) getIntent().getSerializableExtra("profile");
        setUser(user);
    }

    private void initialize(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        textView_description = (TextView) findViewById(R.id.textView_description);
        listView_menu = (ListView) findViewById(R.id.listView_profileMenu);
        fab_editOrFollow = (FloatingActionButton) findViewById(R.id.fab_editProfile);
    }

    private void setUser(User u){
        displayedUser = u;
        refreshProfile();
        refreshCorellationState();
        refresh_fab_editOrFollow();
    }

    private void refreshProfile(){
        collapsingToolbar.setTitle(displayedUser.getUsername());
        String bio = displayedUser.getBiography();
        bio = (bio == null || bio.isEmpty()) ? getString(R.string.not_specified) : bio;
        Log.i("bio:" , bio);
        textView_description.setText(bio);
        if(isUserPro(displayedUser)){
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
        //ListMenuModelAdapter(adapter); // if extending ListActivity
        //setListViewHeightBasedOnChildren(listView_menu); // try

        //listView_menu.setOnItemSelectedListener(this);
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
        return false;
    }

    private void refreshCorellationState(){
        if(displayedUser.getId() == database.getCurrentUserId()){
            correlationState = UserCorrelation.OWN;
        }
        else{
            if(isUserFollowed(displayedUser)){
                correlationState = UserCorrelation.FOLLOWING;
            }
            else{
                correlationState = UserCorrelation.NOT_FOLLOWING;
            }
        }
    }

    private boolean isUserFollowed(User u){
        // TODO: 28.03.2018 implement ws call
        return false;
    }

    private void refresh_fab_editOrFollow(){
        switch(correlationState){
            case NOT_FOLLOWING:
                fab_editOrFollow.setImageResource(android.R.drawable.ic_menu_add);
                break;
            case FOLLOWING:
                fab_editOrFollow.setImageResource(android.R.drawable.ic_menu_delete);
                break;
            case OWN:
                fab_editOrFollow.setImageResource(android.R.drawable.ic_menu_edit);
                break;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
