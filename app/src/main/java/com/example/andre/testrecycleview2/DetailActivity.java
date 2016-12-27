package com.example.andre.testrecycleview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_SUBTITLE = "EXTRA_SUBTITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        ((TextView)findViewById(R.id.detail_title)).setText(extras.getString(EXTRA_TITLE));
        ((TextView)findViewById(R.id.detail_subtitle)).setText(extras.getString(EXTRA_SUBTITLE));
    }
}
