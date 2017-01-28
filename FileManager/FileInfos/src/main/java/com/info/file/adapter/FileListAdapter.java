package com.info.file.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.info.file.R;
import com.info.file.bean.FileBean;
import com.yline.base.common.CommonListAdapter;

/**
 * Created by yline on 2017/1/28.
 */
public class FileListAdapter extends CommonListAdapter<FileBean>
{
	public FileListAdapter(Context context)
	{
		super(context);
	}

	@Override
	protected int getItemRes(int i)
	{
		return R.layout.item_file_list;
	}

	@Override
	protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		viewHolder.setImage(R.id.iv_type, sList.get(i).getImageId());
		viewHolder.setText(R.id.tv_name, sList.get(i).getFileName());
		viewHolder.setText(R.id.tv_info, sList.get(i).getChildMessage());
	}
}
