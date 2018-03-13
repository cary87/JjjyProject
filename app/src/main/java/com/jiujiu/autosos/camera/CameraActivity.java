package com.jiujiu.autosos.camera;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.cameraview.CameraView;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.order.model.PictureTypeEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_INDEX;
import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_TAG;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class CameraActivity extends AbsBaseActivity {
    public static final String TAG = CameraActivity.class.getSimpleName();

    @BindView(R.id.camera)
    CameraView mCameraView;
    @BindView(R.id.iv_tip_pic)
    ImageView ivTipPic;
    @BindView(R.id.tv_count_down_take)
    TextView takePicture;

    private Handler mBackgroundHandler;

    /**
     * 哪种业务拍照
     */
    private PictureTypeEnum mTag = PictureTypeEnum.arrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        PictureTypeEnum pictureTypeEnum = (PictureTypeEnum) getIntent().getSerializableExtra(PHOTO_TAG);
        if (pictureTypeEnum != null) {
            mTag = pictureTypeEnum;
        }
        switch (mTag) {
            case arrive:
                ivTipPic.setBackgroundResource(R.drawable.car_no_tip_pic);
                countDownToEnableTake();
                break;
            case survery:
                int picIndex = getIntent().getIntExtra(PHOTO_INDEX, 1);
                Class drawable = R.drawable.class;
                Field field;
                try {
                    field = drawable.getField("kancha" + picIndex);
                    int res_ID = field.getInt(field.getName());
                    ivTipPic.setBackground(getDrawable(res_ID));
                    takePicture.setEnabled(false);
                    countDownToEnableTake();
                } catch (Exception e) {
                }
                break;
            case vin:
                ivTipPic.setBackgroundResource(R.drawable.vin_tip);
                countDownToEnableTake();
                break;
            case construction:
                ivTipPic.setBackgroundResource(R.drawable.construction_tip);
                countDownToEnableTake();
                break;
        }
    }

    /**
     * 倒计时拍照
     */
    private void countDownToEnableTake() {
        final int count = 3;
        Observable.interval(1, TimeUnit.SECONDS).take(count + 1).map(new Function<Long, Integer>() {
            @Override
            public Integer apply(Long aLong) throws Exception {
                return count - aLong.intValue();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer o) {
                takePicture.setText(o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ivTipPic.setVisibility(View.GONE);
                takePicture.setText("");
                takePicture.setEnabled(true);
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_camera;
    }

    @OnClick(R.id.tv_count_down_take)
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
