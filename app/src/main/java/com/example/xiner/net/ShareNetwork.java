package com.example.xiner.net;

import android.content.Context;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.adapter.ShareAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
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



    public DetailItem ParseNet(JSONObject jsonObject){
        Log.v(TAG,jsonObject+"object");
        DetailItem detailItem = new DetailItem();
        for (Iterator iterator = jsonObject.keys();iterator.hasNext();){
            String key = (String) iterator.next();
            if (key.equals("userFigure")){
                try {
                    String userFigure = jsonObject.getString("userFigure");
                    detailItem.setUserFigure(userFigure);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (key.equals("files")){
                List<FileItem> fileItems = new ArrayList<>();
                try {
                   JSONArray array = jsonObject.getJSONArray("files");
                    if (array!=null){
                        for (int i =0;i< array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            FileItem fileItem = new FileItem();
                            for (Iterator iter = object.keys();iter.hasNext();){
                                if (iter.equals("filename")){
                                    String filename = object.getString("filename");
                                    fileItem.setFilename(filename);
                                }else if (iter.equals("url")){
                                    String url = object.getString("url");
                                    fileItem.setUrl(url);
                                }else if (iter.equals("type")){
                                    String type = object.getString("type");
                                    fileItem.setType(type);
                                }

                            }
                            fileItems.add(fileItem);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detailItem.setFiles(fileItems);
            }else if (key.equals("comments")){
                List<Comment>commentList = new ArrayList<>();
                try {
                    JSONArray commentArray = jsonObject.getJSONArray("comments");
                    if (commentArray!=null){
                        for (int a=0;a<commentArray.length();a++){
                            Comment comment = new Comment();
                            JSONObject commentObject = commentArray.getJSONObject(a);
                            for (Iterator commentiter = commentObject.keys();commentiter.hasNext();){
                                if (commentiter.equals("username")){
                                    String username = commentObject.getString("username");
                                    comment.setUsername(username);
                                }else if (commentiter.equals("userFigure")){
                                    String userFigure = commentObject.getString("userFigure");
                                    comment.setUserFigure(userFigure);
                                }else if (commentiter.equals("createdTime")){
                                    String createTime = commentObject.getString("createdTime");
                                    comment.setCreatedTime(new Date(Long.parseLong(createTime)));
                                }else if (commentiter.equals("content")){
                                    String content = commentObject.getString("content");
                                    comment.setContent(content);

                                }else if (commentiter.equals("files")){
                                    List<FileItem> commentfileItems = new ArrayList<>();
                                    try {
                                        JSONArray array = jsonObject.getJSONArray("files");
                                        if (array!=null){
                                            for (int i =0;i< array.length();i++){
                                                JSONObject object = array.getJSONObject(i);
                                                FileItem fileItem = new FileItem();
                                                for (Iterator iter = object.keys();iter.hasNext();){
                                                    if (iter.equals("filename")){
                                                        String filename = object.getString("filename");
                                                        fileItem.setFilename(filename);
                                                    }else if (iter.equals("url")){
                                                        String url = object.getString("url");
                                                        fileItem.setUrl(url);
                                                    }else if (iter.equals("type")){
                                                        String type = object.getString("type");
                                                        fileItem.setType(type);
                                                    }

                                                }
                                                commentfileItems.add(fileItem);
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    comment.setFiles(commentfileItems);
                                }
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
