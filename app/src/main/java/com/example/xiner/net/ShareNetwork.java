package com.example.xiner.net;

import android.content.Context;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.adapter.ShareAdapter;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.Item;
import com.example.xiner.entity.User;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by xiner on 15-3-8.
 */
public class ShareNetwork  {

    public static final String getshareList = HttpUtil.baseUrl;
    public static final String loadmoreurl=HttpUtil.baseUrl+"/loadmore";

    private static String TAG = "ShareNetwork";
    Context context;
    ShareAdapter adapter;


    ArrayList<Item> adpterlist;
    SwipeRefreshLayout layout;
    public ShareNetwork(){

    }

    public ShareNetwork(Context context,ShareAdapter shareAdapter,ArrayList<Item>list,SwipeRefreshLayout layout){
        this.context = context;
        this.adapter = shareAdapter;
         this.adpterlist = list;
        this.layout = layout;
    }

    public  void getSharelist() {

        HttpUtil.get(getshareList, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                adpterlist.clear();
                adpterlist.addAll(ParseNet(response));
                adapter.notifyDataSetChanged();
                layout.setRefreshing(false);
//                downloadFiles();
            }




            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v(TAG, statusCode + "codefailer");
                throwable.printStackTrace(System.out);
            }
        });

    }
    ArrayList<Item>listitem = new ArrayList<>();
    public  List<Item> ClickLoadMore(){
        RequestParams params = new RequestParams();
        params.put("number",1);



        HttpUtil.post(loadmoreurl,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                listitem=ParseNet(response);
//                adpterlist.addAll(ParseNet(response));
//                adapter.notifyDataSetChanged();
//                layout.setRefreshing(false);
                Log.v(TAG,listitem.size()+"lengthnei");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace(System.out);
                Log.v(TAG,"failer");
            }
        });
        Log.v(TAG,listitem.size()+"length");

        return listitem;
    }


    public ArrayList<Item> ParseNet(JSONArray jsonArray){
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Item shareItem = new Item();
            try {
                JSONObject item = jsonArray.getJSONObject(i);
                for (Iterator iter = item.keys(); iter.hasNext(); ) {

                    String key = (String) iter.next();
                    System.out.println(key + "key" + item.getString(key));

                    if (key.equals("content")) {
                        String content = item.getString("content");
                        shareItem.setContent(content);

                    } else if (key.equals("createdTime")) {
                        String time = item.getString("createdTime");

                        shareItem.setCreatedTime(new Date(Long.parseLong(time)));
                    } else if (key.equals("starNumber")) {
                        Long starNumber = item.getLong("starNumber");
                        shareItem.setStarNumber(starNumber);

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
                        shareItem.setOwner(user);
                    } else if (key.equals("fileItems")) {
                        Set<FileItem> fileItems = new HashSet<FileItem>();

                        JSONArray fileArray = item.getJSONArray("fileItems");
                        if (fileArray!=null) {
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
                                        Log.v(TAG, fileItem.getUrl() + "url");
                                    } else if (iterator.equals("type")) {
                                        String type = fileObject.getString("type");
                                        fileItem.setType(type);
                                    }

                                    fileItems.add(fileItem);
                                }
                            }
                        }else{

                        }
                        shareItem.setFileItems(fileItems);
                    } else if (key.equals("praiseNumber")) {

                        shareItem.setPraiseNumber(item.getLong("praiseNumber"));
                    } else if (key.equals("subject")) {
                        shareItem.setSubject(item.getString("subject"));
                    }else if(key.equals("starNumber")){
                        shareItem.setStarNumber(item.getLong("starNumber"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(shareItem);
        }
        return items;
    }
}
