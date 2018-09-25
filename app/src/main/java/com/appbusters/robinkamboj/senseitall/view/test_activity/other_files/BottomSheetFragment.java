package com.appbusters.robinkamboj.senseitall.view.test_activity.other_files;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appbusters.robinkamboj.senseitall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.results)
    TextView results;

    @BindView(R.id.header_text)
    TextView headerText;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the lv for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        setup(v);
        return v;
    }

    public void setup(View v) {
        ButterKnife.bind(this, v);
    }

    public void setResults(String text) {
        progressBar.setVisibility(View.GONE);
        results.setText(text);
    }

    public void setHeader(String header) {
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder builder = new StringBuilder(header).append(" Results");
        headerText.setText(builder.toString());
    }
}
