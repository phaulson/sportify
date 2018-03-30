package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.Post;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 26.03.2018.
 */

public class FeedListAdapter extends BaseAdapter implements View.OnClickListener, View.OnTouchListener {
    private Context context;

    // TODO: implement with real posts
    private HashMap<Integer, Post> posts = new HashMap<Integer, Post>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public FeedListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return posts.get(keys.get(i)).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_feed, null);

        //get UI controls
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relativeLayout_feed);
        RelativeLayout rlHeader = (RelativeLayout)view.findViewById(R.id.relativeLayout_feed_header);

        TextView username = (TextView)view.findViewById(R.id.textView_feed_username);
        TextView timeStamp = (TextView)view.findViewById(R.id.textView_feed_timeStamp);
        TextView caption = (TextView)view.findViewById(R.id.textView_feed_caption);
        TextView likes = (TextView)view.findViewById(R.id.textView_feed_likes);

        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_feed_profile);
        ImageView postPic = (ImageView)view.findViewById(R.id.imageView_feed_post);
        ImageView heart = (ImageView)view.findViewById(R.id.imageView_feed_heart);
        ImageView comment = (ImageView)view.findViewById(R.id.imageView_feed_comment);


        // TODO: implement with real posts
        Post post = posts.get(keys.get(i));
        rl.setTag(post.getId());

        username.setText(post.getUser().getUsername());
        timeStamp.setText(post.getTimeStamp());
        caption.setText(post.getCaption());
        likes.setText(post.getLikes() + " like" + (post.getLikes() != 1 ? "s": ""));

        profilePic.setImageResource(post.getUser().getProfilePic());

        //check if post has pic
        if(post.getPostPic() != -1)
            postPic.setImageResource(post.getPostPic());
        else {
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.relativeLayout_feed_header);
            params.addRule(RelativeLayout.ALIGN_START, R.id.imageView_feed_post);
            caption.setLayoutParams(params);
        }

        //check if post is liked
        if(post.isLiked())
            heart.setImageResource(R.drawable.sp_heart_filled);
        else
            heart.setImageResource(R.drawable.sp_heart_blank);

        comment.setImageResource(R.drawable.sp_comment);


        //add eventListeners
        heart.setOnClickListener(this);
        comment.setOnClickListener(this);

        //double tap event for post
        final GestureDetector detector = new GestureDetector(context, new GestureListener(postPic));
        postPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });

        //touch event for header
        rlHeader.setOnTouchListener(this);

        return view;
    }

    /**
     * adds a new Post to the Adapter
     * @param post the Post that should be added
     */
    // TODO: implement with real posts
    public void addPost(Post post) {
        posts.put(post.getId(), post);
        keys.add(post.getId());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // TODO: implement real onClick logic
        if(id == R.id.imageView_feed_heart) {
            like(view);
        }
        else if(id == R.id.imageView_feed_comment) {
            Snackbar.make(view, "The CommentActivity will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * if the user has already liked this post, the heart turns blank and the number of likes is decremented
     * otherwise, the heart turns red and the number of likes is incremented
     */
    private void like(View view) {
        RelativeLayout rl = (RelativeLayout)view.getParent();
        int idRl = Integer.parseInt(rl.getTag().toString());
        Post post = posts.get(idRl);

        if(post.isLiked()) {
            post.setLiked(false);
            post.setLikes(post.getLikes() - 1);
            ImageView heart = (ImageView) rl.findViewById(R.id.imageView_feed_heart);
            heart.setImageResource(R.drawable.sp_heart_blank);
        } else {
            post.setLiked(true);
            post.setLikes(post.getLikes() + 1);
            ImageView heart = (ImageView) rl.findViewById(R.id.imageView_feed_heart);
            heart.setImageResource(R.drawable.sp_heart_filled);
        }
        TextView likes = (TextView) rl.findViewById(R.id.textView_feed_likes);
        likes.setText(post.getLikes() + " like" + (post.getLikes() != 1 ? "s": ""));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == motionEvent.ACTION_UP) {
            // TODO: open the ProfileActivity
            RelativeLayout rl = (RelativeLayout) view.getParent();
            int idRl = Integer.parseInt(rl.getTag().toString());
            Post post = posts.get(idRl);
            Snackbar.make(view, "The ProfileActivity for '" + post.getUser().getUsername() + "' will be implemented soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return true;
    }


    /**
     * the listener that handles the double tap event
     */
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private View view;

        public GestureListener(View view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            like(view);
            return true;
        }
    }
}
