package com.builtio.contentstack;

import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by shailesh on 25/10/17.
 */

public class ImageOPTestcase extends ApplicationTestCase<TestActivity> {

    private final String TAG = "ImageTransformationTestcase";
    private final String IMAGE_URL = "https://images.contentstack.io/v3/assets/blt903007d63561dea2/blt638399801b6bd23c/59afa6406c11eb860ddf04aa/download";
    private CountDownLatch startSignal;
    private Context context;
    private Stack stack;
    private final String DEFAULT_APPLICATION_KEY = "blt903007d63561dea2";
    private final String DEFAULT_ACCESS_TOKEN = "blte31e27332a44557e";
    private final String DEFAULT_ENV = "Gal_dev";
    private LinkedHashMap imageParams = new LinkedHashMap();


    public ImageOPTestcase() {
        super(TestActivity.class);
    }


    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV, config);
        startSignal = new CountDownLatch(1);
    }



    public void test01_ImageOptimization(){

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");
        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    Log.e(TAG, "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            startSignal.countDown();
            Log.e(TAG, "Testcases Passed");
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }
    }







    public void test02_ImageOptimization(){

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");
        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    Log.e(TAG, "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            startSignal.countDown();
            Log.e(TAG, "Testcases Passed");
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }
    }




    public void test03_ImageOptimization(){

        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    Log.e(TAG, "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            startSignal.countDown();
            Log.e(TAG, "Testcases Passed");
        }else {
            Log.e(TAG, "Testcases Failed");
            try{
                startSignal.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }
    }


}
