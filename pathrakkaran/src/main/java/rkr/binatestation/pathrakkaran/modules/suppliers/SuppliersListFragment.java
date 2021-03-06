package rkr.binatestation.pathrakkaran.modules.suppliers;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rkr.binatestation.pathrakkaran.R;
import rkr.binatestation.pathrakkaran.adapters.UsersAdapter;
import rkr.binatestation.pathrakkaran.models.UserDetailsModel;

import static rkr.binatestation.pathrakkaran.utils.Constants.KEY_USER_ID;

/**
 * Fragment to load agent product list
 */
public class SuppliersListFragment extends Fragment implements SuppliersListeners.ViewListener, View.OnClickListener {


    private static final String TAG = "SuppliersListFragment";
    private ContentLoadingProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private long userId = 0;
    private SuppliersListeners.PresenterListener mPresenterListener;
    private UsersAdapter mUsersAdapter;
    private AddSupplierFragment mAddSupplierFragment;
    private ActionBar mActionBar;

    public SuppliersListFragment() {
        // Required empty public constructor
    }

    public static SuppliersListFragment newInstance() {
        Log.d(TAG, "newInstance() called");
        Bundle args = new Bundle();

        SuppliersListFragment fragment = new SuppliersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isPresenterLive() {
        return mPresenterListener != null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mActionBar = activity.getSupportActionBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPresenterListener = new SuppliersPresenter(this);
        userId = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE).getLong(KEY_USER_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.FL_recycler_view);
        FloatingActionButton addProduct = (FloatingActionButton) view.findViewById(R.id.FL_action_add_product);
        mProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.FL_progress_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.FL_swipe_refresh);

        //Setting Recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mUsersAdapter = new UsersAdapter());

        //Setting addProduct button
        addProduct.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isPresenterLive()) {
                    mPresenterListener.loadSuppliersList(getLoaderManager(), userId);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPresenterLive()) {
            mPresenterListener.loadSuppliersList(getLoaderManager(), userId);
        }
        setActionBar(
                getString(R.string.suppliers_list),
                getString(R.string.app_name)
        );
    }

    private void setActionBar(String title, String subtitle) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
            mActionBar.setSubtitle(subtitle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FL_action_add_product:
                mAddSupplierFragment = AddSupplierFragment.newInstance(new AddSupplierFragment.AddSupplierListener() {
                    @Override
                    public void submit(String name, String mobile, String email) {
                        if (isPresenterLive()) {
                            mPresenterListener.registerSupplier(getContext(), name, mobile, email, userId);
                        }
                    }
                });
                mAddSupplierFragment.show(getChildFragmentManager(), mAddSupplierFragment.getTag());
                break;
        }
    }

    @Override
    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.show();
        }
    }

    @Override
    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setRecyclerView(List<UserDetailsModel> userDetailsModelList) {
        Log.d(TAG, "setRecyclerView() called with: userDetailsModelList = [" + userDetailsModelList + "]");
        if (mUsersAdapter != null) {
            mUsersAdapter.setUserDetailsModelList(userDetailsModelList);
        }

    }

    @Override
    public void addItem(UserDetailsModel userDetailsModel) {
        if (mUsersAdapter != null) {
            mUsersAdapter.addItem(userDetailsModel);
        }
        if (mAddSupplierFragment != null && mAddSupplierFragment.isResumed()) {
            mAddSupplierFragment.hideProgress();
            mAddSupplierFragment.dismiss();
        }
    }
}
