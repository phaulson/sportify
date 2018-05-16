package com.spogss.sportifycommunity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.Connection.SportifyClient;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView postPic;
    private EditText caption;
    private ImageButton removePic;
    private SportifyClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        Button buttonAddPic = (Button) findViewById(R.id.button_addPic);
        buttonAddPic.setOnClickListener(this);

        postPic = (ImageView) findViewById(R.id.imageView_post_post);
        postPic.setVisibility(View.GONE);
        caption = (EditText) findViewById(R.id.editText_post_caption);

        removePic = (ImageButton) findViewById(R.id.imageButton_post_removePic);
        removePic.setOnClickListener(this);
        removePic.setVisibility(View.GONE);

        client = SportifyClient.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
                case R.id.action_post:
                    //TODO: check if caption and picture not set
                    if (caption.getText().toString().trim().equals(""))
                        throw new Exception("Please write a caption or post a pic");
                    new AddPostTask().execute((Void) null);
                    Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_addPic:
                showPicDialog();
                break;
            case R.id.imageButton_post_removePic:
                postPic.setImageDrawable(null);
                postPic.setTag(null);
                postPic.setVisibility(View.GONE);
                removePic.setVisibility(View.GONE);
                break;
        }
    }

    private void showPicDialog() {
        //TODO: add icons for camera and gallery
        CharSequence options[] = new CharSequence[]{"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Take picture from camera or gallery?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent takePicture = null;
                if (which == 0)
                    takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                else if (which == 1)
                    takePicture = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(takePicture, which);
            }
        });
        builder.setNegativeButton(getString(R.string.action_post_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    postPic.setImageBitmap(bitmap);
                    break;
                case 1:
                    Uri selectedImage = imageReturnedIntent.getData();
                    postPic.setImageURI(selectedImage);
                    break;
            }
            postPic.setTag("added");
            postPic.setVisibility(View.VISIBLE);
            removePic.setVisibility(View.VISIBLE);
        }
    }

    //AsyncTask
    private class AddPostTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return client.addPost(client.getCurrentUserID(), caption.getText().toString());
        }
    }
}
