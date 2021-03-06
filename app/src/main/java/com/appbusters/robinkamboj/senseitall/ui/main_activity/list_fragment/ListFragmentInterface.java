package com.appbusters.robinkamboj.senseitall.ui.main_activity.list_fragment;

import android.view.View;

import com.appbusters.robinkamboj.senseitall.model.recycler.PermissionsItem;

import java.util.List;

public interface ListFragmentInterface {

    void setup(View v);

    void setHeaderText(String intentCategory);

    void inflateData();

    void changeStatusBarColor();

    void setHeaderTextAndRv();

    int checkIfAllPermissionsGiven();

    void checkForPresentSensors();

    List<PermissionsItem> getPermissionItemsList();

    void setEditTextSearchListener();

    void filter(String searchText);

    void hideOrShowSoftKeyboard(boolean haveToShow);

    void resetSearchText();

    //adapter related stuff

    void initializeAdapter();

    void fillGenericDataForSelected(int type);
}
