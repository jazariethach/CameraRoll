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
import android.util.FloatMath;
import android.view.Display;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MultiTouch extends ActionBarActivity {
    public static TouchView imageview;
    public static Drawable d;
    public static Matrix matrix;
    public static Matrix savedMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_picture);

        imageview = new TouchView(this);
        imageview.setScaleType(ImageView.ScaleType.MATRIX);
        setContentView(imageview);

        //  imageview = (TouchView) findViewById(R.id.launchedPic);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Uri uri = Uri.parse(extra.getString("imageUri"));

            File myFile = new File(uri.getPath());
            d = Drawable.createFromPath(myFile.getAbsolutePath());

            matrix = new Matrix();
            savedMatrix = new Matrix();
            Display display = getWindowManager().getDefaultDisplay();

            imageview.setImageURI(uri);

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
      private float x = 0;
      private float y = 0;
      Bitmap bmap;
      int height;
      int width;
      Paint paint;
      private int mActivePointerId;
      private List<Point> dots;
      private ScaleGestureDetector mScaleDetector;
      private float mScaleFactor = .4f;
      private int mImageHeight, mImageWidth;
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
      private static final int MAX_CLICK_DURATION = 100;
      private long startClickTime;

      public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        // paint.setARGB(150,255,0,0);
        paint.setStyle(Paint.Style.STROKE);
        dots = new ArrayList<Point>();
    }


      public TouchView(Context context, AttributeSet attrs) {

          this(context, attrs, 0);
      }

      @Override
      public void onDraw(Canvas canvas) {
          super.onDraw(canvas);
          height = this.getMeasuredHeight();
          width = this.getMeasuredWidth();
          //canvas.setBitmap(bImage);
          for (Point p : dots) {
              canvas.drawCircle(p.x, p.y, 2, paint);
          }
      }
//launchmode single instance
      @Override
      public boolean onTouchEvent( MotionEvent event) {
          //canvas get matrix
          //postrotate
          //bundle canvas  state, save and restore
          //restore
          switch (event.getAction() & MotionEvent.ACTION_MASK) {
              case MotionEvent.ACTION_DOWN:
                  startClickTime = Calendar.getInstance().getTimeInMillis();
                  savedMatrix.set(matrix);
                  start.set(event.getX(), event.getY());
                  mode = DRAG;
                  lastEvent = null;
                  break;
              case MotionEvent.ACTION_POINTER_DOWN:
                  oldDist = spacing(event);
                  if (oldDist > 10f) {
                      savedMatrix.set(matrix);
                      midPoint(mid, event);
                      mode = ZOOM;
                  }
                  lastEvent = new float[4];
                  lastEvent[0] = event.getX(0);
                  lastEvent[1] = event.getX(1);
                  lastEvent[2] = event.getY(0);
                  lastEvent[3] = event.getY(1);
                  d_idk = rotation(event);
                  break;
              case MotionEvent.ACTION_UP:
                  long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                  if(clickDuration < MAX_CLICK_DURATION) {
                      x = event.getX();
                      y = event.getY();
                      //if statement to check width and height of image
                      dots.add(new Point(Math.round(x), Math.round(y)));
                      invalidate();
                  }
                  break;
              case MotionEvent.ACTION_POINTER_UP:
                  mode = NONE;
                  lastEvent = null;
                  break;
              case MotionEvent.ACTION_MOVE:
                  if (mode == DRAG) {
                      matrix.set(savedMatrix);
                      float dx = event.getX() - start.x;
                      float dy = event.getY() - start.y;
                      matrix.postTranslate(dx, dy);
                  } else if (mode == ZOOM) {
                      float newDist = spacing(event);
                      if (newDist > 10f) {
                          matrix.set(savedMatrix);
                          float scale = (newDist / oldDist);
                          matrix.postScale(scale, scale, mid.x, mid.y);
                      }
                      if (lastEvent != null && event.getPointerCount() == 2) {
                          newRot = rotation(event);
                          float r = newRot - d_idk;
                          float[] values = new float[9];
                          matrix.getValues(values);
                          float tx = values[2];
                          float ty = values[5];
                          float sx = values[0];
                          float xc = (imageview.getWidth() / 2) * sx;
                          float yc = (imageview.getHeight() / 2) * sx;
                          matrix.postRotate(r, tx + xc, ty + yc);
                      }
                  }
                  break;
          }

          imageview.setImageMatrix(matrix);
          return true;
      }

      /**
       * Determine the space between the first two fingers
       */
      private float spacing(MotionEvent event) {
          float x = event.getX(0) - event.getX(1);
          float y = event.getY(0) - event.getY(1);
          return FloatMath.sqrt(x * x + y * y);
      }

      /**
       * Calculate the mid point of the first two fingers
       */
      private void midPoint(PointF point, MotionEvent event) {
          float x = event.getX(0) + event.getX(1);
          float y = event.getY(0) + event.getY(1);
          point.set(x / 2, y / 2);
      }

      /**
       * Calculate the degree to be rotated by.
       *
       * @param event
       * @return Degrees
       */
      private float rotation(MotionEvent event) {
          double delta_x = (event.getX(0) - event.getX(1));
          double delta_y = (event.getY(0) - event.getY(1));
          double radians = Math.atan2(delta_y, delta_x);
          return (float) Math.toDegrees(radians);
      }

  }
}
