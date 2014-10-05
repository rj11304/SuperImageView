package com.superview.imageview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

public class Util {

	public static InputStream readWallpaper(Context context,Uri uri){  //通过uri获取图片文件的输出流
		if (uri == null) {
            throw new IllegalArgumentException("Uri cannot be empty");
        }
        String scheme = uri.getScheme();
        InputStream in = null;
        if ("content".equals(scheme)) {
            try {
                in = context.getContentResolver().openInputStream(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("file".equals(scheme)) {
            List<String> segments = uri.getPathSegments();
            if (segments != null && segments.size() > 1
                    && "android_asset".equals(segments.get(0))) {
                AssetManager assetManager = context.getAssets();
                StringBuilder assetPath = new StringBuilder();
                for (int i = 1; i < segments.size(); i++) {
                    if (i > 1) {
                        assetPath.append("/");
                    }
                    assetPath.append(segments.get(i));
                }
                try {
                    in = assetManager.open(assetPath.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    in = new FileInputStream(new File(uri.getPath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if("http".equals(scheme)){
        	try {
				URL url = new URL(uri.toString());
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setDoInput(true);
				con.connect();
				con.setConnectTimeout(6000);
				in = con.getInputStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return in;
	}
}
