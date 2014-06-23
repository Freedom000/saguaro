package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.model.License;
import com.willowtreeapps.saguaro.android.sample.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * User: derek Date: 6/23/14 Time: 11:39 AM
 */
public class CustomAcknowledgmentsActivity extends BaseSampleActivity {

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_acknowledgments);

        mListView = (ListView) findViewById(R.id.list);
        final Resources resources = getResources();
        final List<License> licenses = Saguaro.getOpenSourceLicenses(this, resources);
        mListView.setAdapter(new LicenseAdapter(this, licenses));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Pair<License, String> pair = (Pair<License, String>) adapterView.getAdapter().getItem(position);
                Toast.makeText(CustomAcknowledgmentsActivity.this,
                        "Item: " + position + ", with license text resId: " + pair.first.textResId,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class LicenseAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<License> mLicenses;
        private List<String> mProjectsForLicenses;
        private ViewHolder mHolder;

        private class ViewHolder {

            public TextView name;
            public TextView projects;
        }

        public LicenseAdapter(Context context, List<License> licenses) {
            mInflater = LayoutInflater.from(context);
            mLicenses = licenses;

            // Do some processing to convert the String[] of projects to one String
            mProjectsForLicenses = new ArrayList<String>();
            for (License license : licenses) {
                String delim = "";
                StringBuilder builder = new StringBuilder();
                for (String s : license.projects) {
                    builder.append(delim);
                    builder.append(s);
                    delim = ", ";
                }
                mProjectsForLicenses.add(builder.toString());
            }
        }

        @Override
        public int getCount() {
            return mLicenses.size();
        }

        @Override
        public Pair<License, String> getItem(int position) {
            return Pair.create(mLicenses.get(position), mProjectsForLicenses.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.acknowledgment_list_item, null);
                mHolder = new ViewHolder();
                mHolder.name = (TextView) view.findViewById(R.id.license_name);
                mHolder.projects = (TextView) view.findViewById(R.id.projects);
            } else {
                mHolder = (ViewHolder) view.getTag();
            }

            Pair<License, String> pair = getItem(position);
            mHolder.name.setText(pair.first.name);
            mHolder.projects.setText(pair.second);

            return view;
        }
    }

}
