package com.torrent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.torrent.download.DownloadActivity;
import com.torrent.torrent.ParseActivity;
import com.torrent.util.EncryptUtil;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, final Bundle savedInstanceState) {
        final TextView hintEncryptTextView = addTextView("");
        addButton("解密Utils 测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();

                long startTime = System.currentTimeMillis();
                String result = EncryptUtil.getAssetsEncrypt(MainActivity.this, "multi.torrent", EncryptUtil.MD5);
                stringBuilder.append("Asset2MD5 = ");
                stringBuilder.append(result);
                stringBuilder.append("   size = ");
                stringBuilder.append(result.length());
                stringBuilder.append("    diffTime = ");
                stringBuilder.append(System.currentTimeMillis() - startTime);
                stringBuilder.append('\n');

                startTime = System.currentTimeMillis();
                result = EncryptUtil.getAssetsEncrypt(MainActivity.this, "multi.torrent", EncryptUtil.SHA1);
                stringBuilder.append("Asset2SHA1 = ");
                stringBuilder.append(result);
                stringBuilder.append("   size = ");
                stringBuilder.append(result.length());
                stringBuilder.append("    diffTime = ");
                stringBuilder.append(System.currentTimeMillis() - startTime);
                stringBuilder.append('\n');

                startTime = System.currentTimeMillis();
                result = EncryptUtil.getAssetsEncrypt(MainActivity.this, "multi.torrent", EncryptUtil.SHA256);
                stringBuilder.append("Asset2SHA256 = ");
                stringBuilder.append(result);
                stringBuilder.append("   size = ");
                stringBuilder.append(result.length());
                stringBuilder.append("    diffTime = ");
                stringBuilder.append(System.currentTimeMillis() - startTime);
                stringBuilder.append('\n');

                result = stringBuilder.toString();

                hintEncryptTextView.setText(result);
            }
        });

        addButton("Download Test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadActivity.launcher(MainActivity.this);
            }
        });

        addButton("Parse Text", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseActivity.launcher(MainActivity.this);
            }
        });
    }
}
