package com.example.akash.bucketdrops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.akash.bucketdrops.beans.Drop;

import adapters.AdapterDrops;
import adapters.AddListener;
import adapters.Divider;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import widgets.BucketRecyclerView;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "Akash";
    private Toolbar mToolBar;
    private Button mBtnAdd;
    private BucketRecyclerView mRecyclerView;
    Realm mRealm;
    RealmResults<Drop> mRealmResults;
    AdapterDrops mAdapter;
    View mEmptyView;
    private AddListener addListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mEmptyView = findViewById(R.id.empty_drops);
        initBackGroundImage();

        mRecyclerView = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.hideIfEmpty(mToolBar);
        mRecyclerView.showIfEmpty(mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        mRealm = Realm.getDefaultInstance();
        mRealmResults = mRealm.where(Drop.class).findAllAsync();
        mAdapter = new AdapterDrops(this, mRealmResults, addListener);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityMain.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                showDialogAdd();
            }
        });
    }

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            Log.d(TAG, "onChange: called");
            mAdapter.update(mRealmResults);
        }
    };


    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private void initBackGroundImage(){
        ImageView backGround = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(backGround);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRealmResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRealmResults.removeChangeListener(mChangeListener);
    }
}
