package com.appbusters.robinkamboj.senseitall.ui.dashboard_activity.discover_fragment;

import android.view.View;

public interface DiscoverInterface {

    void initialize();

    void setup(View v);

    void setCategoriesAdapter();

    void setToolsAdapter();

    void setViewPagerNewlyAdded();

}
