package com.example.liaohaicongsx.coc.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.MyImageLoader.MyImageLoader;


/**
 * Created by liaohaicongsx on 2017/05/15.
 */
public class ImageFragment extends DialogFragment {

    public static final String KEY_IMG_URL = "imgUrl";

    public static ImageFragment newInstance(String imgUrl) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_IMG_URL, imgUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String imgUrl = getArguments().getString(KEY_IMG_URL);
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        MyImageLoader.getInstance(getActivity().getApplicationContext()).displayImage(imgUrl, imageView);
        builder.setView(imageView);
        return builder.create();
    }
}
