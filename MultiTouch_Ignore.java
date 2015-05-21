package edu.ucsb.cs.cs185.jazariethach.cameraroll;

/**
 * Created by Jazarie on 5/17/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jazarie on 5/14/2015.
 */
public class MultiTouch_Ignore extends ImageView {
    private Drawable mIcon;
    private float x = 0;
    private float y = 0;

    private float mLastTouchX;
    private float mLastTouchY;
    Matrix matrix;
    Bitmap bImage;
    int height;
    int width;
    Paint paint;
    GestureDetector gestures;
    ScaleGestureDetector scaleGesture;
    private int mActivePointerId;
    private List<Point> circlePoints;



    /*public MultiTouch(Context context) {
        super(context);
        //Bitmap bm=((BitmapDrawable)this.getDrawable()).getBitmap();


    }*/


/*    public MultiTouch(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        //Bitmap bm=((BitmapDrawable)this.getDrawable()).getBitmap();

    }
*/

    public MultiTouch_Ignore(Context context, AttributeSet attrs) {

        super(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.STROKE);
        circlePoints = new ArrayList<Point>();


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
            //canvas.drawCircle(x, y, 2, paint);
            //canvas.save();
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mActivePointerId = event.getPointerId(0); // 0 == pointer index

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
