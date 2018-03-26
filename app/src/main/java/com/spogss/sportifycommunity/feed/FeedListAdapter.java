package com.spogss.sportifycommunity.feed;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;

import java.util.ArrayList;

/**
 * Created by Pauli on 26.03.2018.
 */

public class FeedListAdapter extends BaseAdapter {
    private Context context;

    // TODO: implement with real posts
    private ArrayList<Post> posts = new ArrayList<Post>();

    public FeedListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return posts.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_feed, null);


        TextView username = (TextView)view.findViewById(R.id.textView_feed_username);
        TextView timeStamp = (TextView)view.findViewById(R.id.textView_feed_timeStamp);
        TextView caption = (TextView)view.findViewById(R.id.textView_feed_caption);

        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_feed_profile);
        ImageView postPic = (ImageView)view.findViewById(R.id.imageView_feed_post);
        ImageView heart = (ImageView)view.findViewById(R.id.imageView_feed_heart);
        ImageView comment = (ImageView)view.findViewById(R.id.imageView_feed_comment);


        // TODO: implement with real posts
        username.setText(posts.get(i).getUsername());
        timeStamp.setText(posts.get(i).getTimeStamp());
        caption.setText(posts.get(i).getCaption());

        profilePic.setImageResource(posts.get(i).getProfilePic());
        postPic.setImageResource(posts.get(i).getPostPic());
        if(posts.get(i).isLiked())
            heart.setImageResource(R.drawable.sp_heart_filled);
        else
            heart.setImageResource(R.drawable.sp_heart_blank);
        comment.setImageResource(R.drawable.sp_comment);

        return view;
    }

    /**
     * adds a new Post to the Adapter
     * @param post the Post that should be added
     */
    // TODO: implement with real posts
    public void addPost(Post post) {
        posts.add(post);
    }
}
