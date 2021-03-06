package com.example.xiner.net;

import android.content.Context;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.adapter.ShareAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileFigure;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
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
public class ShareNetwork {

    public static final String getshareList = HttpUtil.baseUrl;
    public static final String loadmoreurl = HttpUtil.baseUrl + "/loadmore";
    public static final String getitemdetail = HttpUtil.baseUrl;

    private static String TAG = "ShareNetwork";
    Context context;
    ShareAdapter adapter;


    ArrayList<ListItem> adpterlist;
    SwipeRefreshLayout layout;

    public ShareNetwork() {

    }

    public ShareNetwork(Context context, ShareAdapter shareAdapter, ArrayList<ListItem> list, SwipeRefreshLayout layout) {
        this.context = context;
        this.adapter = shareAdapter;
        this.adpterlist = list;
        this.layout = layout;
    }
    public ShareNetwork(Context context, ShareAdapter shareAdapter, ArrayList<ListItem> list) {
        this.context = context;
        this.adapter = shareAdapter;
        this.adpterlist = list;

    }


    public DetailItem ParseNet(JSONObject jsonObject) {
        Log.v(TAG, jsonObject + "object");

        DetailItem detailItem = new DetailItem();
        for (Iterator iterator = jsonObject.keys(); iterator.hasNext();) {
            String key = (String) iterator.next();
            if (key.equals("userFigure")) {

                try {
                    String userFigure = jsonObject.getString("userFigure");
                    if (userFigure != null) {
                        detailItem.setUserFigure(userFigure);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (key.equals("files")) {

                List<FileItem> fileItems = new ArrayList<>();
                try {
                    JSONArray array = jsonObject.getJSONArray("files");
                    if (array.length()!=0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            FileItem fileItem = new FileItem();
                            for (Iterator iter = object.keys(); iter.hasNext(); ) {

                                String keyyy = (String) iter.next();
                                if (keyyy.equals("filename")) {

                                    String filename = object.getString("filename");
                                    fileItem.setFilename(filename);
                                } else if (keyyy.equals("url")) {

                                    String url = object.getString("url");
                                    fileItem.setUrl(url);
                                } else if (keyyy.equals("type")) {

                                    String type = object.getString("type");
                                    fileItem.setType(type);
                                    Log.v(TAG,fileItem.getType());
                                }

                            }
                            fileItems.add(fileItem);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detailItem.setFiles(fileItems);
            }
            else if (key.equals("comments")){
                List<Comment>commentList = new ArrayList<>();
                try {
                    JSONArray commentArray = jsonObject.getJSONArray("comments");
                    if (commentArray.length()!=0){
                        for (int a=0;a<commentArray.length();a++){
                            Log.v(TAG, "haha2");
                            Comment comment = new Comment();
                            JSONObject commentObject = commentArray.getJSONObject(a);
                            for (Iterator commentiter = commentObject.keys();commentiter.hasNext();){
                                String keycomment = (String) commentiter.next();
                                if (keycomment.equals("username")){
                                    Log.v(TAG,"haha5");
                                    String username = commentObject.getString("username");
                                    comment.setUsername(username);
                                }else if (keycomment.equals("userFigure")){
                                    Log.v(TAG,"haha6");
                                    String userFigure = commentObject.getString("userFigure");
                                    comment.setUserFigure(userFigure);
                                }else if (keycomment.equals("createdTime")){
                                    Log.v(TAG,"haha7");
                                    String createTime = commentObject.getString("createdTime");
                                    comment.setCreatedTime(new Date(Long.parseLong(createTime)));
                                }else if (keycomment.equals("content")){
                                    Log.v(TAG,"haha8");
                                    String content = commentObject.getString("content");
                                    comment.setContent(content);

                                }
//                                else if (keycomment.equals("files")){
//                                    Log.v(TAG,"haha9");
//                                    List<FileItem> commentfileItems = new ArrayList<>();
//                                    try {
//                                        JSONArray array = jsonObject.getJSONArray("files");
//                                        if (array!=null){
//                                            for (int i =0;i< array.length();i++){
//                                                Log.v(TAG,"haha10");
//                                                JSONObject object = array.getJSONObject(i);
//                                                FileItem fileItem = new FileItem();
//                                                for (Iterator iter = object.keys();iter.hasNext();){
//                                                    if (iter.equals("filename")){
//                                                        Log.v(TAG,"haha11");
//                                                        String filename = object.getString("filename");
//                                                        fileItem.setFilename(filename);
//                                                    }else if (iter.equals("url")){
//                                                        Log.v(TAG,"haha12");
//                                                        String url = object.getString("url");
//                                                        fileItem.setUrl(url);
//                                                    }else if (iter.equals("type")){
//                                                        Log.v(TAG,"haha13");
//                                                        String type = object.getString("type");
//                                                        fileItem.setType(type);
//                                                    }
//
//                                                }
//                                                commentfileItems.add(fileItem);
//                                            }
//
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    comment.setFiles(commentfileItems);
//                                }
                            }
                            commentList.add(comment);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detailItem.setComments(commentList);
            }
        }

        return detailItem;
    }
    public User ParseUser(JSONObject jsonObject) {
        Log.v(TAG, jsonObject + "object");

        User user = new User();
        for (Iterator iterator = jsonObject.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            if (key.equals("nickname")) {
                Log.v(TAG,"userha");
                try {
                    String nickname = jsonObject.getString("nickname");
                    if (nickname != null) {
                        user.setNickname(nickname);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (key.equals("age")) {
                Log.v(TAG,"userha1");

                try {
                    int age = jsonObject.getInt("age");
//                    if (nickname != null) {
                    user.setAge(age);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (key.equals("username")) {
                Log.v(TAG,"userha2");

                try {
                    String username = jsonObject.getString("username");
                    if (username != null) {
                        user.setUsername(username);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (key.equals("figure")){
                try {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("figure");
                    FileFigure fileItem = new FileFigure();
                    for (Iterator iter = jsonObject1.keys(); iter.hasNext(); ) {

                        String keyyy = (String) iter.next();
                        if (keyyy.equals("filename")) {
                            Log.v(TAG,"userhaf");

                            String filename = jsonObject1.getString("filename");
                            if (filename!=null) {
                                fileItem.setFilename(filename);
                            }
                        } else if (keyyy.equals("url")) {
                            Log.v(TAG,"userhaf1");

                            String url = jsonObject1.getString("url");
                            if (url!=null) {
                                fileItem.setUrl(url);
                            }
                        } else if (keyyy.equals("type")) {

                            String type = jsonObject1.getString("type");
                            if (type!=null) {
                                fileItem.setType(type);
                            }
                            Log.v(TAG, fileItem.getType());
                        }

                    }
                    user.setFileFigure(fileItem);
//                            fileItems.add(fileItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//                List<FileItem> fileItems = new ArrayList<>();
//            try {
//                JSONArray array = jsonObject.getJSONArray("figure");
//                if (array.length() != 0) {
//
//                    for (int i = 0; i < array.length(); i++) {
//
//                        JSONObject object = array.getJSONObject(i);
//                        FileFigure fileItem = new FileFigure();
//                        for (Iterator iter = object.keys(); iter.hasNext(); ) {
//
//                            String keyyy = (String) iter.next();
//                            if (keyyy.equals("filename")) {
//                                Log.v(TAG,"userhaf");
//
//                                String filename = object.getString("filename");
//                                if (filename!=null) {
//                                    fileItem.setFilename(filename);
//                                }
//                            } else if (keyyy.equals("url")) {
//                                Log.v(TAG,"userhaf1");
//
//                                String url = object.getString("url");
//                                if (url!=null) {
//                                    fileItem.setUrl(url);
//                                }
//                            } else if (keyyy.equals("type")) {
//
//                                String type = object.getString("type");
//                                if (type!=null) {
//                                    fileItem.setType(type);
//                                }
//                                Log.v(TAG, fileItem.getType());
//                            }
//
//                        }
//                        user.setFileFigure(fileItem);
////                            fileItems.add(fileItem);
//                    }
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }
        return user;
    }

    public ArrayList<ListItem> ParseNet(JSONArray jsonArray) {
        ArrayList<ListItem> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            ListItem shareItem = new ListItem();
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
                    } else if (key.equals("stars")) {
                        Integer starNumber = item.getInt("stars");
                        shareItem.setStars(starNumber);

                    } else if (key.equals("userId")) {
                        String userid = item.getString("userId");
                        shareItem.setUserId(userid);
                    } else if (key.equals("id")) {
                        Long id = item.getLong("id");
                        shareItem.setId(id);
                    } else if (key.equals("subject")) {
                        String subject = item.getString("subject");
                        shareItem.setSubject(subject);
                    } else if (key.equals("nickname")) {
                        String nickname = item.getString("nickname");
                        shareItem.setNickname(nickname);
                    } else if (key.equals("ups")) {
                        Integer ups = item.getInt("ups");
                        shareItem.setUps(ups);
                    } else if (key.equals("comments")) {
                        Integer comments = item.getInt("comments");
                        shareItem.setComments(comments);
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
