package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.spogss.sportifycommunity.activity.ProfileActivity;
import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.SportifyClient;
import com.spogss.sportifycommunity.model.PostModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Pauli on 26.03.2018.
 */

public class FeedListAdapter extends BaseAdapter implements View.OnClickListener, View.OnTouchListener {
    private Context context;
    private SportifyClient client;

    private HashMap<Integer, PostModel> posts = new HashMap<Integer, PostModel>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public FeedListAdapter(Context context) {
        this.context = context;
        this.client = SportifyClient.newInstance();
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
        return posts.get(keys.get(i)).getPost().getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_feed, null);

        //get UI controls
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.relativeLayout_feed);
        RelativeLayout rlHeader = (RelativeLayout) view.findViewById(R.id.relativeLayout_feed_header);

        TextView username = (TextView) view.findViewById(R.id.textView_feed_username);
        TextView timeStamp = (TextView) view.findViewById(R.id.textView_feed_timeStamp);
        TextView caption = (TextView) view.findViewById(R.id.textView_feed_caption);
        TextView likes = (TextView) view.findViewById(R.id.textView_feed_likes);

        ImageView profilePic = (ImageView) view.findViewById(R.id.imageView_feed_profile);
        ImageView postPic = (ImageView) view.findViewById(R.id.imageView_feed_post);
        ImageView heart = (ImageView) view.findViewById(R.id.imageView_feed_heart);
        ImageView comment = (ImageView) view.findViewById(R.id.imageView_feed_comment);


        PostModel postModel = posts.get(keys.get(i));
        rl.setTag(postModel.getPost().getId());

        username.setText(postModel.getUser().getUsername());
        timeStamp.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(postModel.getPost().getTimestamp()).toString());
        caption.setText(postModel.getPost().getCaption());

        likes.setText(postModel.getNumberOfLikes() + " like" + (postModel.getNumberOfLikes() != 1 ? "s": ""));

        //TODO: implement with real profile pics
        profilePic.setImageResource(R.drawable.sp_default_profile_picture);

        //TODO: uncomment once implemented with pics
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) postPic.getLayoutParams();
        //check if post has pic
        //if(post.getPostPic() != -1) {
        //    postPic.setImageResource(post.getPostPic());
        //    marginParams.height = R.dimen.image_post_height;
        //}
        //else {
            marginParams.topMargin = 0;
            marginParams.bottomMargin = 0;
        //}

        //check if post is liked
        if (postModel.isLiked())
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

        Log.i("Post", postModel.getPost().getCaption());

        return view;
    }

    /**
     * adds a new Collection of Posts to the Adapter
     * @param p the Posts that should be added
     */
    public void addPosts(Collection<PostModel> p) {
        for(PostModel post : p) {
            posts.put(post.getPost().getId(), post);
            keys.add(post.getPost().getId());
        }
        this.notifyDataSetChanged();
    }

    public void clear() {
        posts.clear();
        keys.clear();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.imageView_feed_heart) {
            like(view);
        }
        else if(id == R.id.imageView_feed_comment) {
            // TODO: implement comment activity
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
        PostModel postModel = posts.get(idRl);

        ImageView heart = (ImageView) rl.findViewById(R.id.imageView_feed_heart);
        TextView likes = (TextView) rl.findViewById(R.id.textView_feed_likes);

        if(postModel.isLiked()) {
            heart.setImageResource(R.drawable.sp_heart_blank);
            postModel.setNumberOfLikes(postModel.getNumberOfLikes() - 1);
            postModel.setLiked(false);
            new SetLikeTask(postModel.getPost(), false).execute((Void) null);
        }
        else {
            heart.setImageResource(R.drawable.sp_heart_filled);
            postModel.setNumberOfLikes(postModel.getNumberOfLikes() + 1);
            postModel.setLiked(true);
            new SetLikeTask(postModel.getPost(), true).execute((Void) null);
        }
        likes.setText(postModel.getNumberOfLikes() + " like" + (postModel.getNumberOfLikes() != 1 ? "s": ""));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == motionEvent.ACTION_UP) {
            // TODO: open the ProfileActivity
            RelativeLayout rl = (RelativeLayout) view.getParent();
            int idRl = Integer.parseInt(rl.getTag().toString());
            PostModel postModel = posts.get(idRl);

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profile", postModel.getUser().getId());
            context.startActivity(intent);
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


    //AsyncTasks
    private class SetLikeTask extends AsyncTask<Void, Void, Boolean> {
        private Post post;
        private boolean like;

        public SetLikeTask(Post post, boolean like) {
            this.post = post;
            this.like = like;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            return client.setLike(client.getCurrentUserID(), post.getId(), like);
        }
    }
}
