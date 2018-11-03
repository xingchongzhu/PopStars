/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.develop.PopStars;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class PermissionsUtil {
    static final String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static List<String> checkouPermissions(Context context){
    	List<String> needPermissions = new ArrayList<String>();
    
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    		for(String str:permissions){
    			Log.d("zxc331", "requestPermission need str = "+str);
    			if( ContextCompat.checkSelfPermission(context, str) != PackageManager.PERMISSION_GRANTED){
    				needPermissions.add(str);
    			}
    		}
    	}
    	return needPermissions;
    }
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static void requestPermission(Activity context) {
    	List<String> needPermissions = checkouPermissions(context);
    	Log.d("zxc331", "requestPermission need size = "+needPermissions.size());
    	if(needPermissions.size() > 0)
    		ActivityCompat.requestPermissions(context, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_ASK_PERMISSIONS);
    }
}