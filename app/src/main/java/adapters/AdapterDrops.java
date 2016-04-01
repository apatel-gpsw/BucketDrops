package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.akash.bucketdrops.R;
import com.example.akash.bucketdrops.beans.Drop;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by knighthood on 3/31/2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    private static final String TAG = "Akash";
    private LayoutInflater mInflater;
    RealmResults<Drop> mResults;
    private AddListener mAddListener;

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        update(results);
    }

    public AdapterDrops(Context context, RealmResults<Drop> results, AddListener addListener) {
        mInflater = LayoutInflater.from(context);
        mAddListener = addListener;
        update(results);
    }

    public void update(RealmResults<Drop> drops) {
        mResults = drops;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mResults == null || position < mResults.size())
            return ITEM;
        else
            return FOOTER;
    }

    public static ArrayList<String> generate() {
        ArrayList<String> dummy = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            dummy.add("Item: " + i);
        return dummy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == FOOTER){
            Log.d(TAG, "onCreateViewHolder: Footer");
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        }
        else{
            Log.d(TAG, "onCreateViewHolder: Row Drop");
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.mTextWhat.setText(drop.getWhat());
        }
        Log.d(TAG, "onBindViewHolder: at position: " + position);
    }

    @Override
    public int getItemCount() {
        return mResults.size() + 1;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);

        public DropHolder(View itemView) {
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
