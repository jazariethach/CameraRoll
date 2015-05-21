package edu.ucsb.cs.cs185.jazariethach.cameraroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayoutManager lm;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preferenceEditor;
    public static File folder;
    Context context;
    TextView no_photos;
    ArrayList<File> picList;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        picList = new ArrayList<File>();


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        lm = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(lm);
        mAdapter = new MyAdapter(context, picList);
        mRecyclerView.setAdapter(mAdapter);
        folder = new File(this.getExternalFilesDir(null) + "/CS185Pics", "CS185Pics");
        if (!folder.exists()) {
          folder.mkdir();
        }

        getCount();
        no_photos = (TextView) findViewById(R.id.no_photos);

        mRecyclerView.setHasFixedSize(true);


        makePicList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void takePic(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            getCount();

            File image = null;
            String n = String.format("%03d",count);
            String imageName = "photo-"+n+".jpg";
            image = new File(folder,imageName);

            if (image != null) {
                Uri uriSavedImage = Uri.fromFile(image);
               // Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //mediaScanIntent.setData(uriSavedImage);
                //sendBroadcast(mediaScanIntent);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                makePicList();
                mAdapter.updateList(picList);
                mRecyclerView.setVisibility(View.VISIBLE);
                no_photos.setVisibility(View.GONE);
                incCount(); //count++
            } else if(resultCode != RESULT_OK){
                //something went wrong
                //delete photo?
            }
        }
    }

    public void deleteAll(){ // need to delete from memory
 //       clearPicList();
        picList.clear();
        File[] pics = folder.listFiles();
        if(pics!=null) {
            if (pics.length > 0) {
                for (int i = 0; i < pics.length; i++) {
                    pics[i].delete();
                }
                no_photos.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void getCount(){
        preferences = getSharedPreferences("tag", 0);
//        preferenceEditor = preferences.edit();
        count = preferences.getInt("count", 1);
    }

    private void incCount(){
        count++;
        preferenceEditor = preferences.edit();
        preferenceEditor.putInt("count", count);
        preferenceEditor.apply();
    }

    private void makePicList(){
   //     clearPicList();
        picList.clear();
        File[] pics = folder.listFiles();
        if(pics!=null) {
            int idx = pics.length -1;
            if (pics.length > 0) {
                no_photos.setVisibility(View.GONE);
                for (int i = 0; i < pics.length; i++) {
                    picList.add(pics[idx - i]);
                }
            }
        }
    }

 /*   public void clearPicList(){
        Iterator it = picList.iterator();
        while(it.hasNext()){
            it.remove();
        }

    }*/
}

