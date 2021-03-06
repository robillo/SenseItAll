package com.appbusters.robinkamboj.senseitall.ui.detail_activity.abstract_stuff.feature_and_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.senseitall.R;
import com.appbusters.robinkamboj.senseitall.model.recycler.GenericData;
import com.appbusters.robinkamboj.senseitall.model.recycler.SensorDetail;
import com.appbusters.robinkamboj.senseitall.utils.AppConstants;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.DetailActivity;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.common_adapters.BasicInformationAdapter;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.other_files.MapAbout;
import com.appbusters.robinkamboj.senseitall.ui.learn_more_activity.LearnMoreActivity;
import com.appbusters.robinkamboj.senseitall.ui.test_activity.TestActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.DATA_NAME;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.DRAWABLE_ID;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.FLING_VELOCITY;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.GPS_LOCATION;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.INFO_RECYCLER_COUNT;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.IS_PRESENT;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.TYPE;

public abstract class FeatureFragment extends Fragment implements SensorInterface  {

    public boolean isLocationServicesGranted;
    private GenericData intentData;
    public boolean isViewingMore = false;
    public BasicInformationAdapter adapter;
    public List<SensorDetail> sensorDetails = new ArrayList<>();
    public List<SensorDetail> subSensorDetails = new ArrayList<>();

    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;

    @BindView(R.id.view_more)
    TextView viewMoreStatistics;

    @BindView(R.id.info_recycler)
    public RecyclerView infoRecycler;

    @BindView(R.id.go_back)
    public TextView goBack;

    @BindView(R.id.go_to_test)
    public TextView goToTest;

    @BindView(R.id.about)
    public TextView about;

    @Override
    public void showSensorDetails() {
        initializeBasicInformation();
        showBasicInformation();
    }

    @Override
    public void showBasicInformation() {
        if(getActivity() != null) intentData = ((DetailActivity) getActivity()).intentData;
        infoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        for(int i=0; i<INFO_RECYCLER_COUNT && i<sensorDetails.size(); i++) {
            subSensorDetails.add(sensorDetails.get(i));
        }

        setStatisticsAdapter();

        if(sensorDetails.size() > INFO_RECYCLER_COUNT) {
            viewMoreStatistics.setVisibility(View.VISIBLE);
        }
        else {
            viewMoreStatistics.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideGoToTestIfNoTest() {
        if(getActivity() != null && !AppConstants.isTestMap.get(((DetailActivity) getActivity()).recyclerName)) {
            goToTest.setVisibility(View.GONE);
        }
    }

    @Override
    public void addToDetailsList(List<SensorDetail> sensorDetails, String key, String value) {
        sensorDetails.add(new SensorDetail(key, value));
    }

    @Override
    public void setupAbout() {
        if (getActivity() != null) {
            String[] temp = getActivity().getResources().getStringArray(
                    MapAbout.mapAbout.get(((DetailActivity) getActivity()).intentData.getName())
            );
            about.setText(temp[0]);
        }
    }

    @Override
    public void setStatisticsAdapter() {

        List<SensorDetail> listToShow;

        if(isViewingMore) listToShow = sensorDetails;
        else listToShow = subSensorDetails;

        if(intentData != null)
            adapter = new BasicInformationAdapter(getActivity(), listToShow, intentData.getName());
        else
            adapter = new BasicInformationAdapter(getActivity(), listToShow);

        infoRecycler.setAdapter(adapter);
    }

    @OnClick(R.id.go_back)
    public void setGoBack() {
        if(getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.go_to_test)
    public void setGoToTest() {
        if(getActivity() != null) {
            if(intentData != null && intentData.getName().equals(GPS_LOCATION)) {
                if(isLocationServicesGranted) {
                    startTestActivity();
                }
                else {
                    Toast.makeText(getActivity(), "please turn on location services first", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                startTestActivity();
            }
        }
    }

    void startTestActivity() {
        if(getActivity() == null) return;

        Intent intent = new Intent(getActivity(), TestActivity.class);
        GenericData intentData = ((DetailActivity) getActivity()).intentData;
        String recyclerName = ((DetailActivity) getActivity()).recyclerName;
        Bundle args = new Bundle();

        if(intentData == null) {
            Toast.makeText(getActivity(), "some error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        args.putString(AppConstants.DATA_NAME, intentData.getName());
        args.putString(AppConstants.RECYCLER_NAME, recyclerName);
        args.putInt(AppConstants.DRAWABLE_ID, intentData.getDrawableId());
        args.putBoolean(AppConstants.IS_PRESENT, intentData.isPresent());
        args.putInt(AppConstants.TYPE, intentData.getType());

        intent.putExtras(args);

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
    }

    @OnClick(R.id.learn_more)
    public void setLearnMore() {
        if(getActivity() != null) {
            GenericData intentData = ((DetailActivity) getActivity()).intentData;
            Intent intent = new Intent(getActivity(), LearnMoreActivity.class);

            Bundle args = new Bundle();

            if(intentData != null) {
                args.putString(DATA_NAME, intentData.getName());
                args.putInt(DRAWABLE_ID, intentData.getDrawableId());
                args.putBoolean(IS_PRESENT, intentData.isPresent());
                args.putInt(TYPE, intentData.getType());
            }

            intent.putExtras(args);

            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        }
    }

    @OnClick(R.id.view_more)
    public void toggleViewMore() {
        isViewingMore = !isViewingMore;

        if(isViewingMore) {
            viewMoreStatistics.setText(getString(R.string.less_statistics));
            setStatisticsAdapter();
            scrollView.fling(FLING_VELOCITY);
        }
        else {
            viewMoreStatistics.setText(getString(R.string.more_statistics));
            setStatisticsAdapter();
        }
    }
}
