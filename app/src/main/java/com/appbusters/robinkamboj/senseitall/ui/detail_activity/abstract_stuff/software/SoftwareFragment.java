package com.appbusters.robinkamboj.senseitall.ui.detail_activity.abstract_stuff.software;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.appbusters.robinkamboj.senseitall.R;
import com.appbusters.robinkamboj.senseitall.model.recycler.GenericData;
import com.appbusters.robinkamboj.senseitall.utils.AppConstants;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.DetailActivity;
import com.appbusters.robinkamboj.senseitall.ui.detail_activity.other_files.MapAbout;
import com.appbusters.robinkamboj.senseitall.ui.learn_more_activity.LearnMoreActivity;
import com.appbusters.robinkamboj.senseitall.ui.test_activity.TestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.DATA_NAME;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.DRAWABLE_ID;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.IS_PRESENT;
import static com.appbusters.robinkamboj.senseitall.utils.AppConstants.TYPE;

abstract public class SoftwareFragment extends Fragment implements SoftwareInterface {

    @BindView(R.id.go_back)
    public TextView goBack;

    @BindView(R.id.go_to_test)
    public TextView goToTest;

    @BindView(R.id.about)
    public TextView about;

    @Override
    public void setup(View v) {
        ButterKnife.bind(this, v);

        hideGoToTestIfNoTest();

        setupAbout();
    }

    @Override
    public void hideGoToTestIfNoTest() {
        if(getActivity() != null && AppConstants.isTestMap.get(((DetailActivity) getActivity()).recyclerName) == null) {
            goToTest.setVisibility(View.GONE);
        }
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


    @OnClick(R.id.learn_more)
    public void setLearnMore() {
        if(getActivity() != null) {
            GenericData intentData = ((DetailActivity) getActivity()).intentData;
            Intent intent = new Intent(getActivity(), LearnMoreActivity.class);

            Bundle args = new Bundle();
            args.putString(DATA_NAME, intentData.getName());
            args.putInt(DRAWABLE_ID, intentData.getDrawableId());
            args.putBoolean(IS_PRESENT, intentData.isPresent());
            args.putInt(TYPE, intentData.getType());
            intent.putExtras(args);

            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        }
    }

    @OnClick(R.id.go_back)
    public void setGoBack() {
        if(getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.go_to_test)
    public void setGoToTest() {
        if(getActivity() != null) {
            Intent intent = new Intent(getActivity(), TestActivity.class);
            GenericData intentData = ((DetailActivity) getActivity()).intentData;
            String recyclerName = ((DetailActivity) getActivity()).recyclerName;
            Bundle args = new Bundle();

            args.putString(AppConstants.DATA_NAME, intentData.getName());
            args.putString(AppConstants.RECYCLER_NAME, recyclerName);
            args.putInt(AppConstants.DRAWABLE_ID, intentData.getDrawableId());
            args.putBoolean(AppConstants.IS_PRESENT, intentData.isPresent());
            args.putInt(AppConstants.TYPE, intentData.getType());

            intent.putExtras(args);

            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        }
    }
}
