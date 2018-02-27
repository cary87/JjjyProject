package com.jiujiu.autosos.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.code19.library.DensityUtil;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.RefreshViewEvent;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2018/1/9.
 */

public class OrderDialog extends Dialog {
    @BindView(R.id.tv_order_title)
    TextView tvOrderTitle;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_ignore)
    TextView tvIgnore;
    @BindView(R.id.btn_accept_order)
    Button btnAcceptOrder;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.btn_show_location)
    ImageButton btnLocation;
    private AbsBaseActivity context;
    private OrderModel order;

    private OnAcceptOrderListener mListener;

    public OrderDialog(@NonNull AbsBaseActivity context, OrderModel order, OnAcceptOrderListener listener) {
        super(context);
        this.context = context;
        this.order = order;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(context, R.layout.dialog_order, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = DensityUtil.dip2px(context,420);
        lp.width = DensityUtil.dip2px(context,320);
        win.setAttributes(lp);

        setCancelable(false);

        tvOrderTitle.setText(OrderUtil.getOrderTypeName(order) + "订单");
        tvPhone.setText(order.getCarOwnerId() + "");
        tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvPhone.getPaint().setAntiAlias(true);//抗锯齿
        tvAddress.setText(order.getAddress());
        tvRemark.setText(TextUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());
        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setDriverId(Long.parseLong(UserStorage.getInstance().getUser().getUserId()));
                order.saveAsync().listen(new SaveCallback() {
                    @Override
                    public void onFinish(boolean success) {
                        if (success) {
                            EventBus.getDefault().post(new RefreshViewEvent());
                        }
                    }
                });
                dismiss();
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhone.getText().toString()));
                context.startActivity(intent);
            }
        });
        tvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(order.getPictures())) {
                    context.showToast("没有图片");
                } else {
                    List<String> photoPaths = Arrays.asList(order.getPictures().split("\\|"));
                    ArrayList<String> absolutePaths = new ArrayList<>();
                    for (String photoPath : photoPaths) {
                        absolutePaths.add(UserStorage.getInstance().getUser().getFastDFSFileUrlPrefix() + photoPath);
                    }
                    PhotoPreview.builder()
                            .setPhotos(absolutePaths)
                            .setCurrentItem(0)
                            .setShowDeleteButton(false)
                            .start(context);
                }
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayLocationActivity.class);
                intent.putExtra("lat", order.getLatitude());
                intent.putExtra("lng", order.getLongitude());
                context.startActivity(intent);
            }
        });

        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onAcceptOrder();
                    dismiss();
                }
            }
        });
    }

    public interface OnAcceptOrderListener {
        void onAcceptOrder();
    }

    public OnAcceptOrderListener getmListener() {
        return mListener;
    }

    public void setmListener(OnAcceptOrderListener mListener) {
        this.mListener = mListener;
    }
}
