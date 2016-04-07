package adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.akash.bucketdrops.AppBucketDrops;
import com.example.akash.bucketdrops.R;
import com.example.akash.bucketdrops.beans.Drop;

import org.w3c.dom.Text;

import java.util.ArrayList;

import extras.Util;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by knighthood on 3/31/2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int ITEM = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;

    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;

    private int mFilterOption;

    private static final String TAG = "Akash";
    private LayoutInflater mInflater;
    RealmResults<Drop> mResults;
    private AddListener mAddListener;
    private MarkListener mMarkListener;
    private Realm mRealm;

    private Context mContext;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener addListener, MarkListener markListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mAddListener = addListener;
        mMarkListener = markListener;
        mRealm = realm;
        update(results);
    }

    public void update(RealmResults<Drop> drops) {
        mResults = drops;
        mFilterOption = AppBucketDrops.load(mContext);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (!mResults.isEmpty()) {
            if (position < mResults.size())
                return ITEM;
            else
                return FOOTER;
        }
        else {
            if (mFilterOption == Filter.COMPLETED || mFilterOption == Filter.INCOMPLETE) {
                if (position == 0)
                    return NO_ITEM;
                else
                    return FOOTER;
            }
            else{
                return ITEM;
            }
        }
    }

    public static ArrayList<String> generate() {
        ArrayList<String> dummy = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            dummy.add("Item: " + i);
        return dummy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == FOOTER) {
            Log.d(TAG, "onCreateViewHolder: Footer");
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        } else if (viewType == NO_ITEM) {
            Log.d(TAG, "onCreateViewHolder: No Item");
            View view = mInflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(view);
        } else {
            Log.d(TAG, "onCreateViewHolder: Row Drop");
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mMarkListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setText(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
        Log.d(TAG, "onBindViewHolder: at position: " + position);
    }

    @Override
    public long getItemId(int position) {
        if(position < mResults.size())
            return mResults.get(position).getTime();
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        if (!mResults.isEmpty())
            return mResults.size() + COUNT_FOOTER;
        else {
            if (mFilterOption == Filter.LEAST_TIME_LEFT ||
                    mFilterOption == Filter.MOST_TIME_LEFT ||
                    mFilterOption == Filter.NONE)
                return 0;
            else
                return COUNT_NO_ITEMS + COUNT_FOOTER;
        }
    }

    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
    }

    public void markComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, MarkListener listener) {
            super(itemView);
            mItemView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
            mMarkListener = listener;
        }

        public void setText(String str) {
            mTextWhat.setText(str);
        }

        @Override
        public void onClick(View v) {
            mMarkListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable;
            if (completed) {
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }
            Util.setBackground(mItemView, drawable);
        }

        public void setWhen(long when) {
            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));
        }
    }

    public static class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mBtnAdd;
        AddListener mListener;

        public FooterHolder(View itemView) {
            super(itemView);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener addListener) {
            super(itemView);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
            mListener = addListener;
        }

        @Override
        public void onClick(View v) {
            mListener.add();
        }
    }
}
