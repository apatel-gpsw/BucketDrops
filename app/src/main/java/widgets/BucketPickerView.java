package widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.akash.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by knighthood on 4/7/2016.
 */
public class BucketPickerView extends LinearLayout implements View.OnTouchListener {

    private int MESSAGE_WHAT = 123;
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView mTextMonth;
    private TextView mTextYear;
    SimpleDateFormat mFormat;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOT = 3;
    private boolean mIncreament = false;
    private boolean mDereament = false;
    private int mActiveId;
    private static final int DELAY = 250;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mIncreament)
                increament(mActiveId);
            if (mDereament)
                decreament(mActiveId);

            if (mIncreament || mDereament)
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            return true;
        }
    });

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormat = new SimpleDateFormat("MMM");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("super", super.onSaveInstanceState());
        bundle.putInt("date", mCalendar.get(Calendar.DATE));
        bundle.putInt("month", mCalendar.get(Calendar.MONTH));
        bundle.putInt("year", mCalendar.get(Calendar.YEAR));
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Parcelable) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("super");
            int date = bundle.getInt("date");
            int month = bundle.getInt("month");
            int year = bundle.getInt("year");

            update(date, month, year, 0, 0, 0);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDate = (TextView) this.findViewById(R.id.tv_date);
        mTextDate.setOnTouchListener(this);

        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextMonth.setOnTouchListener(this);

        mTextYear = (TextView) this.findViewById(R.id.tv_year);
        mTextYear.setOnTouchListener(this);

        update(mCalendar.get(Calendar.DATE), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR), 0, 0, 0);
    }

    private void update(int date, int month, int year, int hour, int minute, int second) {
        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mTextDate.setText(date + "");
        mTextMonth.setText(mFormat.format(mCalendar.getTime()));
        mTextYear.setText(year + "");
    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_date:
                processEventsFor(mTextDate, event);
                break;
            case R.id.tv_month:
                processEventsFor(mTextMonth, event);
                break;
            case R.id.tv_year:
                processEventsFor(mTextYear, event);
                break;
        }
        return true;
    }

    public void processEventsFor(TextView textView, MotionEvent event) {
        Drawable[] drawables = textView.getCompoundDrawables();
        if (drawables[TOP] != null && drawables[BOT] != null) {
            Rect topBound = drawables[TOP].getBounds();
            Rect botBound = drawables[BOT].getBounds();
            float x = event.getX();
            float y = event.getY();
            mActiveId = textView.getId();

            if (topDrwableHit(textView, topBound.height(), x, y)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mIncreament = true;
                    increament(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView, true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mIncreament = false;
                    toggleDrawable(textView, false);
                }
            } else if (botDrawableHit(textView, botBound.height(), x, y)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDereament = true;
                    decreament(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView, true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mDereament = false;
                    toggleDrawable(textView, false);
                }
            } else {
                mIncreament = false;
                mDereament = false;
                toggleDrawable(textView, false);
            }
        }
    }

    private void increament(int i) {
        switch (i) {
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, 1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }
        set(mCalendar);
    }

    private void decreament(int i) {
        switch (i) {
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, -1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }
        set(mCalendar);
    }

    private void set(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        mTextDate.setText(date + "");
        mTextMonth.setText(mFormat.format(mCalendar.getTime()));
        mTextYear.setText(year + "");
    }

    private boolean topDrwableHit(TextView textView, int height, float x, float y) {
        int minX = textView.getPaddingLeft();
        int maxX = textView.getWidth() - textView.getPaddingRight();
        int minY = textView.getPaddingTop();
        int maxY = textView.getPaddingBottom() + height;
        return x > minX && x < maxX && y > minY && y < maxY;
    }

    private boolean botDrawableHit(TextView textView, int height, float x, float y) {
        int minX = textView.getPaddingLeft();
        int maxX = textView.getWidth() - textView.getPaddingRight();
        int maxY = textView.getHeight() - textView.getPaddingBottom();
        int minY = maxY - height;
        return x > minX && x < maxX && y > minY && y < maxY;
    }

    private void toggleDrawable(TextView textView, boolean pressed) {
        if (pressed) {
            if (mIncreament) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_pressed, 0, R.drawable.up_normal);
            }
            if (mDereament) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_pressed);
            }
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal, 0, R.drawable.down_normal);
        }
    }

//    public boolean hasDrawableTop(Drawable[] drawables){
//        return drawables[TOP] != null;
//    }
//
//    public boolean hasDrawableBot(Drawable[] drawables){
//        return drawables[BOT] != null;
//    }
}

