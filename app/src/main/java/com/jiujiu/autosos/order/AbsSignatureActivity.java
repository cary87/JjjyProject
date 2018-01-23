package com.jiujiu.autosos.order;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.resp.FileUploadResp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/17.
 */

public abstract class AbsSignatureActivity extends AbsBaseActivity {

    public interface OnSaveCompleteListener {
        void onComplete(String path);
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    public void saveSignature(Bitmap backgroundBitmap, Bitmap signatureBitmap, final OnSaveCompleteListener listener) {
        if (signatureBitmap != null) {
            if (backgroundBitmap != null) {
                signatureBitmap = drawBg4Bitmap(ContextCompat.getColor(this,R.color.translucent), signatureBitmap);
                signatureBitmap = mergeBitmap(backgroundBitmap, signatureBitmap);
            }
            File file = saveSignaturePicture(signatureBitmap);
            if (file != null && file.exists()) {
                HashMap<String, String> params = new HashMap<>();
                params.put("key", "attach");
                UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        handleError(e);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        LogUtils.i("wzh", progress + "");
                    }

                    @Override
                    public void onResponse(FileUploadResp resp, int i) {
                        LogUtils.i("wzh", resp.toString());
                        if (listener != null) {
                            listener.onComplete(resp.getData().getPath());
                        }
                    }
                });
            }
        }
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     * @param backBitmap 在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {

        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            LogUtils.e("wzh", "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect  = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
        return bitmap;
    }

    private File getPicDir(String albumName) {
        File file = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            LogUtils.e("wzh", "Directory not created");
        }
        return file;
    }

    private File saveSignaturePicture(Bitmap signature) {
        File photo = null;
        try {
            photo = new File(getPicDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            return photo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
}
