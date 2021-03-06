package com.builtio.contentstack;

import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.builtio.contentstack.utilities.CSAppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by @builtio on.
 */
public class QueryTestCase  extends ApplicationTestCase<TestActivity> {

    private static final String TAG = "TESTCASE";
    public static final String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
    public static final String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
    public static final String DEFAULT_ENV = "env1";

    CountDownLatch latch;
    Stack stack;
    Context context;
    String[] uidArray;
    String[] containArray;
    ArrayList<Entry> entries = null;

    public QueryTestCase() {
        super(TestActivity.class);
    }



    protected void setUp() throws Exception {

        super.setUp();

        context = getContext();

        //stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV);

        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        stack = Contentstack.stack(context, DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV,config);


        uidArray = new String[]{"blte88d9bec040e7c7c", "bltdf783472903c3e21"};
        containArray = new String[]{"Roti Maker", "kids dress"};

        //Counter lock for wait
        latch = new CountDownLatch(1);
    }

    public void test_00_fetchAllEntries() throws InterruptedException {
        Query query = stack.contentType("product").query();
        final Object result[] = new Object[] { new Object() };
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        try{
            entries = (List<Entry>) result[0];
        } catch (ClassCastException cce){

        }
        assertEquals(27, entries.size());
    }


    public void test_01_fetchEntriesOfNonExistingContentType() throws InterruptedException {
        Query query = stack.contentType("department").query();
        final Object result[] = new Object[] { new Object() };
        final String[] s = {null};
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    s[0] = error.getErrorMessage().trim();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        try{
            entries = (List<Entry>) result [0];
        } catch (ClassCastException cce){
            //System.out.println(s[0].compareTo("The Content Type 'products' was not found. Please try again.1"));
            assertTrue(s[0].compareTo("The Content Type 'products' was not found. Please try again.1")<0);
        }
    }

    public void test_02_fetchSingleEntry() throws InterruptedException,ClassCastException {
        Query query = stack.contentType("categories").query();
        final Object result[] = new Object[]{new Object()};
        query.where("title","Women");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        Entry entry = null;
        entry = ((List<Entry>) result[0]).get(0);
        if(entry != null){
            assertEquals("Women", entry.getString("title"));
        }
    }

    public void test_03_fetchSingleNonExistingEntry()throws InterruptedException,ClassCastException {
        Query query = stack.contentType("categories").query();
        final Object result[] = new Object[]{new Object()};
        query.where("uid","blta3b58d6893d8935b");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        if(entries != null){
            assertTrue(entries.size() == 0);
        }
    }

    public void test_04_fetchEntryWithIncludeReference() throws InterruptedException {
        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.includeReference("category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        JSONArray categoryArray = (JSONArray) entries.get(0).get("category");
        try {
            assertTrue(categoryArray.get(0) instanceof JSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_05_fetchEntryNotContainedInField() throws InterruptedException {


        Query query = stack.contentType("product").query();
        query.notContainedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();

        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (Arrays.asList(containArray).contains(entry.getString("title"))) {
                    isContains = true;
                }
            }
            assertTrue(!isContains);
        }
    }

    public void test_06_fetchEntryContainedInField() throws InterruptedException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.containedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (!Arrays.asList(containArray).contains(entry.getString("title"))) {
                    isContains = true;
                }
            }
            assertTrue(!isContains);
        }
    }

    public void test_07_fetchEntryNotEqualToField() throws InterruptedException, ParseException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.notEqualTo("title", "yellow t shirt");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            if(entry.getTitle().trim().equals("yellow t shirt")){
                count++;
            }
        }
        assertTrue(count == 0);
    }

    public void test_08_fetchEntryGreaterThanEqualToField() throws InterruptedException, ParseException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.greaterThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            if((Integer)entry.get("price")>=90) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_09_fetchEntryGreaterThanField() throws InterruptedException, ParseException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.greaterThan("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            if((Integer)entry.get("price")>90) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_10_fetchEntryLessThanEqualField() throws InterruptedException, ParseException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.lessThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            if((Integer)entry.get("price")<=90) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_11_fetchEntryLessThanField() throws InterruptedException, ParseException {

        Query query = stack.contentType("product").query();
        final Object result[] = new Object[]{new Object()};
        query.lessThan("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            System.out.println(entry.get("price"));
            if((Integer)entry.get("price")<90) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_12_fetchEntriesWithOr() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        final Object result[] = new Object[]{new Object()};

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[]{20, 45});

        ArrayList<Query> array = new ArrayList<Query>();
        array.add(query);
        array.add(subQuery);

        orQuery.or(array);

        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            int discount = (Integer)entry.get("discount");
            if((Integer)entry.get("price")<90 || (discount==20 || discount == 45)) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_13_fetchEntriesWithAnd() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        final Object result[] = new Object[]{new Object()};

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[]{20, 45});

        ArrayList<Query> array = new ArrayList<Query>();
        array.add(query);
        array.add(subQuery);

        orQuery.and(array);

        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        Integer count = 0;
        for(Entry entry: entries){
            int discount = (Integer)entry.get("discount");
            if((Integer)entry.get("price")<90 || (discount==20 || discount == 45)) {
                count++;
            }
        }
        assertTrue(count.equals(entries.size()));
    }

    public void test_14_addQuery() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();

        final Object result[] = new Object[]{new Object()};

        query.addQuery("limit", "8");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];

        assertTrue(entries.size() == 8);
    }

    public void test_15_removeQueryFromQuery() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();

        final Object result[] = new Object[]{new Object()};

        query.addQuery("limit", "8");
        query.removeQuery("limit");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        assertTrue(entries.size() == 27);
    }

    public void test_18_includeSchema() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.includeSchema();
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getSchema();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        JSONArray schema = null;
        schema = (JSONArray) result[0];
        assertTrue(schema !=  null);
    }

    public void test_19_search() throws InterruptedException, ParseException {
        String head = "laptop";

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.search("dress");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        if(entries != null){
            String resultHead = null;
            for (int i = 0; i < entries.size(); i++) {
                JSONObject jsonObject = entries.get(i).toJSON();
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                       Object value = jsonObject.opt(key);
                       if(value instanceof String && ((String) value).contains("dress")){

                       }
                    } catch (Exception e) {
                        CSAppUtils.showLog(TAG, "----------------setQueryJson"+e.toString());
                    }
                }

            }
            if(resultHead != null) {
                assertTrue(resultHead.equalsIgnoreCase(head));
            }
        }
    }

    public void test_20_ascending() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.ascending("title");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    for (Entry entry: (List<Entry>)result[0]) {
                        Log.w(TAG, "----------Test--Query-20--Success---------" + entry.getString("title"));
                    }

                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        assertTrue(entries.size() == 27);
    }

    public void test_21_descending() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.descending("title");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    for (Entry entry: (List<Entry>)result[0]) {
                        Log.w(TAG, "----------Test--Query-21--Success---------" + entry.getString("title"));
                    }

                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        assertTrue(entries.size() == 27);
    }

    public void test_22_limit() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.limit(3);
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    for (Entry entry: (List<Entry>)result[0]) {
                        System.out.println(" entry = [" + entry.getString("title") + "]");
                    }

                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        assertTrue(entries.size() == 3);
    }

    public void test_23_skip() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.skip(3);
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        List<Entry> entries = null;
        entries = (List<Entry>) result[0];
        assertTrue(entries.size() == 24);
    }

    public void test_24_only() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.only(new String[]{"price"});
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (entry.toJSON().has("title")) {
                    isContains = true;
                }
            }
            assertFalse(isContains);
        }
    }

    public void test_25_except() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.except(new String[]{"price"});
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (entry.toJSON().has("price")) {
                    isContains = true;
                }
            }
            assertTrue(!isContains);
        }
    }

    public void test_26_count() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.count();
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getCount();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        assertEquals(27, (int)result[0]);
    }

    public void test_27_regex() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.regex("title", "lap*", "i");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (entry.getString("title").equals("laptop")) {
                    isContains = true;
                }
            }
            assertTrue(isContains);
        }
    }

    public void test_28_exist() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.exists("title");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (entry.toJSON().has("title")) {
                    isContains = true;
                }
            }
            assertTrue(isContains);
        }
    }

    public void test_29_notExist() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.notExists("price1");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getCount();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if(entries != null) {
            boolean isContains = false;
            for (Entry entry : entries) {
                if (entry.toJSON().has("price1")) {
                    isContains = true;
                }
            }
            assertTrue(!isContains);
        }
    }

    /*public void test_30_afterUid() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.afterUid("blt127655bd036aad42");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        assertEquals(1, ((List<Entry>)result[0]).size());
    }

    public void test_31_beforeUid() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.beforeUid("blt127655bd036aad42");
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        assertEquals(20, ((List<Entry>)result[0]).size());
    }*/

    public void test_32_tags() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.tags(new String[]{"pink"});
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        assertEquals(1, ((List<Entry>)result[0]).size());
    }

    public void test_33_language() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.language(Language.ENGLISH_UNITED_KINGDOM);
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        assertEquals(4, ((List<Entry>)result[0]).size());
    }

    public void test_34_includeCount() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.includeCount();
        query.where("uid","blt3976eac6d3a0cb74");
        final Object result[] = new Object[2];

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    result[1] = queryresult.getCount();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        if((List<Entry>)result[0] != null) {
            assertEquals(1, (int)result[1]);
        }
    }

    public void test_35_includeReferenceOnly_fetch() throws InterruptedException {
        final Object result[] = new Object[]{new Object()};
        final Query query = stack.contentType("multifield").query();
        query.where("uid", "blt1b1cb4f26c4b682e");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("title");
        strings1.add("brief_description");
        strings1.add("discount");
        strings1.add("price");
        strings1.add("in_stock");

        query.onlyWithReferenceUid(strings, "package_info.info_category");
        query.exceptWithReferenceUid(strings1, "product_ref");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        try {
            for (Entry entry : entries) {
                JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");

                if(categoryArray != null && categoryArray.length() > 0) {

                    JSONObject jsonObject = categoryArray.getJSONObject(0);
                    boolean isContains = false;
                    if (jsonObject.has("title")) {
                        isContains = true;
                        assertTrue(isContains);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_36_includeReferenceExcept_fetch() throws InterruptedException {
        final Object result[] = new Object[]{new Object()};
        final Query query = stack.contentType("product").query().where("uid", "blt7801c5d40cbbe979");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");

        query.exceptWithReferenceUid(strings, "category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    entries = (ArrayList<Entry>) queryresult.getResultObjects();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        try {
            for (Entry entry : entries) {
                JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
                JSONObject jsonObject = categoryArray.getJSONObject(0);
                boolean isContains = false;
                if (jsonObject.has("title")) {
                    isContains = true;
                }
                assertFalse(isContains);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void test_37_findOne() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        //query.includeCount();
        query.where("in_stock", true);
        query.where("title", "halloween dress");
        final Object result[] = new Object[2];

        query.findOne(new SingleQueryResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Entry entry, Error error) {

                if (error == null) {
                    result[0] = entry;
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        Entry entry = (Entry) result[0];
        System.out.println("entry==?"+ entry);
        assertEquals(entry.getUid(), (String)"bltbb44e49e6148eb30"/*"blte88d9bec040e7c7c"*/);
    }



    //Char count 8209 will passed...
    public void test_38_complexFind() throws InterruptedException, ParseException {

        ContentType contentType = stack.contentType("product");

        Query query = contentType.query();
        final Object result[] = new Object[] { new Object() };

        query.notEqualTo("title", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*******");

        query.includeSchema();
        query.includeCount();

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    System.out.println("responseType = [" + responseType + "], queryresult = [" + queryresult.getResultObjects().size() + "]");
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();

    }





    public void test_39_includeContentType() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.includeContentType();
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getContentType();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        JSONObject contentType = null;
        contentType = (JSONObject) result[0];
        assertTrue(contentType !=  null);
    }





    public void test_40_includeSchemaAndContentType() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.includeSchema();
        query.includeContentType();
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getContentType();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        JSONObject contentType = null;
        contentType = (JSONObject) result[0];
        assertTrue(contentType !=  null);
    }




    public void test_40_includeContentTypeAndSchema() throws InterruptedException, ParseException {

        ContentType ct = stack.contentType("product");
        Query query = ct.query();
        query.includeContentType();
        query.includeSchema();
        final Object result[] = new Object[]{new Object()};

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getContentType();
                    latch.countDown();
                } else {
                    result[0] = error.getErrorCode();
                    latch.countDown();
                }
            }
        });
        latch.await();
        JSONObject contentType = null;
        contentType = (JSONObject) result[0];
        assertTrue(contentType !=  null);
    }

    /************************************************************/


    public void test_39_addParams() throws InterruptedException, ParseException {

        ContentType contentType = stack.contentType("product");
        Query query = contentType.query();
        final Object result[] = new Object[] { new Object() };
        query.addParam("key","sample_value");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {

                if (error == null) {
                    result[0] = queryresult.getResultObjects();
                    System.out.println("responseType = [" + responseType + "], queryresult = [" + queryresult.getResultObjects().size() + "]");
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();

    }

}
