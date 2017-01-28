package test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.yiyekeji.coolschool.ui.base.BaseActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by lxl on 2017/1/28.
 */
public class TestZxingCreateAty extends BaseActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         imageView=new ImageView(this);
        setContentView(imageView);
        createQRCode();
    }

    public void createQRCode() {
        try {
            //生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
            Bitmap qrcodeBitmap = EncodingUtils.createQRCode("lxlxllx", 600,600,null);
            if (qrcodeBitmap!=null) {
                imageView.setImageBitmap(qrcodeBitmap);
            }else{
                Toast.makeText(this,"生成二维码失败",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
