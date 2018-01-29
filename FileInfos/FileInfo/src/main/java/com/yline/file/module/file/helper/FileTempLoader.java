package com.yline.file.module.file.helper;

import android.os.AsyncTask;

import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加载临时的数据,没有文件大小
 *
 * @author yline 2017/1/28 --> 12:24
 * @version 1.0.0
 */
public class FileTempLoader extends AsyncTask<String, Void, List<FileModel>> {
    private FileHelper.LoadListener listener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<FileModel> doInBackground(String... params) {
        String path = params[0];
        LogFileUtil.v(path);

        List<FileModel> resultList = new ArrayList<>();

        final File pathDir = new File(path);

        final File[] dirs = pathDir.listFiles(FileUtil.getsDirFilter());
        if (null != dirs) {
            Arrays.sort(dirs, FileUtil.getsComparator());

            for (File dirFile : dirs) {
                resultList.add(new FileModel(dirFile.getName(), dirFile.getAbsolutePath(),
                        dirFile.listFiles(FileUtil.getsDirFilter()).length,
                        dirFile.listFiles(FileUtil.getsFileFilter()).length,
                        FileSizeUtil.getErrorSize()));
            }
        }

        final File[] files = pathDir.listFiles(FileUtil.getsFileFilter());
        if (null != files) {
            Arrays.sort(files, FileUtil.getsComparator());

            for (File file : files) {
                resultList.add(new FileModel(file.getName(), file.getAbsolutePath(), FileSizeUtil.getErrorSize()));
            }
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(List<FileModel> fileBeen) {
        super.onPostExecute(fileBeen);

        callLoadListener(fileBeen);
    }

    public void callLoadListener(List<FileModel> fileBeen) {
        if (null != listener) {
            listener.onLoadFinish(fileBeen);
        }
    }

    public void setLoadListener(FileHelper.LoadListener listener) {
        this.listener = listener;
    }
}
