package edu.ucsb.cs.cs185.jazariethach.cameraroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiTouch extends ActionBarActivity {
    //MultiTouch imageview;
    public static TouchView imageview;
    public static Drawable d;
    Bitmap bmap;
    public static Matrix matrix;
    public static Matrix savedMatrix;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d_idk = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    public static float mFocusX = 0.f;
    public static float mFocusY = 0.f;

    // float scale = 1f;
    ScaleGestureDetector SGD;

    @Override/**/
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageview = (TouchView) findViewById(R.id.launchedPic);
//        imageview = (MultiTouch) findViewById(R.id.launchedPic);
       // SGD = new ScaleGestureDetector(this,new ScaleListener());
        //bitmap factory decode function
        //setimagebitmap

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Uri uri = Uri.parse(extra.getString("imageUri"));

            imageview.setImageURI(uri);
          /*  File myFile = new File(uri.getPath());
            d = Drawable.createFromPath(myFile.getAbsolutePath());
           */
            matrix = new Matrix();
            savedMatrix = new Matrix();
          /*  Display display = getWindowManager().getDefaultDisplay();
            mFocusX = display.getWidth()/2f;
            mFocusY = display.getHeight()/2f;
            */
            //imageview.setImageDrawable(getResources().getDrawable(R.drawable.launchedPic));
          //  imageview.buildDrawingCache();
           // bmap = Bitmap.createBitmap(imageview.getDrawingCache());
            //imageview.buildDrawingCache(true);
            //bmap = Bitmap.createBitmap(imageview.getDrawingCache());
           // imageview.setImageBitmap(bmap);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.load) {
            load();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void load(){

    }

  public static class TouchView extends ImageView {
      private Drawable mIcon;
      private float x = 0;
      private float y = 0;


      Bitmap bImage;
      int height;
      int width;
      Paint paint;
      GestureDetector gestures;
      ScaleGestureDetector scaleGesture;
      private int mActivePointerId;
      private List<Point> circlePoints;
      private ScaleGestureDetector mScaleDetector;
      private float mScaleFactor = .4f;
      private int mImageHeight, mImageWidth;


      public TouchView(Context context) {
        super(context);
        //Bitmap bm=((BitmapDrawable)this.getDrawable()).getBitmap();
    }

    public TouchView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        //Bitmap bm=((BitmapDrawable)this.getDrawable()).getBitmap();
    }


      public TouchView(Context context, AttributeSet attrs) {

          super(context, attrs);
          paint = new Paint();
          paint.setStrokeWidth(10);
          paint.setColor(Color.RED);
          paint.setStyle(Paint.Style.STROKE);
          circlePoints = new ArrayList<Point>();

       /*   mImageHeight 	= d.getIntrinsicHeight();
          mImageWidth 	= d.getIntrinsicWidth();
          float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
          float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;

          matrix.postScale(mScaleFactor, mScaleFactor);
          matrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
          imageview.setImageMatrix(matrix);
          */
          // this.buildDrawingCache();//How to get bitmap or view????
          //bImage = this.getDrawingCache();

          //Bitmap bm=((BitmapDrawable)this.getDrawable()).getBitmap();
          //   Bitmap bitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(), Bitmap.Config.RGB_565);
          //  Canvas canvas = new Canvas(bitmap);
          //this.draw(canvas);
      }

      @Override
      public void onDraw(Canvas canvas) {
          super.onDraw(canvas);
          height = this.getMeasuredHeight();
          width = this.getMeasuredWidth();
          //canvas.setBitmap(bImage);
          for (Point p : circlePoints) {
              canvas.drawCircle(p.x, p.y, 2, paint);
          }
      }

      @Override
      public boolean onTouchEvent( MotionEvent event) {
     /*     mScaleDetector.onTouchEvent(event);
          mActivePointerId = event.getPointerId(0); // 0 == pointer index
          float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
          float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;

          matrix.reset();
          matrix.postScale(mScaleFactor, mScaleFactor);*/
          //canvas get matrix
          //postrotate
          //bundle canvas  state, save and restore
          //restore

          if (event.getAction() == MotionEvent.ACTION_DOWN) {
              x = event.getX();
              y = event.getY();
              circlePoints.add(new Point(Math.round(x), Math.round(y)));

              invalidate();
          }
          return false;

      }
      private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
          @Override
          public boolean onScale(ScaleGestureDetector detector) {
              mScaleFactor *= detector.getScaleFactor(); // scale change since previous event

              // Don't let the object get too small or too large.
              mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

              return true;
          }
      }


/*

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
        }
    } */
  }
}
