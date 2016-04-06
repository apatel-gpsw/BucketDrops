package widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import extras.Util;

/**
 * Created by knighthood on 4/1/2016.
 */
public class BucketRecyclerView extends RecyclerView {

    private List<View> mNonEmptyList = Collections.emptyList();
    private List<View> mEmptyList = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleView();
        }
    };

    private void toggleView() {
        if (getAdapter() != null && !mEmptyList.isEmpty() && !mNonEmptyList.isEmpty()) {
            // No Drops to show
            if (getAdapter().getItemCount() == 0) {
                // Show Empty List (Image and Add Button) and Hide Non Empty List
                Util.showHideViews(mEmptyList, mNonEmptyList);

                // Hide BucketList
                setVisibility(View.GONE);
            } else {
                // Show Non Empty List and Hide Empty List (Image and Add Button)
                Util.showHideViews(mNonEmptyList, mEmptyList);

                // Show BucketList
                setVisibility(View.VISIBLE);
            }
        }
    }

    public BucketRecyclerView(Context context) {
        super(context);
    }

    public BucketRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null)
            adapter.registerAdapterDataObserver(mObserver);
        mObserver.onChanged();
    }

    public void hideIfEmpty(View... views) {
        mNonEmptyList = Arrays.asList(views);
    }

    public void showIfEmpty(View... mEmptyViews) {
        mEmptyList = Arrays.asList(mEmptyViews);
    }
}
