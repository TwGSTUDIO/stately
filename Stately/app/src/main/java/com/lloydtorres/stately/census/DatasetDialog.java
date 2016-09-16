/**
 * Copyright 2016 Lloyd Torres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lloydtorres.stately.census;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lloydtorres.stately.R;
import com.lloydtorres.stately.core.RecyclerDialogFragment;
import com.lloydtorres.stately.dto.Dataset;

import java.util.ArrayList;

/**
 * Created by Lloyd on 2016-04-10.
 * A dialog showing the available trend datasets.
 */
public class DatasetDialog extends RecyclerDialogFragment {
    public static final String DIALOG_TAG = "fragment_dataset_dialog";
    public static final String DATASETS_KEY = "datasets";

    private ArrayList<Dataset> datasets;

    public void setDatasets(String[] rawDataset, int selected) {
        datasets = new ArrayList<Dataset>();

        for (int i=0; i<rawDataset.length-1; i++)
        {
            Dataset d = new Dataset();
            d.name = rawDataset[i].split("##")[0];
            d.id = i;
            d.selected = false;
            datasets.add(d);
        }

        if (selected < rawDataset.length-1)
        {
            datasets.get(selected).selected = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore saved state
        if (savedInstanceState != null && datasets == null) {
            datasets = savedInstanceState.getParcelableArrayList(DATASETS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getDialog().setTitle(getString(R.string.trends_datasets));

        return view;
    }

    @Override
    protected void initRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_padded);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new DatasetRecyclerAdapter(getActivity(), this, datasets);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        int position = ((DatasetRecyclerAdapter) mRecyclerAdapter).getSelectedPosition();
        if (position != DatasetRecyclerAdapter.INVALID_POSITION) {
            mLayoutManager.scrollToPosition(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save state
        super.onSaveInstanceState(savedInstanceState);
        if (datasets != null)
        {
            savedInstanceState.putParcelableArrayList(DATASETS_KEY, datasets);
        }
    }
}
