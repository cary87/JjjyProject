package com.jiujiu.autosos.camera;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_TAG;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class CameraActivity extends AbsBaseActivity {
    public static final String TAG = CameraActivity.class.getSimpleName();

    @BindView(R.id.camera)
    CameraView mCameraView;

    private Handler mBackgroundHandler;

    /**
     * 哪种业务拍照
     */
    private int mTag = -1;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        mTag = getIntent().getIntExtra(PHOTO_TAG, -1);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_camera;
    }

    @OnClick(R.id.take_picture)
    public void onViewClicked() {
        if (mCameraView != null) {
            mCameraView.takePicture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), "已经拍照", Toast.LENGTH_SHORT)
                    .show();
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "picture-" + System.currentTimeMillis() + ".jpg");
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                        viewPicture(file);
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
            });
        }

    };

    private void viewPicture(File file) {
        Intent intent = new Intent(this, DisplayPictureTakenActivity.class);
        intent.putExtra("url", file.getAbsolutePath());
        intent.putExtra(PHOTO_TAG, mTag);
        startActivity(intent);
        finish();
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

}
