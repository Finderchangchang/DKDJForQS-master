package liuliu.qs.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.view.TitleBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import liuliu.qs.R;
import liuliu.qs.base.BaseActivity;
import liuliu.qs.config.Key;
import liuliu.qs.listener.AddressManageListener;
import liuliu.qs.method.HttpUtil;
import liuliu.qs.method.Utils;
import liuliu.qs.model.PoiModel;
import liuliu.qs.model.ShopInfo;
import liuliu.qs.view.IAddressManage;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29.
 */

public class SaveOrderActivity extends BaseActivity implements IAddressManage, RouteSearch.OnRouteSearchListener {
    public static SaveOrderActivity mIntails;
    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.center_iv)
    ImageView centerIv;
    @Bind(R.id.title_bar)
    TitleBar title_bar;
    AMap aMap;
    public AMapLocationClientOption mLocationOption = null;
    Dialog dialog;
    AddressManageListener mListener;
    String isSave;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.tel_tv)
    TextView telTv;
    @Bind(R.id.sh_address_tv)
    TextView shAddressTv;
    @Bind(R.id.fei_tv)
    TextView feiTv;
    @Bind(R.id.pay_btn)
    Button payBtn;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    @Bind(R.id.tel_et)
    EditText tel_et;
    FinalDb db;
    private RouteSearch routeSearch;
    double juli;
    String sendfee;
    double nowlat, nowlng;
    TextView user_address_tv;

    @Override
    public void initViews() {
        setContentView(R.layout.ac_save_order);
        ButterKnife.bind(this);
        db = FinalDb.create(this);
        try {
            nowlat = Double.parseDouble(Utils.getCache("nowlat"));
            nowlng = Double.parseDouble(Utils.getCache("nowlng"));
            if (nowlat != 0) {
                //逆地理位置
                Mytherea mytherea = new Mytherea();
                mytherea.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListener = new AddressManageListener(this);
        mIntails = this;
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        title_bar.setLeftClick(() -> {
            SaveOrderActivity.this.finish();
        });
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        payBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel_et.getText().toString().trim())) {
                ToastShort("请填写手机号码");
            } /*else if (!Utils.isMobileNo(tel_et.getText().toString().trim())) {
                ToastShort("请输入正确的手机号码");
            } */ else {
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.dialog_dake, null);
                TextView shop_name_tv = (TextView) dialog.findViewById(R.id.shop_name_tv);
                shop_name_tv.setText(list.get(0).getTogoname());
                TextView tel_tv = (TextView) dialog.findViewById(R.id.tel_tv);
                tel_tv.setText(list.get(0).getTogoaccount());
                TextView shop_address_tv = (TextView) dialog.findViewById(R.id.shop_address_tv);
                shop_address_tv.setText(list.get(0).getAddress());
                user_address_tv = (TextView) dialog.findViewById(R.id.user_address_tv);
                if (Hnum.length() == 0 || Hnum.equals("")) {
                    //user_address_tv.setText(poiModel.getPoiName());
                    String a = shAddressTv.getText().toString();
                    a = a.substring(6, a.length() - 1);
                    if (a.length() > 13) {
                        user_address_tv.setText(a.substring(0, 13) + "...");
                    } else {
                        user_address_tv.setText(a);
                    }
                } else {
                    String addHnumStr = poiModel.getPoiName() + "，详细地址:" + Hnum;
                    if (addHnumStr.length() > 13) {
                        user_address_tv.setText(addHnumStr.substring(0, 13) + "...");
                    } else
                        user_address_tv.setText(addHnumStr);
                }
                TextView user_tel_tv = (TextView) dialog.findViewById(R.id.user_tel_tv);
                user_tel_tv.setText(tel_et.getText().toString().trim());
                TextView fei_tv = (TextView) dialog.findViewById(R.id.fei_tv);//订单配送费
                fei_tv.setText(sendfee + "元");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("取消", (dialog12, which) -> {

                });
                builder.setNegativeButton("确定", (dialog1, which) -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("togoid", Utils.getCache(Key.KEY_UserId));
                    map.put("ulat", poiModel.getLat() + "");
                    map.put("ulng", poiModel.getLng() + "");
                    map.put("utel", user_tel_tv.getText().toString().trim());
                    if (Hnum.length() == 0 || Hnum.equals("") || nowlat == 0.00) {//如果传递过来的手选地址为空
                        map.put("diZhi", URLEncoder.encode(poiModel.getPoiName()));
                    } else if (Hnum.length() != 0 || !Hnum.equals("") || nowlat == 0.00) {
                        map.put("diZhi", URLEncoder.encode(poiModel.getPoiName() + "，详细地址" + Hnum));
                    } else {
                        map.put("dizhi", address);//网络获得逆地理地址
                    }
                    map.put("juLi", juli + "");
                    map.put("sendFee", sendfee);
                    HttpUtil.load().addOrder(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(model -> {
                                ToastShort(model.getState());
                                finish();
                            }, error -> {
                                ToastShort("请检查网络连接");
                            });
                });
                builder.setView(dialog);
                builder.show();
            }
        });

    }

    private void closeThis() {
        Intent intent = new Intent();
        intent.putExtra("val", poiModel);
        setResult(8, intent);
        SaveOrderActivity.this.finish();
    }


    @Override
    public void manageResult(boolean result) {
        if (result) {
            closeThis();
            ToastShort("提交成功");
        } else {
            ToastShort("请检查网络连接是否正常");
        }
    }

    List<PoiModel> pois;
    String aid;
    List<ShopInfo> list;
    String tel;
    String Hnum;//从activity接收到的门牌号信息
    RouteSearch.FromAndTo fromAndTo;

    @Override
    public void initEvents() {
        dialog = Utils.ProgressDialog(this, "定位中，请稍后...", true);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        Serializable modd = getIntent().getSerializableExtra("model");
        tel = getIntent().getStringExtra("tel");
        Hnum = getIntent().getStringExtra("Hnum");

        if (tel != null) {
            tel_et.setText(tel);
            tel_et.setSelection(tel.length());
        }

        isSave = getIntent().getStringExtra("add");
        if (modd != null) {
            poiModel = (PoiModel) modd;
            aid = poiModel.getAid();
        }
        list = db.findAll(ShopInfo.class);
        try {
            if (list != null) {
                addressTv.setText("商家的地址：" + list.get(0).getAddress());
                if (Hnum.length() == 0 || Hnum.equals("")) {
                    if (poiModel != null)
                        shAddressTv.setText("收货人地址：" + poiModel.getPoiName());
                } else {
                    if (poiModel != null)
                        shAddressTv.setText("收货人地址：" + poiModel.getPoiName() + "，详细地址：" + Hnum);
                }
                if (nowlng == 0 || nowlat == 0) {//如果是手动选择的就用系统的
                    if (poiModel != null)
                        fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(Double.parseDouble(list.get(0).getLat()),
                                Double.parseDouble(list.get(0).getLng())), new LatLonPoint(poiModel.getLat(), poiModel.getLng()));
                } else {
                    fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(Double.parseDouble(list.get(0).getLat()),
                            Double.parseDouble(list.get(0).getLng())), new LatLonPoint(nowlat, nowlng));
                }
                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
                routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请重新选择地址", Toast.LENGTH_SHORT);
        }
        pois = new ArrayList<>();
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
            if (poiModel != null) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(poiModel.getLat(), poiModel.getLng()), 14));
            } else {
                dialog.show();
            }
        }
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        }

    }

    PoiModel poiModel;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (i == 1000 && walkRouteResult != null) {
            List<LatLng> latLngs = new ArrayList<LatLng>();
            if (walkRouteResult.getPaths().size() > 0) {
                WalkPath path = walkRouteResult.getPaths().get(0);
                double length = 0;
                for (WalkPath path1 : walkRouteResult.getPaths()) {
                    length += path1.getDistance();
                    for (WalkStep walkStep : path1.getSteps()) {
                        for (LatLonPoint point : walkStep.getPolyline()) {
                            latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                        }
                    }
                }
                juli = Double.valueOf(length) / 1000;
                aMap.addPolyline(new PolylineOptions().
                        addAll(latLngs).width(5).color(Color.argb(255, 1, 1, 1)));
                if (juli <= 0) {
                    ToastShort("送货距离有问题，请联系业务员~~");
                } else {
                    aMap.addMarker(new MarkerOptions().position(latLngs.get(0)));
                    if (latLngs.size() >= 1) {
                        aMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)));
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("togoid", Utils.getCache(Key.KEY_UserId));
                    map.put("gonglishu", juli + "");
                    HttpUtil.load().getPSFei(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(model -> {
                                feiTv.setText("订单配送费：" + model.getSendfee() + "元");
                                sendfee = model.getSendfee();
                                aMap.addPolyline(new PolylineOptions().
                                        addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
                            }, error -> {
                                feiTv.setText("算费失败");
                            });
                }
            }
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private String mapUriStr = "http://maps.google.cn/maps/api/geocode/json?latlng={0},{1}&sensor=true&language=zh-CN";
    private HttpEntity httpEntity = null;
    private String result;
    String address;

    /**
     * 访问网络获得逆地址
     */
    class Mytherea extends Thread {
        @Override
        public void run() {
            super.run();
            String uristr = MessageFormat.format(mapUriStr, nowlat, nowlng);
            HttpGet httpGet = new HttpGet(uristr);
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse response = httpClient.execute(httpGet);
                httpEntity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                result = "";

                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                //  result=result.substring(0,getCharacterPosition(result))+"]}]}";

                if (result != null) {//返回的具体地址
                    JSONObject object = new JSONObject(result.toString().trim().replace(" ", ""));
                    JSONArray array = new JSONArray(object.getString("results").toString());
                    JSONObject object1 = array.optJSONObject(0);//"address_components"
                    if (object1 != null) {
                        String addressname = object1.getString("formatted_address");
                        address = addressname;
                    }
                } else {
                    address = address + "";
                }
                if (address.length() > 0) {
                    Message m = new Message();
                    h.sendMessage(m);
                    Log.e("address", address);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getCharacterPosition(String string) {
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile("}").matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == 2) {
                break;
            }
        }
        return slashMatcher.start();
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            address = address.substring(8, address.length() - 1);//中国辽宁朝阳市截掉
            int i = address.indexOf("邮政编码");
            if (i != -1) {
                address = address.substring(0, i);
            }
            if (Hnum.length() == 0 || Hnum.equals("")) {
                shAddressTv.setText("收货人地址：" + poiModel.getPoiName() + ",详细地址:" + address);
            } else {
                shAddressTv.setText("收货人地址：" + poiModel.getPoiName() + ",详细地址：" + address + Hnum);
            }
        }
    };
}
