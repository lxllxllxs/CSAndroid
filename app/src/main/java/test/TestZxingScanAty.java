package test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/28.
 */
public class TestZxingScanAty extends BaseActivity {
    @InjectView(R.id.tv_jump)
    TextView tvJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity_);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.tv_jump)
    public void onClick() {
        Intent intent = new Intent(TestZxingScanAty.this, CaptureActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK!=resultCode){
            return;
        }
        String result=data.getStringExtra("result");
        tvJump.setText(result);

    }
}
