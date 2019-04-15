package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.spogss.sportifycommunity.activity.PlanActivity;
import com.spogss.sportifycommunity.activity.ProfileActivity;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 27.03.2018.
 */

public class SearchListAdapter<T> extends BaseAdapter implements View.OnTouchListener {
    private Context context;

    private HashMap<Integer, T> content = new HashMap<Integer, T>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public SearchListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int i) {
        return content.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return keys.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_users, null);

        //get UI controls
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relativeLayout_users);
        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_users_profile);
        TextView username = (TextView)view.findViewById(R.id.textView_users_username);

        Object o = content.get(keys.get(i));
        rl.setTag(keys.get(i));
        if(o instanceof User) {
            username.setText(((User) o).getUsername());
            //TODO: implement with real profile pictures
            profilePic.setImageResource(R.drawable.sp_default_profile_picture);
        }
        else if(o instanceof PlanModel) {
            username.setText(((PlanModel) o).getPlan().getName());
            profilePic.setImageResource(R.drawable.sp_plan);
        }

        //add eventListeners
        rl.setOnTouchListener(this);

        return view;
    }

    /**
     * adds a user to the Adapter
     * @param generic the user that should be added
     */
    public void addGeneric(T generic, int id) {
        content.put(id, generic);
        keys.add(id);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            int idRl = Integer.parseInt(view.getTag().toString());
            T o = content.get(idRl);
            if(o instanceof User) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("profile", ((User)o).getId());
                context.startActivity(intent);
            }
            else if(o instanceof PlanModel) {
                Intent intent = new Intent(context, PlanActivity.class);
                intent.putExtra("plan", (PlanModel)o);
                context.startActivity(intent);
            }
        }
        return true;
    }
}
