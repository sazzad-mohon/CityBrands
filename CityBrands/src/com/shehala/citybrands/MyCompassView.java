package com.shehala.citybrands;

import com.shehala.citybrands.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MyCompassView extends View {
private float mGraduationRotateAngle = 0;
	private float mPointerRotateAngle = 0;

	private Drawable mGraduationDrawable = null;
	private Drawable mPointerDrawable = null;

	private float mGraduationDrawableRadius = 0;
	private float mPointerDrawableRadius = 0;

	private float mCanvasCenterX = 0;
	private float mCanvasCenterY = 0;

	private float mPreTouchX = 0;
	private float mPreTouchY = 0;

	public MyCompassView(Context context) {
		super(context);

//		Resources res = context.getResources();
//		mGraduationDrawable = res.getDrawable(R.drawable.compass_image);
//		mGraduationDrawable.setBounds(0, 0, 300, 300);
//		mPointerDrawable = res.getDrawable(R.drawable.compass_needle);
//		mPointerDrawable.setBounds(0, 0, 190, 190);
//
//		mGraduationDrawableRadius = 300 / 2;
//		mPointerDrawableRadius = 190 / 2;
		
		
//		
//		Resources res = context.getResources();
//		mGraduationDrawable = res.getDrawable(R.drawable.compass_image);
//		mGraduationDrawable.setBounds(0, 20, 240, 240);
//		mPointerDrawable = res.getDrawable(R.drawable.compass_needle);
//		mPointerDrawable.setBounds(0, 20, 140, 140);
//
//		mGraduationDrawableRadius = 240 / 2;
//		mPointerDrawableRadius = 140 / 2;
		
		 init(context);
	}

	public MyCompassView(Context context,AttributeSet attr) {
		super(context,attr,0);
		 init(context);
	}
	
	public MyCompassView(Context context,AttributeSet attr, int defStyle) {
		super(context,attr,defStyle);
		 init(context);
	}
	
	
	private void init(Context context) {
	    //do stuff that was in your original constructor...
		
		Resources res = context.getResources();
		mGraduationDrawable = res.getDrawable(R.drawable.compass_image);
		mGraduationDrawable.setBounds(0, 0, 240, 240);
		mPointerDrawable = res.getDrawable(R.drawable.compass_needle);
		mPointerDrawable.setBounds(0, 0, 140, 140);

		mGraduationDrawableRadius = 240 / 2;
		mPointerDrawableRadius = 140 / 2;
	
	}
	
	public void setPointerRotateAngle(float angle) {
		if (angle - mPointerRotateAngle > 2) {
			while (angle - mPointerRotateAngle > 0.2) {
				mPointerRotateAngle += 0.2;
				this.invalidate();
			}
		} else if (angle - mPointerRotateAngle < -2) {
			while (mPointerRotateAngle - angle > 0.2) {
				mPointerRotateAngle -= 0.2;
				this.invalidate();
			}
		}
	}

	public void setGraduationRotateAngle(float angle) {
		mGraduationRotateAngle = angle;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean hasRotation = false;
		boolean isClockwise = true;
		float dx = event.getX() - mPreTouchX;
		float dy = event.getY() - mPreTouchY;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
//			 if (mPreTouchX > mCanvasCenterX && mPreTouchX < mCanvasCenterX +
//			 mGraduationDrawableRadius) {
//			 if (mPreTouchY > mCanvasCenterY && mPreTouchY < mCanvasCenterY +
//			 mGraduationDrawableRadius) {
//			 if (dx > 0 && dy < 0) {isClockwise = true; hasRotation = true;}
//			 else if (dx < 0 && dy > 0) {isClockwise = false; hasRotation =
//			 true;}
//			 else {hasRotation = false;}
//			 }
//			 else if (mPreTouchY > mCanvasCenterY - mGraduationDrawableRadius
//			 && mPreTouchY < mCanvasCenterY){
//			 if (dx > 0 && dy > 0) {isClockwise = false; hasRotation = true;}
//			 else if (dx < 0 && dy < 0) {isClockwise = true; hasRotation =
//			 true;}
//			 else {hasRotation = false;}
//			 }
//			 }
//			 else if (mPreTouchX > mCanvasCenterX - mGraduationDrawableRadius
//			 && mPreTouchX < mCanvasCenterX){
//			 if (mPreTouchY > mCanvasCenterY && mPreTouchY < mCanvasCenterY +
//			 mGraduationDrawableRadius) {
//			 if (dx > 0 && dy > 0) {isClockwise = true; hasRotation = true;}
//			 else if (dx < 0 && dy < 0) {isClockwise = false; hasRotation =
//			 true;}
//			 else {hasRotation = false;}
//			 }
//			 else if (mPreTouchY > mCanvasCenterY - mGraduationDrawableRadius
//			 && mPreTouchY < mCanvasCenterY) {
//			 if (dx > 0 && dy < 0) {isClockwise = false; hasRotation = true;}
//			 else if (dx < 0 && dy > 0) {isClockwise = true; hasRotation =
//			 true;}
//			 else {hasRotation = false;}
//			 }
//			 }
			break;
		}

		if (hasRotation) {
			float touchXToCenterX = mPreTouchX - mCanvasCenterX;
			float touchYToCenterY = mPreTouchY - mCanvasCenterY;
			float touchRadius = (float) Math.sqrt(touchXToCenterX
					* touchXToCenterX + touchYToCenterY * touchYToCenterY);
			float touchPathLen = (float) Math.sqrt(dx * dx + dy * dy);
			float rotateDegree = (float) Math.toDegrees(touchPathLen
					/ touchRadius);
			Log.d("On Touch", "angle: " + rotateDegree);
			if (isClockwise)
				this.setGraduationRotateAngle(mGraduationRotateAngle
						+ rotateDegree);
			else
				this.setGraduationRotateAngle(mGraduationRotateAngle
						- rotateDegree);
			this.invalidate();
		}
		mPreTouchX = event.getX();
		mPreTouchY = event.getY();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int w = canvas.getWidth();
		int h = canvas.getHeight();
		mCanvasCenterX = w / 2;
		mCanvasCenterY = h / 2;

		canvas.save();
		canvas.translate(mCanvasCenterX, mCanvasCenterY);
		canvas.rotate(-mGraduationRotateAngle);
		canvas.translate(-mGraduationDrawableRadius, -mGraduationDrawableRadius);
		mGraduationDrawable.draw(canvas);
		canvas.restore();

		canvas.translate(mCanvasCenterX, mCanvasCenterY);
		canvas.rotate(-mPointerRotateAngle);
		canvas.translate(-mPointerDrawableRadius, -mPointerDrawableRadius);
		mPointerDrawable.draw(canvas);
	}

}
