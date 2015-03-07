package com.example.xiner.net;

import android.content.Context;
import android.util.Log;

import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.Item;
import com.example.xiner.entity.User;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xiner on 15-1-27.
 */
public class Network {

    Context context;

    public static final String TAG = "Network";
    public static final String uploadFile = "http://211.87.226.208:8080/api/v1/item/files";
    public static final String getshareList = "http://211.87.226.208:8080/api/v1/item";
    HttpUtil httpUtil;


    public static void uploadshare(ArrayList<String> file, String content, String grade, String subject) {

        RequestParams params = new RequestParams();
        params.put("content", content);
        params.put("grade", grade);
        params.put("subject", subject);


        for (int i = 0; i < file.size(); i++) {
            try {

                params.put("file" + i, new File(new URI(file.get(i))), "multipart/form-data");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


        HttpUtil.post(uploadFile, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("PublicDocA", statusCode + "codesuccess");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("PublicDocAf", statusCode + "codefailed");
                error.printStackTrace(System.out);
            }
        });
    }


    public static void getSharelist() {

        HttpUtil.get(getshareList, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        for (Iterator iter = item.keys(); iter.hasNext(); ) {
                            Item item1 = new Item();
                            String key = (String) iter.next();
                            System.out.println(item.getString(key));

                            if (key.equals("content")) {
                                String content = item.getString("content");
                                item1.setContent(content);

                            } else if (key.equals("createdTime")) {
                                String time = item.getString("createdTime");

                                item1.setCreatedTime(new Date(Long.parseLong(time)));
                            } else if (key.equals("starNumber")) {
                                Long starNumber = item.getLong("starNumber");
                                item1.setStarNumber(starNumber);

                            } else if (key.equals("owner")) {
                                JSONObject jsonObject = new JSONObject(item.getString("owner"));
                                User user = new User();
                                for (Iterator iterable = jsonObject.keys(); iterable.hasNext(); ) {
                                    String userkey = (String) iterable.next();
                                    if (userkey.equals("username")) {
                                        String username = jsonObject.getString("username");
                                        user.setUsername(username);
                                        Log.v(TAG, username + "username");
                                    } else if (userkey.equals("mail")) {
                                        String mail = jsonObject.getString("mail");
                                        user.setMail(mail);
                                    }

                                }
                            } else if (key.equals("fileItem")) {
                                Set<FileItem> fileItems = new HashSet<FileItem>();
                                JSONArray fileArray = item.getJSONArray("fileItem");
                                for (int k = 0; k < fileArray.length(); k++) {
                                    JSONObject fileObject = fileArray.getJSONObject(k);
                                    FileItem fileItem = new FileItem();
                                    for (Iterator iterator = fileObject.keys(); iterator.hasNext(); ) {
                                        if (iterator.equals("filename")) {
                                            String filename = fileObject.getString("filename");
                                            fileItem.setFilename(filename);
                                        } else if (iterator.equals("url")) {
                                            String url = fileObject.getString("url");
                                            fileItem.setUrl(url);
                                            Log.v(TAG,fileItem.getUrl()+"url");
                                        } else if (iterator.equals("type")) {
                                            String type = fileObject.getString("type");
                                            fileItem.setType(type);
                                        }
                                        fileItems.add(fileItem);
                                    }
                                }

                            }
//                            String content = item.getString("content");
//                            Log.v(TAG,content+"content");
//                            Date date = new Date(item.getString("createdTime"));
//                            Long starNumber = item.getLong("starNumber");
                            System.out.println(item.getString(key));
                        }


//
//                            String content = item.getString("content");
//                        Date date = new Date(item.getString("createdTime"));
//                        Long starNumber = item.getLong("starNumber");

//                        Log.v(TAG,content+"content");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.v(TAG, statusCode + "codesuccess");

//                Log.v(TAG, response.toString());
            }

            private Item parseItem(JSONObject itemObject) {

                return null;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v(TAG, statusCode + "codefailer");
                throwable.printStackTrace(System.out);
            }
        });

    }


}
