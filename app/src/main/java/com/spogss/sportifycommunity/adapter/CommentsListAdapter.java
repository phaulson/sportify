package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.activity.ProfileActivity;
import com.spogss.sportifycommunity.data.Comment;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.CommentModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Pauli on 13.06.2018.
 */

public class CommentsListAdapter extends BaseAdapter implements View.OnTouchListener {
    private Context context;

    private HashMap<Integer, CommentModel> content = new HashMap<Integer, CommentModel>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public CommentsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(keys.get(position));
    }

    @Override
    public long getItemId(int position) {
        return content.get(keys.get(position)).getComment().getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroupt) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_comment, null);


        //get UI controls
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relativeLayout_comments);
        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_comment_profile);
        TextView name = (TextView)view.findViewById(R.id.textView_comment_username);
        ReadMoreTextView text = (ReadMoreTextView) view.findViewById(R.id.textView_comment_text);
        TextView timeStamp = (TextView)view.findViewById(R.id.textView_comment_timeStamp);

        CommentModel model = content.get(keys.get(i));
        rl.setTag(model.getComment().getId());

        name.setText(model.getUser().getUsername());
        text.setText(model.getComment().getText());
        timeStamp.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(model.getComment().getTimestamp()).toString());

        //TODO: implement with real plan pictures
        profilePic.setImageResource(R.drawable.sp_default_profile_picture);


        //add eventListeners
        rl.setOnTouchListener(this);

        return view;
    }

    /**
     * add new comments to adapter
     * @param c
     */
    public void addComments(Collection<CommentModel> c) {
        for(CommentModel cm : c) {
            content.put(cm.getComment().getId(), cm);
            keys.add(cm.getComment().getId());
        }
        notifyDataSetChanged();
    }

    /**
     * clears the adapter
     */
    public void clear() {
        content.clear();
        keys.clear();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            int idRl = Integer.parseInt(view.getTag().toString());
            CommentModel commentModel = content.get(idRl);

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profile", commentModel.getUser().getId());
            context.startActivity(intent);
        }
        return true;
    }
}
