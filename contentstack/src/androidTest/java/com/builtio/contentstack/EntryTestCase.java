package com.builtio.contentstack;

import android.content.Context;
import android.test.ApplicationTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by chinmay on 22/8/16.
 */
public class EntryTestCase extends ApplicationTestCase<TestActivity> {

    private static final String TAG = "TESTCASE";
    public static final String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
    public static final String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
    public static final String DEFAULT_ENV = "env1";

    CountDownLatch latch;
    Stack stack;
    Context context;
    String[] uidArray;

    public EntryTestCase() {
        super(TestActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        context = getContext();

        stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV);

        uidArray = new String[]{"blte88d9bec040e7c7c", "bltdf783472903c3e21"};

        //Counter lock for wait
        latch = new CountDownLatch(1);
    }

    public void test_00_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        assertEquals(entry.getString("title"), "laptop");
    }

    public void test_01_only_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.only(new String[]{"price"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        boolean isContains = false;
        if (entry.toJSON().has("title")) {
            isContains = true;
        }
        assertFalse(isContains);
    }

    public void test_02_except_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.except(new String[]{"title"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        boolean isContains = false;
        if (entry.toJSON().has("title")) {
            isContains = true;
        }
        assertFalse(isContains);
    }

    public void test_03_includeReference_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.includeReference("category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
        try {
            assertTrue(categoryArray.get(0) instanceof JSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_04_includeReferenceOnly_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");

        entry.onlyWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

        try {
            JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
            JSONObject jsonObject = categoryArray.getJSONObject(0);
            boolean isContains = false;
            if (jsonObject.has("title")) {
                isContains = true;
            }
            assertTrue(isContains);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_05_includeReferenceExcept_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");

        entry.exceptWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        try {
            JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
            JSONObject jsonObject = categoryArray.getJSONObject(0);
            boolean isContains = false;
            if (jsonObject.has("title")) {
                isContains = true;
            }
            assertFalse(isContains);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_06_getMarkdown_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        String htmlString = entry.getHtmlText("markdown");
        assertNotNull(htmlString);

    }

    public void test_07_getMarkdown_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        ArrayList<String> htmlString = entry.getMultipleHtmlText("markdown_multiple");
        assertEquals(2, htmlString.size());

    }

    public void test_08_get() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        String title = (String) entry.get("title");
        assertNotNull(title);
    }
}
