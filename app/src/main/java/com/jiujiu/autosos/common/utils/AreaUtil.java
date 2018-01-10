package com.jiujiu.autosos.common.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.common.model.AreaModel;
import com.litesuits.common.io.IOUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */

public class AreaUtil {
    private AreaUtil() {

    }

    public static List<AreaModel> getArea(Context context) {
        String json;
        List<AreaModel> list = new ArrayList<>();
        try {
            json = IOUtils.toString(context.getAssets().open("area.json"));
            list = new Gson().fromJson(json, new TypeToken<List<AreaModel>>() {
            }.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
