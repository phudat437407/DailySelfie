package com.example.selfie1;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by kashif on 16/11/14.
 */
public class SelfieRecordAdapter extends BaseAdapter {

    private ArrayList<com.example.selfie1.SelfieRecord> mRecordList = new ArrayList<com.example.selfie1.SelfieRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SelfieRecordAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);

        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] selfieFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".jpg");
                }
            });

            for (File file : selfieFiles) {
                com.example.selfie1.SelfieRecord selfieRecord = new com.example.selfie1.SelfieRecord(file.getAbsolutePath(), file.getName());
                mRecordList.add(selfieRecord);
            }
        }
    }

    public int getCount() {
        return mRecordList.size();
    }

    public Object getItem(int position) {
        return mRecordList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        final SelfieRecordView selfieRecordView;

        final com.example.selfie1.SelfieRecord currentRecord = mRecordList.get(position);

        if (null == convertView) {
            selfieRecordView = new SelfieRecordView();
            newView = inflater.inflate(R.layout.selfie_listitem, parent, false);
            selfieRecordView.checkBoxSelected = (CheckBox) newView.findViewById(R.id.checkbox_selected);
            selfieRecordView.thumbnail = (ImageView) newView.findViewById(R.id.thumbnail);
            selfieRecordView.selfieDate = (TextView) newView.findViewById(R.id.selfie_date);
            newView.setTag(selfieRecordView);
        }
        else {
            selfieRecordView = (SelfieRecordView) newView.getTag();
        }

        selfieRecordView.checkBoxSelected.setChecked(currentRecord.getSelected());
        selfieRecordView.checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentRecord.setSelected(isChecked);
            }
        });

        com.example.selfie1.ImageHelper.setImageFromFilePath(currentRecord.getPath(), selfieRecordView.thumbnail);
        selfieRecordView.selfieDate.setText(currentRecord.getDisplayName());

        return newView;
    }

    static class SelfieRecordView {
        CheckBox checkBoxSelected;
        ImageView thumbnail;
        TextView selfieDate;
    }

    public void add(com.example.selfie1.SelfieRecord selfieRecord) {
        mRecordList.add(selfieRecord);
        notifyDataSetChanged();
    }

    public ArrayList<com.example.selfie1.SelfieRecord> getAllRecords() {
        return mRecordList;
    }

    public ArrayList<com.example.selfie1.SelfieRecord> getSelectedRecords() {
        ArrayList<com.example.selfie1.SelfieRecord> mSelectedRecordList = new ArrayList<>();
        for (com.example.selfie1.SelfieRecord record : mRecordList) {
            if (record.getSelected()) {
                mSelectedRecordList.add(record);
            }
        }
        return mSelectedRecordList;
    }

    public void clearAll() {
        mRecordList.clear();
        notifyDataSetChanged();
    }

    public void clearSelected() {
        mRecordList.removeAll(getSelectedRecords());
        notifyDataSetChanged();
    }
}
