package com.example.akash.bucketdrops;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.akash.bucketdrops.beans.Drop;

import adapters.AdapterDrops;
import adapters.AddListener;
import adapters.CompleteListener;
import adapters.Divider;
import adapters.Filter;
import adapters.MarkListener;
import adapters.SimpleTouchCallBack;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import widgets.BucketRecyclerView;

public class ActivityMain extends AppCompatActivity {

    public static final String TAG = "Akash";
    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;
    AdapterDrops mAdapter;

    // Button Click Listener
    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    // Add Drop (Show date picker)
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    // When Drops are updated (removed/marked/added)
    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            Log.d(TAG, "onChange: was called");
            mAdapter.update(mResults);
        }
    };

    // Event Listener for Drop rows
    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    // Even Listener when Marked is clicked and items is completed
    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);
        }
    };

    // When Add is clicked
    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    // When Drop row is clicked
    private void showDialogMark(int position) {
        DialogMark dialog = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(), "Mark");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Button
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(mBtnAddListener);

        // Realm
        mRealm = Realm.getDefaultInstance();

        // Load the drops based on filter stored
        int filterOption = AppBucketDrops.load(this);
        loadResults(filterOption);

        // Views
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mEmptyView = findViewById(R.id.empty_drops);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.setLayoutManager(manager);

        // Adapter
        mAdapter = new AdapterDrops(this, mRealm, mResults, mAddListener, mMarkListener);
        mRecycler.setAdapter(mAdapter);

        // Touch Call Back
        SimpleTouchCallBack callback = new SimpleTouchCallBack(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);
        initBackgroundImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.sort_none:
                filterOption = Filter.NONE;
                break;
            case R.id.sort_descending_date:
                filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.sort_ascending_date:
                filterOption = Filter.MOST_TIME_LEFT;
                break;
            case R.id.show_complete:
                filterOption = Filter.COMPLETED;
                break;
            case R.id.show_incomplete:
                filterOption = Filter.INCOMPLETE;
                break;
            default:
                handled = false;
                break;
        }
        loadResults(filterOption);
        AppBucketDrops.save(this, filterOption);
        return handled;
    }

    /**
     * Sort drops based on selected filter (From Menu Option)
     * @param filterOption
     */
    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.COMPLETED:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }

    // Glide library to load Background Image
    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }
}
