package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akash.bucketdrops.R;
import com.example.akash.bucketdrops.beans.Drop;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by knighthood on 3/31/2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private static final String TAG = "Akash";
    private LayoutInflater mInflater;
    RealmResults<Drop> mResults;

    public AdapterDrops(Context context, RealmResults<Drop> results){
        mInflater = LayoutInflater.from(context);
        update(results);
    }

    public void update(RealmResults<Drop> drops){
        mResults = drops;
        notifyDataSetChanged();
    }

    public static ArrayList<String> generate(){
        ArrayList<String> dummy = new ArrayList<>();
        for(int i = 0; i < 100; i++)
            dummy.add("Item: " + i);
        return dummy;
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder holder = new DropHolder(view);
        Log.d(TAG, "onCreateViewHolder: ");
        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop = mResults.get(position);
        holder.mTextWhat.setText(drop.getWhat());
        Log.d(TAG, "onBindViewHolder: at position: " + position);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder{

        TextView mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
        public DropHolder(View itemView) {
            super(itemView);
        }
    }
}
