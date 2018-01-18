package com.jiujiu.autosos.order;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;

import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.model.TakePhotoEvent;
import com.jiujiu.autosos.resp.FileUploadResp;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/17.
 */

public abstract class AbsSignatureActivity extends AbsBaseActivity {

    public interface OnSaveCompleteListener {
        void onComplete();
    }

    private List<String> paths = new ArrayList<>();

    public void saveSignature(Bitmap signatureBitmap, final OnSaveCompleteListener listener) {
        if (signatureBitmap != null) {
            File file = saveSignaturePicture(signatureBitmap);
            if (file != null && file.exists()) {
                showLoadingDialog("保存中");
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
                        hideLoadingDialog();
                        LogUtils.i("wzh", resp.toString());
                        paths.add(resp.getData().getPath());
                        EventBus.getDefault().post(new TakePhotoEvent(-1, paths));
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                });
            }
        }
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
