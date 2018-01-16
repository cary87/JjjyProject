package com.jiujiu.autosos.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiujiu.autosos.R;

import java.util.Collection;

/**
 * 对话框工具类
 */
public class DialogUtils {

    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /**
     * 显示单选对话框
     *
     * @param context
     * @param title
     * @param c
     * @param defaultPosition
     * @param callback
     */
    public static void showSingleChoiceDialog(Context context, String title, Collection c, int defaultPosition, MaterialDialog.ListCallbackSingleChoice callback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .items(c)
                .itemsCallbackSingleChoice(defaultPosition, callback)
                .positiveText(context.getResources().getString(R.string.positive))
                .negativeText(context.getResources().getString(R.string.negative))
                .show();
    }

    /**
     * 显示多选对话框
     *
     * @param context
     * @param title
     * @param c
     * @param defaultPositionList
     * @param callback
     */
    public static void showMultiChoiceDialog(Context context, String title, Collection c, Integer[] defaultPositionList, MaterialDialog.ListCallbackMultiChoice callback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .items(c)
                .itemsCallbackMultiChoice(defaultPositionList, callback)
                .positiveText(context.getResources().getString(R.string.positive))
                .negativeText(context.getResources().getString(R.string.negative))
                .show();
    }

    /**
     * 显示输入对话框
     *
     * @param context
     * @param title
     * @param content
     * @param inputType
     * @param inputHint
     * @param callback
     */
    public static void showInputDialog(Context context, String title, String content, int inputType, String inputHint, MaterialDialog.InputCallback callback) {
        new MaterialDialog.Builder(context)
                .title(title)
                //.content(content)
                .inputType(inputType)
                //.inputRange()
                .input(inputHint, content, false, callback)
                .positiveText(context.getResources().getString(R.string.positive))
                .negativeText(context.getResources().getString(R.string.negative))
                .show();
    }

    public static void showLoadingDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .progress(true, 100)
                .content(content)
                .canceledOnTouchOutside(false)
                .show();
    }

    public static Dialog getLoadingDialog(Context context, String content) {
        return new MaterialDialog.Builder(context)
                .progress(true, 100)
                .content(content)
                .canceledOnTouchOutside(false)
                .build();
    }

    public static void showSingleChoiceListDialog(Context context, Collection c, int defaultPosition, MaterialDialog.ListCallbackSingleChoice callback, DialogInterface.OnCancelListener cancelListener) {
        new MaterialDialog.Builder(context)
                .items(c)
                .itemsCallbackSingleChoice(defaultPosition, callback)
                .cancelListener(cancelListener)
                .show();
    }

    public static void showConfirmDialog(Context context, String title, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context).title(title)
                .positiveText(context.getResources().getString(R.string.positive))
                .canceledOnTouchOutside(false)
                .onAny(callback)
                .show();
    }

    public static void showConfirmDialogWithCancel(Context context, String title, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context).title(title)
                .positiveText(context.getResources().getString(R.string.positive))
                .negativeText("否")
                .canceledOnTouchOutside(false)
                .onAny(callback)
                .show();
    }

    public static void showMessageDialog(Context context, String message, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context).title(message)
                .positiveText(context.getResources().getString(R.string.positive))
                .canceledOnTouchOutside(false)
                .onAny(callback)
                .show();
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

}
