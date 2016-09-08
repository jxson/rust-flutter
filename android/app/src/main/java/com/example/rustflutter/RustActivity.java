// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.example.rustflutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.domokit.activity.ActivityImpl;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterView;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public class RustActivity extends Activity {
     static {
         System.loadLibrary("sample");
     }

    private static native String hello();

    private static final String TAG = "RustActivity";

    private FlutterView flutterView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlutterMain.ensureInitializationComplete(getApplicationContext(), null);
        setContentView(R.layout.rustflutter);

        flutterView = (FlutterView) findViewById(R.id.flutter_view);
        flutterView.runFromBundle(FlutterMain.findAppBundlePath(getApplicationContext()), null);

        flutterView.addOnMessageListener("hello",
            new FlutterView.OnMessageListener() {
                @Override
                public String onMessage(FlutterView view, String message) {
                    return onHello(message);
                }
            });
    }

    @Override
    protected void onDestroy() {
        if (flutterView != null) {
            flutterView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flutterView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        flutterView.onPostResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Reload the Flutter Dart code when the activity receives an intent
        // from the "flutter refresh" command.
        // This feature should only be enabled during development.  Use the
        // debuggable flag as an indicator that we are in development mode.
        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            if (Intent.ACTION_RUN.equals(intent.getAction())) {
                flutterView.runFromBundle(intent.getDataString(),
                                          intent.getStringExtra("snapshot"));
            }
        }
    }

    private String onHello(String json) {
        Log.i(TAG, "== Activity ==> Received hello message");

        Log.i(TAG, "== Activity ==> Calling RUST");
        final String message = hello();
        if (message == null) {
          throw new RuntimeException("RUST hello() failed");
        }

        Log.i(TAG, "== Activity ==> RUST message: " + message);
        // final String message = "Hello from Android";
        JSONObject reply = new JSONObject();
      try {
          reply.put("value", message);
      } catch (JSONException e) {
          Log.e(TAG, "JSON exception", e);
          return null;
      }

      return reply.toString();
    }
}
