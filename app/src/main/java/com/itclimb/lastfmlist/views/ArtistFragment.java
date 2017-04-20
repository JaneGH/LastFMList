package com.itclimb.lastfmlist.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.itclimb.lastfmlist.R;
import com.itclimb.lastfmlist.adapters.ArtistListAdapter;
import com.itclimb.lastfmlist.base.BaseFragment;
import com.itclimb.lastfmlist.data.model.ArtistItem;
import com.itclimb.lastfmlist.injection.components.MainActivityComponent;
import com.itclimb.lastfmlist.network.LastFmSpiceService;
import com.itclimb.lastfmlist.presenters.ArtistFragmentPresenterImpl;
import com.octo.android.robospice.SpiceManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

public class ArtistFragment extends BaseFragment implements OnItemSelectedListener, IArtistFragmentView {

    @Inject
    ArtistFragmentPresenterImpl presenter;

    protected SpiceManager spiceManager = new SpiceManager(LastFmSpiceService.class);

    private Activity activity;
    private ListView listView;
    private ArtistListAdapter mArtistListAdapter;
    private View mRootView;

    public ArtistFragment() {
    }

    // ----- Lifecycle override method

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(MainActivityComponent.class).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
        presenter.onResume(spiceManager);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_artist, container, false);
            listView = (ListView) mRootView.findViewById(R.id.lv_artist);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        if (mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        listView.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore() {
//                presenter.onLoadMore();
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onItemClick((ArtistItem) listView.getAdapter().getItem(position));
            }
        });
        TextView toolBarText = (TextView) activity.findViewById(R.id.tv_toolbar);
        toolBarText.setText(getResources().getString(R.string.popular_artists));
        setSpinner();
    }

    private void setSpinner() {
        // Spinner element
        Spinner spinner = (Spinner) activity.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.country_array, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void setArtistListAdapter(List<ArtistItem> itemTalkList, int totalTalks) {
        if (mArtistListAdapter == null) {
            mArtistListAdapter = new ArtistListAdapter(this, itemTalkList, totalTalks);
            listView.setAdapter(mArtistListAdapter);
        } else {
            presenter.addListToAdapter(itemTalkList);
        }
    }

    @Override
    public void addListToAdapter(List<ArtistItem> itemTalkList) {
        mArtistListAdapter.add(itemTalkList);
    }

    @Override
    public void showProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void replaceToArtistFragment(String name) {
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AlbumFragment albumFragment = AlbumFragment.newInstance(name);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, albumFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void startService() {
        if (!spiceManager.isStarted()){
            spiceManager.start(activity);
        }
    }

    @Override
    public void stopService() {
        if (spiceManager.isStarted()){
            spiceManager.shouldStop();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.loadArtists(item, spiceManager);
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyPictureChanged() {
        if(mArtistListAdapter!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mArtistListAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
