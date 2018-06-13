package com.shehala.citybrands.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;



/**
 * A SeekBar is an extension of ProgressBar that adds a draggable thumb. The user can touch
 * the thumb and drag left or right to set the current progress level or use the arrow keys.
 * Placing focusable widgets to the left or right of a SeekBar is discouraged.
 * <p>
 * Clients of the SeekBar can attach a {@link SeekBar.OnSeekBarChangeListener} to
 * be notified of the user's actions.
 *
 * @attr ref android.R.styleable#SeekBar_thumb
 */
public class VerticalSeekBar extends SeekBar {

//    /**
//     * A callback that notifies clients when the progress level has been
//     * changed. This includes changes that were initiated by the user through a
//     * touch gesture or arrow key/trackball as well as changes that were initiated
//     * programmatically.
//     */
//    public interface OnSeekBarChangeListener {
//
//        /**
//         * Notification that the progress level has changed. Clients can use the fromUser parameter
//         * to distinguish user-initiated changes from those that occurred programmatically.
//         *
//         * @param seekBar The SeekBar whose progress has changed
//         * @param progress The current progress level. This will be in the range 0..max where max
//         *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
//         * @param fromUser True if the progress change was initiated by the user.
//         */
//        void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser);
//
//        /**
//         * Notification that the user has started a touch gesture. Clients may want to use this
//         * to disable advancing the seekbar.
//         * @param seekBar The SeekBar in which the touch gesture began
//         */
//        void onStartTrackingTouch(VerticalSeekBar seekBar);
//
//        /**
//         * Notification that the user has finished a touch gesture. Clients may want to use this
//         * to re-enable advancing the seekbar.
//         * @param seekBar The SeekBar in which the touch gesture began
//         */
//        void onStopTrackingTouch(VerticalSeekBar seekBar);
//    }
//
//    private OnSeekBarChangeListener mOnSeekBarChangeListener;
//
//    public VerticalSeekBar(Context context) {
//        this(context, null);
//    }
//
//    public VerticalSeekBar(Context context, AttributeSet attrs) {
//        this(context, attrs, R.drawable.all_shop_style_progress);
//    }
//
//    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    void onProgressRefresh(float scale, boolean fromUser) {
//        super.onProgressRefresh(scale, fromUser);
//
//        if (mOnSeekBarChangeListener != null) {
//            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
//        }
//    }
//
//    /**
//     * Sets a listener to receive notifications of changes to the SeekBar's progress level. Also
//     * provides notifications of when the user starts and stops a touch gesture within the SeekBar.
//     *
//     * @param l The seek bar notification listener
//     *
//     * @see SeekBar.OnSeekBarChangeListener
//     */
//    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
//        mOnSeekBarChangeListener = l;
//    }
//
//    @Override
//    void onStartTrackingTouch() {
//        if (mOnSeekBarChangeListener != null) {
//            mOnSeekBarChangeListener.onStartTrackingTouch(this);
//        }
//    }
//
//    @Override
//    void onStopTrackingTouch() {
//        if (mOnSeekBarChangeListener != null) {
//            mOnSeekBarChangeListener.onStopTrackingTouch(this);
//        }
//    }

	
	
	
	//private int prg = 0;
	private OnSeekBarChangeListener myListener= new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			//PrintLog.getWarnLog("Vertical progress : ",   "stop");
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			//PrintLog.getWarnLog("Vertical progress : ",   "start");
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			//PrintLog.getWarnLog("Vertical progress : ", progress + "");
			//prg = progress;
//			 TextView tv = new TextView(getContext());
//			 tv.setText("Progress : "+prg);
		
		}
	};

	public VerticalSeekBar(Context context) {
		super(context);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
		this.myListener = mListener;
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (myListener != null)
				myListener.onStartTrackingTouch(this);
			break;

		case MotionEvent.ACTION_MOVE:
			setProgress(getMax()
					- (int) (getMax() * event.getY() / getHeight()));
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			 myListener.onProgressChanged(this, getMax()
			 - (int) (getMax() * event.getY() / getHeight()), true);
			break;

		case MotionEvent.ACTION_UP:
			 myListener.onStopTrackingTouch(this);
			
			break;

		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		onSizeChanged(getWidth(), getHeight(), 0, 0);
	}
}
