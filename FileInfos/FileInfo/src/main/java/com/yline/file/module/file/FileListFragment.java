package com.yline.file.module.file;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yline.base.BaseListFragment;
import com.yline.file.R;
import com.yline.file.module.file.helper.FileHelper;
import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.util.List;

/**
 * Created by yline on 2017/1/27.
 */
public class FileListFragment extends BaseListFragment implements FileHelper.LoadListener {
    private String path;

    private FileListAdapter mAdapter;

    private FileHelper fileHelper;

    public static FileListFragment newInstance(String path) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(FileListActivity.getTagPath(), path);
        fragment.setArguments(args);

        return fragment;
    }

    public void refreshFragment(String path) {
        setListShown(false);
        fileHelper.getFileList(this, path);
    }

    private String initPath() {
        if (null != getArguments()) {
            return getArguments().getString(FileListActivity.getTagPath());
        } else {
            return FileUtil.getPathTop();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogFileUtil.v("onViewCreated");

        path = initPath();

        fileHelper = new FileHelper();

        mAdapter = new FileListAdapter(getActivity());
        setListAdapter(mAdapter);

        setEmptyText(getString(R.string.empty_directory));
        setListShown(false);

        // 获取数据
        fileHelper.getFileList(this, path);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != l.getAdapter()) {
            FileModel fileBean = (FileModel) l.getAdapter().getItem(position);
            path = fileBean.getAbsolutePath();

            if (getActivity() instanceof onFileSelectedCallback) {
                ((onFileSelectedCallback) getActivity()).onFileSelected(path);
            }
        }
    }

    @Override
    public void onLoadFinish(List<FileModel> fileBeen) {
        // 更新数据
        mAdapter.setDataList(fileBeen, true);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    public interface onFileSelectedCallback {
        public void onFileSelected(String path);
    }
}
