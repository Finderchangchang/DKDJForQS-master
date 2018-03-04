package liuliu.qs.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import liuliu.qs.R;
import liuliu.qs.base.BaseActivity;

/**
 * Created by XY on 2017/8/14.
 */

public class SeePhotoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.big_photo)
    ImageView BigPhoto;
    @Bind(R.id.back_tv1)
    TextView BackTv;
    @Bind(R.id.imgNumber_tv)
    TextView imgNumberTv;
    String imgOrder, imgNumber;

    @Override
    public void initViews() {
        setContentView(R.layout.ac_seephoto);
        ButterKnife.bind(this);
        imgOrder = getIntent().getStringExtra("imgOrder");
        imgNumber = getIntent().getStringExtra("imgNumber");


    }

    @Override
    public void initEvents() {
        BackTv.setOnClickListener(this);
        if(imgOrder!=null){
        Picasso.with(this).load(imgOrder).into(BigPhoto);}
        imgNumberTv.setText(imgNumber);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_tv1) {
            finish();
        }
    }
}
