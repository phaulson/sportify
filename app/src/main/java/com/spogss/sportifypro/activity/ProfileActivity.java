package com.spogss.sportifypro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.Database;
import com.spogss.sportifypro.data.SportifyClient;
import com.spogss.sportifypro.data.pojo.ProUser;
import com.spogss.sportifypro.data.pojo.User;
import com.spogss.sportifypro.gmodel.ListMenuModel;
import com.spogss.sportifypro.gmodel.ListMenuModelAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textView_description;

    private ListView listView_menu;
    private final int V_ID_MENUITEM_SHOW_LOCATIONS = 2508;
    private final int V_ID_MENUITEM_SHOW_PLANS = 2509;

    private final int REQ_CODE_EDIT_PROFILE = 30;

    private FloatingActionButton fab_editOrFollow;

    private SportifyClient client;
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

        client = SportifyClient.newInstance();

        Integer userId = (Integer) getIntent().getSerializableExtra("profile");

        new setUserTask().execute(userId);
    }

    private void initialize(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        textView_description = (TextView) findViewById(R.id.textView_description);
        listView_menu = (ListView) findViewById(R.id.listView_profileMenu);
        fab_editOrFollow = (FloatingActionButton) findViewById(R.id.fab_editProfile);
        fab_editOrFollow.setOnClickListener(this);
    }

    private void setUser(User u){
        displayedUser = u;
        refreshProfile();
        refreshCorellationState();
        refresh_fab_editOrFollow();
    }

    private void refreshProfile(){
        collapsingToolbar.setTitle(displayedUser.getUsername());
        String bio = displayedUser.getDescription();
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

        ListMenuModel m = new ListMenuModel("Group Title");
        models.add(m);

        m = new ListMenuModel(V_ID_MENUITEM_SHOW_LOCATIONS,
                android.R.drawable.ic_menu_mylocation,"Locations",">>");
        m.setOnClickListener(this);
        models.add(m);

        m = new ListMenuModel(V_ID_MENUITEM_SHOW_PLANS,
                android.R.drawable.ic_menu_my_calendar,"Plans",">>");
        m.setOnClickListener(this);
        models.add(m);

        return models;
    }

    private boolean isUserPro(User u){
        // TODO: 27.03.2018 actual code
        return u instanceof ProUser;
    }

    private void refreshCorellationState(){
        if(displayedUser.getId() == client.getCurrentUserID()){
            correlationState = UserCorrelation.OWN;
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_editProfile:
                switch(correlationState){
                    case NOT_FOLLOWING:
                        // TODO: 09.04.2018 follow user 
                        break;
                    case FOLLOWING:
                        // TODO: 09.04.2018 unfollow user 
                        break;
                    case OWN:
                        editProfile();
                        break;
                }
                break;
            case V_ID_MENUITEM_SHOW_LOCATIONS:
                Toast.makeText(getApplicationContext(), "show locations", Toast.LENGTH_SHORT).show();
                break;
            case V_ID_MENUITEM_SHOW_PLANS:
                Toast.makeText(getApplicationContext(), "show plans", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    private void editProfile(){
        Intent intent_editProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
        intent_editProfile.putExtra("profile", displayedUser);
        startActivityForResult(intent_editProfile, REQ_CODE_EDIT_PROFILE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_EDIT_PROFILE) {
            refreshProfile();
        }
    }

    private class setUserTask extends AsyncTask<Integer, Void, User>{

        @Override
        protected User doInBackground(Integer... integers) {
            User u = client.getProfile(integers[0]);
            return u;
        }

        @Override
        protected void onPostExecute(User user) {
            setUser(user);
        }
    }
}
