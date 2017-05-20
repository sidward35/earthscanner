/*
 * Copyright (C) The Android Open Source Project
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

package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    private Bundle x;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x=savedInstanceState;
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    /*@Override
    public void onBackPressed(){
        if(this.findViewById(android.R.id.content).equals(R.layout.product_info)){
            setContentView(R.layout.activity_main);
            barcodeValue.setText("");
        }else super.onBackPressed();
    }*/

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {                                                                 //dis where the meat at boiiii
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText(barcode.displayValue);
                    setContentView(R.layout.product_info);
                    ImageView img = (ImageView)findViewById(R.id.imageView);
                    TextView tv1 = (TextView)findViewById(R.id.titleView), tv2 = (TextView)findViewById(R.id.descView), tv3 = (TextView)findViewById(R.id.ratingView);
                    if(barcode.displayValue.equals("demo1")){
                        img.setBackgroundResource(R.drawable.bottle);
                        tv1.setText("Green Works Eco-Friendly Biodegradable Water Bottle");
                        tv2.setText("Save tons of plastic from being produced by using our reusable Eco Bottle instead of cheap disposable bottles. It's BPA free and made with clean, and durable stainless steel.");
                        tv3.setText("Rating: 10/10");
                    }else if(barcode.displayValue.equals("demo2")){
                        img.setBackgroundResource(R.drawable.cigarettes);
                        tv1.setText("Marlboro Cigarettes 20-Pack");
                        tv2.setText("Vintage 1988 Marlboro Wooden Stick Matches-New in Unopened Box\n" +
                                "Flip Up Top Miniature Box- Approximately 3\" H x 1 3/8\" W\n" +
                                "Made in Germany Exclusively for The Phillip-Morris Co. New York 10017\n" +
                                "Comes with 20 Wooden Stick Matches");
                        tv3.setText("Rating: 1/10");
                    }else if(barcode.displayValue.equals("demo3")){
                        img.setBackgroundResource(R.drawable.chips);
                        tv1.setText("Lay's Chips Classic");
                        tv2.setText("10 oz. bag of Lay's Original Potato Chips\n" +
                                "Made with three ingredients: potatoes, oil and salt\n" +
                                "Gluten free\n" +
                                "Delicious snack for splitting with the whole family\n" +
                                "Pair with your favorite Frito-Lay dip");
                        tv3.setText("Rating: 5/10");
                    }
                    Button goBack = (Button)findViewById(R.id.button2);
                    goBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onCreate(x);
                        }
                    });
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
