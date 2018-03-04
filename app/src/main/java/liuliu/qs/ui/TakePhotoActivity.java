package liuliu.qs.ui;

import butterknife.Bind;
import butterknife.ButterKnife;
import liuliu.qs.R;
import liuliu.qs.base.BaseActivity;
import liuliu.qs.config.Key;
import liuliu.qs.method.Utils;
import liuliu.qs.model.MessageModel;
import liuliu.qs.setImage.ImgUtil;
import liuliu.qs.setImage.compressImage;
import liuliu.qs.wxapi.MD5;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by XY on 2017/7/24.
 */

public class TakePhotoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.send_photo_ll)
    LinearLayout SendPhotoLl;
    @Bind(R.id.send_photo_button)
    Button SendphotoButton;
    @Bind(R.id.retake_button)
    Button RetakeButton;
    @Bind(R.id.photo_imageview)
    ImageView PhotoImageview;
    @Bind(R.id.take_photo_tv)
    TextView TakephotoTv;
    @Bind(R.id.back_tv)
    TextView BackTv;
    @Bind(R.id.choose_photo_tv)
    TextView ChoosePhotoTv;
    Uri imageUri;
    Bitmap bitmap;
    @Bind(R.id.image_icon)
    ImageView image_icon;
    static String chooseuri;
    private final int TATE_PHOTO = 1;
    private final int CROP_PHOTO = 2;
    private int flag = 1;
    private int Firstflag = 1;
    private int photoflag = 0;
    String url;
    File outputImage;
    Map<String, String> map;
    String pickname;//选择相册图片的文件名

    @Override
    public void initViews() {

        setContentView(R.layout.activity_take_photo);

        ButterKnife.bind(this);

        // EventBus.getDefault().register(this);
        url = "http://kuaipao.myejq.com/App/shop/kuaipaoAddOrderImg.ashx";
        BackTv.setOnClickListener(v -> {
            finish();
        });
        RetakeButton.setOnClickListener(this);
        TakephotoTv.setOnClickListener(this);
    }

    @Override
    public void initEvents() {

        //发送照片按钮
        SendphotoButton.setOnClickListener(v -> {


            if (photoflag == 1) {
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(this);
                //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示");
                dialog.setMessage("确认发送吗？");
                dialog.setIcon(R.mipmap.message);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPhoto();
                        dialog.dismiss();
                    }


                });
                dialog.create().show();

            } else {
                ToastShort("请选择照片！");
            }
        });


        /**byOkhttp*/
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url1 = new URL(url);
                        byOKhttp byokhttp = new byOKhttp();
                        byokhttp.upImage(url1, TakePhotoActivity.this,  handler,chooseuri,flag);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/


        /**by AsyncImage*/
//            try {
//                 AsyncImage asyncimage=new AsyncImage();
//                 asyncimage.uploadFile(outfile+"","http://kuaipao.myejq.com/App/shop/kuaipaoAddOrderImg.aspx",TakePhotoActivity.this,TakePhotoActivity.this);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        /**by postIoImage*/
/*            new Thread() {
                public void run() {
                    PostIoImage ioImage=new PostIoImage();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("togoid", Utils.getCache(Key.KEY_UserId));
                    map.put("ext", "jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    String photo = Base64.encodeToString(baos.toByteArray(), 0, baos.toByteArray().length,Base64.DEFAULT);
                    final Map<String, File> files = new HashMap<String, File>();
                    String str = null;
                    try {
                        str= ioImage.post("http://kuaipao.myejq.com/App/shop/kuaipaoAddOrderImg.aspx",map,photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            }.start();*/


        //选择相册照片
        ChoosePhotoTv.setOnClickListener(v -> {
            /*outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
            if (outputImage.exists()) {
                outputImage.delete();
            }
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image*//**//*");
            //intent.putExtra("crop", true);
            //intent.putExtra("scale", true);
            flag = 2;
            startActivityForResult(intent, CROP_PHOTO);
            */
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);


        });
    }

    //发送照片
    private void sendPhoto() {

        if (map == null) {
            map = new HashMap<String, String>();
            map.put("togoid", Utils.getCache(Key.KEY_UserId));
            map.put("ext", "jpg");
            map.put("imgname", pickname);
        }
        if (Firstflag == 1) {//第一次上传图片标志位=0
            map.put("imgfirst", "0");
        } else if (Firstflag == 2) {//第二次上传图片标志位上传1
            map.put("imgfirst", "1");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Message m = new Message();
        Observable.create(subscriber -> {
            subscriber.onNext(communication01(url, map, baos.toByteArray()));
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(str -> {
                    MessageModel model = new Gson().fromJson(str.toString(), MessageModel.class);
                    if (("1").equals(model.getState())) {
                        //ToastShort(model.getMsg());
                        m.arg1 = 1;
                        Firstflag=1;

                    } else if (("-1").equals(model.getState())) {
                        m.arg1 = 2;//已上传过的图片
                        Firstflag=2;
                    } else {
                        ToastShort(model.getMsg());
                    }
                    h.sendMessage(m);
                }, error -> {

                });
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {//订单提交成功
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(TakePhotoActivity.this);
                //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示");
                dialog.setMessage("发送成功！");
                dialog.setIcon(R.mipmap.message);

                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TakePhotoActivity.this.finish();
                    }
                });
                dialog.create().show();

            } else if (msg.arg1 == 2) {//重复图片订单
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(TakePhotoActivity.this);
                //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示");
                dialog.setMessage("此照片已上传过，是否重复上传？");
                dialog.setIcon(R.mipmap.message);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendPhoto();
                    }
                });
                dialog.create().show();
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://拍照获得照片
                int x = 0;
                if (resultCode == Activity.RESULT_OK) {

                    if (flag == 1) {
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            x = bitmap.getWidth() / 500;

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        compressImage compressImage = new compressImage();
                        bitmap = compressImage.getBitmapFromUrl(bitmap, 500, bitmap.getHeight() / x);
                        TakephotoTv.setVisibility(View.GONE);
                        image_icon.setVisibility(View.GONE);
                        RetakeButton.setText("重拍");
                        PhotoImageview.setVisibility(View.VISIBLE);
                        PhotoImageview.setImageBitmap(bitmap);
                        pickname = getNonceStr();
                        photoflag = 1;
                        SendphotoButton.setBackgroundDrawable(getResources().getDrawable(R.mipmap.send));
                    }
                }
                break;
            case CROP_PHOTO://相册获得照片
                flag = 2;
                if (resultCode == RESULT_OK) {


                    bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {

                        String name = data.getData() + "";
                        pickname = name.substring(name.lastIndexOf("/") + 1, name.length());
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);

                    } else {
                        String name = data.getData() + "";
                        pickname = name.substring(name.lastIndexOf("/") + 1, name.length());
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);

                    }
                    TakephotoTv.setVisibility(View.GONE);
                    image_icon.setVisibility(View.GONE);
                    PhotoImageview.setVisibility(View.VISIBLE);
                    PhotoImageview.setImageBitmap(bitmap);
                    photoflag = 1;
                    SendphotoButton.setBackgroundDrawable(getResources().getDrawable(R.mipmap.send));
                }
                break;

            default:
                break;
        }
    }

    /**
     * 生成随机号，防重发
     */
    private String getNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    /**
     * 网络操作相关的子线程
     */
//    Runnable networkTask = new Runnable() {
//
//        @Override
//        public void run() {
//            // TODO
//            // 在这里进行 http request.网络请求相关操作
//            PostIoImage ioImage = new PostIoImage();
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("togoid", Utils.getCache(Key.KEY_UserId));
//            map.put("ext", "jpg");
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            String photo = Base64.encodeToString(baos.toByteArray(), 0, baos.toByteArray().length, Base64.DEFAULT);
//            final Map<String, File> files = new HashMap<String, File>();
//            files.put("uploadfile", outputImage);
//            String str = null;
//            try {
//                str = ioImage.post("http://kuaipao.myejq.com/App/shop/kuaipaoAddOrderImg.aspx", map, photo);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Message msg = new Message();
//            Bundle data = new Bundle();
//            data.putString("value", str);
//            msg.setData(data);
//            handler.sendMessage(msg);
//        }
//    };
    @Override
    public void onClick(View view) {
        flag = 1;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(TakePhotoActivity.this, "请检测sd是否可用", Toast.LENGTH_SHORT).show();
            return;

        }
        outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        try {

            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
            imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TATE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);


    }

    /**
     * 最原始传照片方法
     */
    public String communication01(String urlString, Map<String, String> map, byte[] bytt) {
        String result = "";

        String end = "\r\n";
        if (!urlString.equals("")) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setDoInput(true);// 允许输入
                conn.setDoOutput(true);// 允许输出
                conn.setUseCaches(false);// 不使用Cache
                conn.setConnectTimeout(6000);// 6秒钟连接超时
                conn.setReadTimeout(6000);// 6秒钟读数据超时
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                //StringBuilder localStringBuilder1 = new StringBuilder();
                Iterator localIterator = map.entrySet().iterator();

                while (true) {
                    if (!localIterator.hasNext()) {
                        break;
                    }

                    Map.Entry localEntry = (Map.Entry) localIterator.next();
                    conn.setRequestProperty((String) localEntry.getKey(), URLEncoder.encode((String) localEntry.getValue(),
                            "UTF-8"));
                }
                /*
                 * conn.setRequestProperty("id", "1");
				 * conn.setRequestProperty("type", "1");
				 * conn.setRequestProperty("ext", "jpg");
				 */
                // / type=1 表示上传商家图片 id 表示商家编号
                // / type=2 表示上传菜品图片 id 表示菜品编号
                // / ext=jpg 表示后缀名

                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.write(bytt, 0, bytt.length);
                dos.writeBytes(end);
                dos.flush();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();
                String s = "";
//                listener.action(260, result, Tag);
            } catch (Exception e) {
                result = "{\"ret\":\"898\"}";
//                listener.action(258, result, Tag);
            }
        }
        return result;
    }


  /*  @Subscribe
    public void onEvent(AnyEventType event) {
        chooseuri =event.getMessage();
    }
*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}
