package com.jiujiu.autosos.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code19.library.DateUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.order.model.OrderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/7.
 */

public class CardHolder {
    @BindView(R.id.tv_occur_address)
    TextView tvOccurAddress;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_time_name)
    TextView tvTimeName;
    @BindView(R.id.card_pic_num)
    TextView tvPicNum;
    @BindView(R.id.card_image_view)
    ImageView firstImageView;

    public CardHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void bindData(OrderModel item) {
        this.tvOccurAddress.setText(item.getAddress());
        this.tvTimeName.setText(DateUtils.formatDataTime(item.getOrderTime()));
        if (TextUtils.isEmpty(item.getPictures())) {
            Glide.with(firstImageView.getContext()).load("file:///android_asset/wall04.jpg").into(firstImageView);
        } else {
            List<String> photoPaths = Arrays.asList(item.getPictures().split("\\|"));
            ArrayList<String> absolutePaths = new ArrayList<>();
            for (String photoPath : photoPaths) {
                absolutePaths.add(UserStorage.getInstance().getUser().getFastDFSFileUrlPrefix() + photoPath);
            }
            this.tvPicNum.setText(absolutePaths.size() + "");
            Glide.with(firstImageView.getContext()).load(absolutePaths.get(0)).into(firstImageView);
        }
        this.tvServiceType.setText(OrderUtil.getOrderTypeName(item));
    }
}
