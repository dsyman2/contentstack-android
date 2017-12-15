package com.builtio.contentstack;

import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by chinmay on 1/6/16.
 */
public class AssetTestCase extends ApplicationTestCase<TestActivity> {

    private static final String TAG = "TESTCASE";
    CountDownLatch startSignal;
    Context context;
    Stack stack;

    public static final String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
    public static final String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
    public static final String DEFAULT_ENV = "env1";

    public AssetTestCase() {
        super(TestActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        context = getContext();

        //stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV);
        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV,config);

        //Counter lock for wait
        startSignal = new CountDownLatch(1);
    }


    public void test01_Asset_getAsset(){

        final Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + entry.toJSON());

                    Asset asset = entry.getAsset("imagefile");

                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.toJSON());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getFileType());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getCreatedBy());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getUpdatedBy());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getFileName());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getFileSize());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getAssetUid());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getUrl());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getCreateAt().getTime());
                    Log.w(TAG, "----------Test--Asset-01--Success---------" + asset.getUpdateAt().getTime());


                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--01--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--01--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--01--Error---------" + error.getErrors());
                }

            }
        });

        try{
            startSignal.await();

        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }//resources

    public void test02_Asset_getAssets(){

        final Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    Log.w(TAG, "----------Test--ENTRY-02--Success---------" + entry.toJSON());

                    List<Asset> assets = entry.getAssets("file");

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.toJSON());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileType());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreatedBy());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileName());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileSize());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getAssetUid());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUrl());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        Log.w(TAG, "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--02--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--02--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--02--Error---------" + error.getErrors());
                }

            }
        });

        try{
            startSignal.await();

        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test03_Asset_fetch(){
        final Object result[] = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.toJSON());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileType());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileName());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileSize());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUrl());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    startSignal.countDown();
                }else {
                    result[0] = error;
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrors());
                    startSignal.countDown();
                }

            }
        });

        try{
            startSignal.await();
            assertEquals(true, result[0] instanceof Asset);
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test04_AssetLibrary_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {
                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.toJSON());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileType());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreatedBy());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileName());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileSize());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getAssetUid());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUrl());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        Log.w(TAG, "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--04--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--04--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--04--Error---------" + error.getErrors());
                }
            }
        });

        try{
            startSignal.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test05_AssetLibrary_includeCount_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    System.out.println("count = [" + assetLibrary.getCount() + "]");

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.toJSON());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileType());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreatedBy());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileName());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileSize());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getAssetUid());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUrl());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            startSignal.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test06_AssetLibrary_includeRelativeUrl_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.toJSON());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileType());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreatedBy());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileName());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileSize());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getAssetUid());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUrl());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        Log.w(TAG, "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            startSignal.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test07_AssetLibrary_setCachePolicy_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.setCachePolicy(CachePolicy.NETWORK_ONLY);
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.toJSON());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getFileType());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getCreatedBy());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getFileName());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getFileSize());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getAssetUid());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getUrl());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        Log.w(TAG, "----------Test--Asset-07--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    startSignal.countDown();
                } else {
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            startSignal.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    public void test08_Asset_setCachePolicy_fetch(){
        final Object result[] = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.setCachePolicy(CachePolicy.NETWORK_ONLY);
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.toJSON());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileType());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileName());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileSize());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUrl());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getTags().length);
                    result[0] = asset;
                    startSignal.countDown();
                }else {
                    result[0] = error;
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrors());
                    startSignal.countDown();
                }

            }
        });

        try{
            startSignal.await();
            assertEquals(true, result[0] instanceof Asset);
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

    /************************************************************/

    public void test11_Asset_addParams(){
        final Object result[] = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.addParam("key", "some_value");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.toJSON());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileType());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileName());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getFileSize());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUrl());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    Log.w(TAG, "----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    startSignal.countDown();
                }else {
                    result[0] = error;
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrorCode());
                    Log.w(TAG, "----------Test--Asset--03--Error---------" + error.getErrors());
                    startSignal.countDown();
                }

            }
        });

        try{
            startSignal.await();
            assertEquals(true, result[0] instanceof Asset);
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }

}
