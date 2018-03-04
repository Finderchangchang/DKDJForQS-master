package liuliu.qs.setImage;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;

import liuliu.qs.config.Key;
import liuliu.qs.method.Utils;

/**
 * Created by XY on 2017/8/2.
 */

public class AsyncImage {
    public static void uploadFile(String path, String url, Context mContext, CloseActivityInterface state) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        if (file.exists() && file.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("uploadfile", file);
            params.put("togoid", Utils.getCache(Key.KEY_UserId));
            params.put("ext", "jpg");
            // 上传文件


            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_LONG).show();
                    state.state(1);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_LONG).show();
                    state.state(2);
                }
            });

        } else {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_LONG).show();
        }

    }
}
