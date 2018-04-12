package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.tempData.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 27.03.2018.
 */

public class SearchListAdapter extends BaseAdapter implements View.OnTouchListener {
    private Context context;

    // TODO: implement with real users
    private HashMap<Integer, User> users = new HashMap<Integer, User>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public SearchListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return users.get(keys.get(i)).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_users, null);

        //get UI controls
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relativeLayout_users);
        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_users_profile);
        TextView username = (TextView)view.findViewById(R.id.textView_users_username);

        // TODO: implement with real users
        User user = users.get(keys.get(i));
        rl.setTag(user.getId());
        username.setText(user.getUsername());
        profilePic.setImageResource(user.getProfilePic());

        //add eventListeners
        rl.setOnTouchListener(this);

        return view;
    }

    /**
     * adds a user to the Adapter
     * @param user the user that should be added
     */
    // TODO: implement with real users
    public void addUser(User user) {
        users.put(user.getId(), user);
        keys.add(user.getId());
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // TODO: open ProfileActivity
            int idRl = Integer.parseInt(view.getTag().toString());
            User user = users.get(idRl);
            Snackbar.make(view, "The ProfileActivity for '" + user.getUsername() + "' will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return true;
    }
}
