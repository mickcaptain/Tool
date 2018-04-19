package cn.mickcaptain.toolutil.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiepengtao on 2017/8/3.
 */

public class PictureUtil {

    public static PictureUtil getIntent() {
        return Sington.instance;
    }

    private static class Sington {
        private static PictureUtil instance = new PictureUtil();

    }

    /**
     *  加工保存圆形bitmap
     *
     * @param mBitmap
     */
    public void saveFile(Bitmap mBitmap) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/flw/photo/";
        long mNowTime = System.currentTimeMillis();
        SimpleDateFormat nowTimeformat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date(mNowTime);
        String fileName = nowTimeformat.format(nowDate);
        File files = new File(dir);

        boolean isExists = false;

        if (!files.exists()) {
            isExists = files.mkdirs();
        } else {
            isExists = true;
        }

        if (!isExists) {
            return;
        }

        FileOutputStream out = null;
        try {
            File file = new File(dir + fileName + ".jpg");
            out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            //保存失败的的提醒
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

}
