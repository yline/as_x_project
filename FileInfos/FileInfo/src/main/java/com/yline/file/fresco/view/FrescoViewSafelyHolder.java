package com.yline.file.fresco.view;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.yline.file.fresco.common.FrescoCallback;
import com.yline.file.fresco.drawable.LoadingDrawable;
import com.yline.file.fresco.drawable.LoadingRenderer;

/**
 * 入口检查
 *
 * @author yline 2017/9/23 -- 17:44
 * @version 1.0.0
 */
public final class FrescoViewSafelyHolder extends FrescoViewHolder {

    public FrescoViewSafelyHolder(FrescoView frescoView) {
        super(frescoView);
        setOnBdttErrorCallback(new FrescoCallback.OnBdttErrorCallback() {
            @Override
            public void onFailure(Uri imageUrl, String hint) {
                Log.i("xxx-fresco", "onFailure: hint = " + hint + ", image = " + imageUrl);
                // NetWorkUtil.isNetworkAvailable(LocalNewsApplication.getContext());
            }
        });
    }

    public void setImageUri(String uriString) {
        if (TextUtils.isEmpty(uriString)) {
            uriString = "failure";
        }

        Uri imageUri = Uri.parse(uriString);
        super.setImageUri(imageUri);
    }

    public void setLayoutParams(int viewWidth, int viewHeight) {
        ViewGroup.LayoutParams layoutParams = frescoView.getLayoutParams();
        if (null != layoutParams) {
            layoutParams.width = viewWidth;
            layoutParams.height = viewHeight;
        } else {
            layoutParams = new ViewGroup.LayoutParams(viewWidth, viewHeight);
        }
        super.setLayoutParams(layoutParams);
    }

    public void setResizeOptions(int bitmapWidth, int bitmapHeight) {
        ResizeOptions resizeOptions = new ResizeOptions(bitmapWidth, bitmapHeight);
        super.setResizeOptions(resizeOptions);
    }

    public void setLoadingRender(@NonNull LoadingRenderer render) {
        super.setLoadingDrawable(new LoadingDrawable(render));
    }
}
