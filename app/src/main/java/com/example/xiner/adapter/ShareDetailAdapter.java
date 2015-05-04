package com.example.xiner.adapter;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.activity.CommentActivity;
import com.example.xiner.activity.PictureDetailActivity;
import com.example.xiner.activity.ShareDetailActivity;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.view.CircularImage;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiner on 4/25/15.
 */
public class ShareDetailAdapter extends RecyclerView.Adapter<ShareDetailAdapter.ViewHoler> {

    private static final String TAG = "ShareDetailAdapter";

    Context context;
    List<Comment> commentList;
    DetailItem detailItem;
    Long id;
    int comments;
    int collectionNum;
    int praiseNum;
    ListItem listItem;
    AppBase app;
    String picpath = Environment.getExternalStorageDirectory() + "/xueyou/thumbnail/userFigure";
    String path = Environment.getExternalStorageDirectory() + "/xueyou/thumbnail/userFigure";


    public ShareDetailAdapter(Context context, List<Comment> commentList, DetailItem detailItem, ListItem listItem) {
        Log.v(TAG, commentList.size() + "sizesizesize");
        this.commentList = commentList;
        this.context = context;
        this.detailItem = detailItem;
        this.listItem = listItem;
        app = AppBase.getApp();
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewlast, viewcard, viewDetail;

        ViewHoler vh;
        if (viewType == 2) {

            viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last, parent, false);

            vh = new ViewHoler(viewlast);
        } else if (viewType == 0) {

            viewDetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_share_detail, parent, false);
            vh = new ViewHoler(viewDetail);
        } else {

            viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer, parent, false);
            vh = new ViewHoler(viewcard);


        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        clickListener clicklistenern = new clickListener(holder);
        if (position == 0) {
            Log.v(TAG, holder.nicknametextdetail + "nicknamenickname");
            holder.nicknametextdetail.setText(listItem.getNickname());
            holder.timetextdetail.setText(myFmt2.format(listItem.getCreatedTime()));
            holder.subjecttext.setText(listItem.getSubject());
            holder.detailtext.setText(listItem.getContent());
            collectionNum = listItem.getStars();
            comments = listItem.getComments();
            praiseNum = listItem.getUps();
            id = listItem.getId();
            holder.collectionText.setText("(" + collectionNum + ")");
            holder.commentText.setText("(" + comments + ")");
//            holder.praiseText.setText("(" + praiseNum + ")");
            holder.commentnum.setText(commentList.size() + "个评论");
            holder.comment.setOnClickListener(clicklistenern);
            holder.collection.setOnClickListener(clicklistenern);

            datainit(holder);

            String Figure = detailItem.getUserFigure();
            if (!Figure.equals("null")) {
                Log.v(TAG,detailItem.getUserFigure()+"detailitemuser");
                String nameFile = detailItem.getUserFigure().substring(detailItem.getUserFigure().lastIndexOf("/"));
                Log.v(TAG,nameFile+"namefilenamefile");

                File file = new File(picpath + nameFile);
                if (file.exists()) {
                    Log.v(TAG,"fileexistsfileexists");
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

                    BitmapFactory.decodeFile(file.getPath(), opts);

                    opts.inJustDecodeBounds = false;

                    opts.inSampleSize = app.calculateInSampleSize(opts, 50, 50);
                    Log.v(TAG, file.getPath() + "file.getPath");
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);

                    holder.head.setImageBitmap(bitmap);
                } else {
                    Log.v(TAG,"fileexistsfileexistsnot");

                    File file1 = new File(path);
                    file1.mkdirs();
                    //下载图片
                    DownloadPicUtil downloadPicUtil = new DownloadPicUtil(holder.head, new File(picpath + nameFile), 50, 50);
                    downloadPicUtil.execute(HttpUtil.baseIp + detailItem.getUserFigure());
                }
            }
        } else if (position == commentList.size() + 1) {

        } else {
            if (holder.timetext != null) {
                holder.timetext.setText(myFmt2.format(commentList.get(position - 1).getCreatedTime()));
            }
            if (holder.nicknametext != null) {
                holder.nicknametext.setText(commentList.get(position - 1).getUsername());
            }
            if (holder.commenttext != null) {
                holder.commenttext.setText(commentList.get(position - 1).getContent());
            }
            String userFigure = commentList.get(position-1).getUserFigure();

            if (!userFigure.equals("null")) {
                String nameFile = userFigure.substring(userFigure.lastIndexOf("/"));
                Log.v(TAG, picpath + nameFile + "picturepathnamefile");
                File file = new File(picpath + nameFile);
                if (file.exists()) {
                    Log.v(TAG,"commentfileexistsfileexists");

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

                    BitmapFactory.decodeFile(file.getPath(), opts);

                    opts.inJustDecodeBounds = false;

                    opts.inSampleSize = app.calculateInSampleSize(opts, 50, 50);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
                    if (holder.headComment!=null) {
                        Log.v(TAG,"picturepath1");
                        holder.headComment.setImageBitmap(bitmap);
                    }
                } else {
                    Log.v(TAG,"commentfileexistsfileexistsnot");

                    File file1 = new File(path);
                    file1.mkdirs();
                    Log.v(TAG, "picturepath2");

                    //下载图片
                    if (holder.headComment!=null) {
                        Log.v(TAG,"picturepath3");

                        DownloadPicUtil downloadPicUtil = new DownloadPicUtil(holder.headComment, new File(picpath + nameFile), 50, 50);
                        downloadPicUtil.execute(HttpUtil.baseIp + commentList.get(position - 1).getUserFigure());
                    }
                }
            }
        }
    }

    private void datainit(ViewHoler viewHoler) {
        List<FileItem> filesurl = detailItem.getFiles();
        final ArrayList<String> pictureurl = new ArrayList<>();
        final ArrayList<String> audiourl = new ArrayList<>();
        final ArrayList<String> other = new ArrayList<>();
        for (int i = 0; i < filesurl.size(); i++) {
            Log.v(TAG, filesurl.get(i).getType() + "typetype");
            if (filesurl.get(i).getType() != null) {
                if (filesurl.get(i).getType().equals("PICTURE")) {
                    pictureurl.add(filesurl.get(i).getUrl());
                } else if (filesurl.get(i).getType().equals("AUDIO")) {

                    audiourl.add(filesurl.get(i).getUrl());
                } else {

                    audiourl.add(filesurl.get(i).getUrl());
                }
            }
//          commentList =  item.getComments();
        }
        if (pictureurl != null) {
            PictureAdapter pictureAdapter = new PictureAdapter(context, pictureurl);
            viewHoler.gridView.setAdapter(pictureAdapter);
            viewHoler.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = pictureurl.get(position);
                    Intent intent = new Intent();
                    intent.setClass(context, PictureDetailActivity.class);
                    intent.putExtra("picturepath", path);
                    context.startActivity(intent);
                }
            });
        }
        if (audiourl.size() != 0) {
//            ListView list = (ListView) findViewById(R.id.filenameaudio_list);
            FileAdapter fileAdapter = new FileAdapter(context, audiourl);
            viewHoler.listView.setAdapter(fileAdapter);
        }

    }


    class clickListener implements View.OnClickListener {
        ViewHoler viewHoler;

        public clickListener(ViewHoler viewHoler) {
            this.viewHoler = viewHoler;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.idima_collection:
                    RequestParams params = new RequestParams();
                    params.put("username", AppBase.getApp().getDataStore().getString("username", "18366116061"));
                    Log.v(TAG, AppBase.getApp().getDataStore().getString("username", "18366116061") + "18366116016");
                    HttpUtil.post(HttpUtil.baseUrl + "/" + id + "/star", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String str = new String(responseBody);
                            if (str.equals("has been starred")) {
                                Toast.makeText(context, "已经收藏过", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                collectionNum = collectionNum + 1;
                                viewHoler.collectionText.setText("(" + collectionNum + ")");

                                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.v(TAG, error.toString() + "error" + statusCode);
                            Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();

                        }
                    });

                    break;
                case R.id.idima_comment:
                    Intent intent = new Intent();
                    intent.setClass(context, CommentActivity.class);
                    intent.putExtra("id", listItem.getId());
                    context.startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == commentList.size() + 1) {
            return 2;
        } else {
            return 1;
        }
    }

    static class ViewHoler extends RecyclerView.ViewHolder {
        TextView timetext, commenttext, nicknametext;
        ImageView head, headComment;
        GridView gridView;
        ListView listView;
        private TextView collectionText, praiseText, commentText, nicknametextdetail, timetextdetail, subjecttext, detailtext, commentnum;

        private ImageView zan, collection, comment;

        public ViewHoler(View itemView) {
            super(itemView);
            listView = (ListView) itemView.findViewById(R.id.filenameaudio_list);

            timetext = (TextView) itemView.findViewById(R.id.timedetail);
            commenttext = (TextView) itemView.findViewById(R.id.comment_text);
            nicknametext = (TextView) itemView.findViewById(R.id.nickname_detail);
            head = (ImageView) itemView.findViewById(R.id.face);
            headComment = (CircularImage) itemView.findViewById(R.id.face_comment);
            gridView = (GridView) itemView.findViewById(R.id.picturegridviewcomment);

            collectionText = (TextView) itemView.findViewById(R.id.collectionnum_text);
//                praiseText = (TextView) itemView.findViewById(R.id.praisenum_text);
            commentText = (TextView) itemView.findViewById(R.id.commentnum_text);
            nicknametextdetail = (TextView) itemView.findViewById(R.id.nickname_text);
            timetextdetail = (TextView) itemView.findViewById(R.id.time_text);
            subjecttext = (TextView) itemView.findViewById(R.id.subject_text);
            detailtext = (TextView) itemView.findViewById(R.id.detail_text);
//            zan = (ImageView) itemView.findViewById(R.id.idima_praise);
            collection = (ImageView) itemView.findViewById(R.id.idima_collection);
            comment = (ImageView) itemView.findViewById(R.id.idima_comment);
            commentnum = (TextView) itemView.findViewById(R.id.pinglun_numtext);
            gridView = (GridView) itemView.findViewById(R.id.picturegridview);

        }
    }
}
