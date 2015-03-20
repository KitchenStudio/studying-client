/*
    Open Manager, an open source file manager for the Android system
    Copyright (C) 2009, 2010, 2011  Joe Berria <nexesdevelopment@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.example.xiner.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.handler.EventHandler;
import com.example.xiner.handler.FileManager;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * <br>
 * <p/>
 * This class handles creating the buttons and
 * text views. This class relies on the class EventHandler to handle all button
 * press logic and to control the data displayed on its ListView. This class
 * also relies on the FileManager class to handle all file operations such as
 * copy/paste zip/unzip etc. However most interaction with the FileManager class
 * is done via the EventHandler class. Also the SettingsMangager class to load
 * and save user settings.
 * <br>
 * <p/>
 * The design objective with this class is to control only the look of the
 * GUI (option menu, context menu, ListView, buttons and so on) and rely on other
 * supporting classes to do the heavy lifting.
 *
 * @author Joe Berria
 */
public final class FileManagerMain extends ActionBarActivity {
    public static final String ACTION_WIDGET = "com.nexes.manager.Main.ACTION_WIDGET";

    private static final String PREFS_NAME = "ManagerPrefsFile";    //user preference file name
    private static final String PREFS_HIDDEN = "hidden";
    private static final String PREFS_COLOR = "color";
    private static final String PREFS_THUMBNAIL = "thumbnail";
    private static final String PREFS_SORT = "sort";
    private static final String PREFS_STORAGE = "sdcard space";

    private static final int SEARCH_B = 0x09;
    private static final int SETTING_REQ = 0x10;            //request code for intent
    private static final String TAG = "FileManagerMain";

    private FileManager mFileMag;
    private EventHandler mHandler;
    private EventHandler.TableRow mTable;

    private SharedPreferences mSettings;
    private boolean mReturnIntent = false;
    private boolean mUseBackKey = true;
    private TextView mPathLabel, mDetailLabel, textView_multiselect;
    ImageView internalImage,externalImage;
    boolean multi = false;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filemanagermain);
        ListView listView = (ListView) findViewById(android.R.id.list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.filemanager_customview);
        
        /*read settings*/
        mSettings = getSharedPreferences(PREFS_NAME, 0);
        boolean hide = mSettings.getBoolean(PREFS_HIDDEN, false);
        boolean thumb = mSettings.getBoolean(PREFS_THUMBNAIL, true);
        int space = mSettings.getInt(PREFS_STORAGE, View.VISIBLE);
        int color = mSettings.getInt(PREFS_COLOR, -1);
        int sort = mSettings.getInt(PREFS_SORT, 3);

        mFileMag = new FileManager();
        mFileMag.setShowHiddenFiles(hide);
        mFileMag.setSortType(sort);

        if (savedInstanceState != null)
            mHandler = new EventHandler(FileManagerMain.this, mFileMag, savedInstanceState.getString("location"));
        else
            mHandler = new EventHandler(FileManagerMain.this, mFileMag);

        mHandler.setTextColor(color);
        mHandler.setShowThumbnails(thumb);
        mTable = mHandler.new TableRow(multi);
        
        /*sets the ListAdapter for our ListActivity and
         *gives our EventHandler class the same adapter
         */
        mHandler.setListAdapter(mTable);
        listView.setAdapter(mTable);
        listView.setOnItemClickListener(new listClickListener());
        listView.setOnItemLongClickListener(new listLongClickListener());

        mDetailLabel = (TextView) findViewById(R.id.detail_label);
        mPathLabel = (TextView) findViewById(R.id.path_label);
        mPathLabel.setText("path: /sdcard");

        mHandler.setUpdateLabels(mPathLabel, mDetailLabel);
        textView_multiselect = (TextView) findViewById(R.id.text_selectmulti);
        textView_multiselect.setOnClickListener(new textSelectMultiListener());

        internalImage =(ImageView)findViewById(R.id.home_button);
        internalImage.setOnClickListener(new internalListerner());
        externalImage=(ImageView)findViewById(R.id.back_button);

        externalImage.setOnClickListener(new externalListerner());
        Intent intent = getIntent();

        if (intent.getAction() != null) {

            if (intent.getAction().equals(Intent.ACTION_GET_CONTENT)) {
//                bimg[5].setVisibility(View.GONE);
                mReturnIntent = true;

            } else if (intent.getAction().equals(ACTION_WIDGET)) {
                Log.e("MAIN", "Widget action, string = " + intent.getExtras().getString("folder"));
                mHandler.updateDirectory(mFileMag.getNextDir(intent.getExtras().getString("folder"), true));

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(android.R.id.home == item.getItemId()){
           finish();

           return false;
       }


           return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("location", mFileMag.getCurrentDir());
    }

    /*(non Java-Doc)
     * Returns the file that was selected to the intent that
     * called this activity. usually from the caller is another application.
     */
    private void returnIntentResults(File data) {
        mReturnIntent = false;

        Intent ret = new Intent();
        ret.setData(Uri.fromFile(data));
        setResult(RESULT_OK, ret);

        finish();
    }



    class externalListerner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(mHandler.isMulti_select_flag()) {
                mHandler.getmDelegate().killMultiSelect(true);
                Toast.makeText(FileManagerMain.this, "Multi-select is now off",
                        Toast.LENGTH_SHORT).show();
            }
            mHandler.stopThumbnailThread();
            String external = Environment.getExternalStorageDirectory().getPath();
            Log.v("EventHandler",external+"externer");
            mHandler.updateDirectory(mHandler.mFileMang.setHomeDir(external));
            if(mPathLabel != null)
                mPathLabel.setText(mHandler.mFileMang.getCurrentDir());
        }
    }

    class internalListerner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mHandler.mFileMang.getCurrentDir() != "/") {
                if(mHandler.isMulti_select_flag()) {
                    mHandler.getmDelegate().killMultiSelect(true);
                    Toast.makeText(FileManagerMain.this, "Multi-select is now off",
                            Toast.LENGTH_SHORT).show();
                }

                mHandler.stopThumbnailThread();
                mHandler.updateDirectory(mHandler.mFileMang.getPreviousDir());
                if(mPathLabel != null)
                    mPathLabel.setText(mHandler.mFileMang.getCurrentDir());
            }
        }
    }

    class textSelectMultiListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (textView_multiselect.getText().equals("选择文件")) {
                textView_multiselect.setText("完成");

                if (mHandler.isMulti_select_flag()) {
                    mHandler.getmDelegate().killMultiSelect(true);

                } else {
                    mHandler.setMulti_select_flag(true);
                }
            }else{
                textView_multiselect.setText("选择文件");
                if(mHandler.getmMultiSelectData() == null || mHandler.getmMultiSelectData().isEmpty()) {
                    mHandler.getmDelegate().killMultiSelect(true);
                   return;
                }
                ArrayList<String> uris = new ArrayList<String>();
                int length = mHandler.getmMultiSelectData().size();
                Intent mail_int = new Intent(FileManagerMain.this,PublicDocActivity.class);
                for(int i = 0; i < length; i++) {
                    File file = new File(mHandler.getmMultiSelectData().get(i));
                    Log.v(TAG,Uri.fromFile(file).toString());
                    uris.add(Uri.fromFile(file).toString());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urilist",uris);
                mail_int.putExtras(bundle);
               setResult(RESULT_OK,mail_int);
                mHandler.getmDelegate().killMultiSelect(true);
                finish();//如果是回传值的话，这个一定要有
            }
        }


    }

    class listLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            multi = true;
            mTable.notifyDataSetChanged();
            Toast.makeText(FileManagerMain.this, "box", Toast.LENGTH_SHORT).show();

            return true;
        }
    }


    class listClickListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            final String item = mHandler.getData(position);
            boolean multiSelect = mHandler.isMultiSelected();
            File file = new File(mFileMag.getCurrentDir() + "/" + item);
            String item_ext = null;

            try {
                item_ext = item.substring(item.lastIndexOf("."), item.length());

            } catch (IndexOutOfBoundsException e) {
                item_ext = "";
            }

    	/*
         * If the user has multi-select on, we just need to record the file
    	 * not make an intent for it.
    	 */
            if (multiSelect) {
                mTable.addMultiPosition(position, file.getPath());

            } else {
                if (file.isDirectory()) {
                    if (file.canRead()) {
                        mHandler.stopThumbnailThread();
                        mHandler.updateDirectory(mFileMag.getNextDir(item, false));
                        mPathLabel.setText(mFileMag.getCurrentDir());

		    		/*set back button switch to true 
                     * (this will be better implemented later)
		    		 */
                        if (!mUseBackKey)
                            mUseBackKey = true;

                    } else {
                        Toast.makeText(FileManagerMain.this, "Can't read folder due to permissions",
                                Toast.LENGTH_SHORT).show();
                    }
                }
	    	
	    	/*music file selected--add more audio formats*/
                else if (item_ext.equalsIgnoreCase(".mp3") ||
                        item_ext.equalsIgnoreCase(".m4a") ||
                        item_ext.equalsIgnoreCase(".mp4")) {

                    if (mReturnIntent) {
                        returnIntentResults(file);
                    } else {
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(file), "audio/*");
                        startActivity(i);
                    }
                }

	    	/*photo file selected*/
                else if (item_ext.equalsIgnoreCase(".jpeg") ||
                        item_ext.equalsIgnoreCase(".jpg") ||
                        item_ext.equalsIgnoreCase(".png") ||
                        item_ext.equalsIgnoreCase(".gif") ||
                        item_ext.equalsIgnoreCase(".tiff")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent picIntent = new Intent();
                            picIntent.setAction(Intent.ACTION_VIEW);
                            picIntent.setDataAndType(Uri.fromFile(file), "image/*");
                            startActivity(picIntent);
                        }
                    }
                }

	    	/*video file selected--add more video formats*/
                else if (item_ext.equalsIgnoreCase(".m4v") ||
                        item_ext.equalsIgnoreCase(".3gp") ||
                        item_ext.equalsIgnoreCase(".wmv") ||
                        item_ext.equalsIgnoreCase(".mp4") ||
                        item_ext.equalsIgnoreCase(".ogg") ||
                        item_ext.equalsIgnoreCase(".wav")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent movieIntent = new Intent();
                            movieIntent.setAction(Intent.ACTION_VIEW);
                            movieIntent.setDataAndType(Uri.fromFile(file), "video/*");
                            startActivity(movieIntent);
                        }
                    }
                }

	    	/* gzip files, this will be implemented later */
                else if (item_ext.equalsIgnoreCase(".gzip") ||
                        item_ext.equalsIgnoreCase(".gz")) {

                    if (mReturnIntent) {
                        returnIntentResults(file);

                    } else {
                        //TODO:
                    }
                }

	    	/*pdf file selected*/
                else if (item_ext.equalsIgnoreCase(".pdf")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent pdfIntent = new Intent();
                            pdfIntent.setAction(Intent.ACTION_VIEW);
                            pdfIntent.setDataAndType(Uri.fromFile(file),
                                    "application/pdf");

                            try {
                                startActivity(pdfIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(FileManagerMain.this, "Sorry, couldn't find a pdf viewer",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

	    	/*Android application file*/
                else if (item_ext.equalsIgnoreCase(".apk")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent apkIntent = new Intent();
                            apkIntent.setAction(Intent.ACTION_VIEW);
                            apkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            startActivity(apkIntent);
                        }
                    }
                }

	    	/* HTML file */
                else if (item_ext.equalsIgnoreCase(".html")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent htmlIntent = new Intent();
                            htmlIntent.setAction(Intent.ACTION_VIEW);
                            htmlIntent.setDataAndType(Uri.fromFile(file), "text/html");

                            try {
                                startActivity(htmlIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(FileManagerMain.this, "Sorry, couldn't find a HTML viewer",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

	    	/* text file*/
                else if (item_ext.equalsIgnoreCase(".txt")) {

                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent txtIntent = new Intent();
                            txtIntent.setAction(Intent.ACTION_VIEW);
                            txtIntent.setDataAndType(Uri.fromFile(file), "text/plain");

                            try {
                                startActivity(txtIntent);
                            } catch (ActivityNotFoundException e) {
                                txtIntent.setType("text/*");
                                startActivity(txtIntent);
                            }
                        }
                    }
                }

	    	/* generic intent */
                else {
                    if (file.exists()) {
                        if (mReturnIntent) {
                            returnIntentResults(file);

                        } else {
                            Intent generic = new Intent();
                            generic.setAction(Intent.ACTION_VIEW);
                            generic.setDataAndType(Uri.fromFile(file), "text/plain");

                            try {
                                startActivity(generic);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(FileManagerMain.this, "Sorry, couldn't find anything " +
                                                "to open " + file.getName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences.Editor editor = mSettings.edit();
        boolean check;
        boolean thumbnail;
        int color, sort, space;
    	
    	/* resultCode must equal RESULT_CANCELED because the only way
    	 * out of that activity is pressing the back button on the phone
    	 * this publishes a canceled result code not an ok result code
    	 */
        if (requestCode == SETTING_REQ && resultCode == RESULT_CANCELED) {
            //save the information we get from settings activity
            check = data.getBooleanExtra("HIDDEN", false);
            thumbnail = data.getBooleanExtra("THUMBNAIL", true);
            color = data.getIntExtra("COLOR", -1);
            sort = data.getIntExtra("SORT", 0);
            space = data.getIntExtra("SPACE", View.VISIBLE);

            editor.putBoolean(PREFS_HIDDEN, check);
            editor.putBoolean(PREFS_THUMBNAIL, thumbnail);
            editor.putInt(PREFS_COLOR, color);
            editor.putInt(PREFS_SORT, sort);
            editor.putInt(PREFS_STORAGE, space);
            editor.commit();

            mFileMag.setShowHiddenFiles(check);
            mFileMag.setSortType(sort);
            mHandler.setTextColor(color);
            mHandler.setShowThumbnails(thumbnail);
            // mStorageLabel.setVisibility(space);
            mHandler.updateDirectory(mFileMag.getNextDir(mFileMag.getCurrentDir(), true));
        }
    }


    /*
     * (non-Javadoc)
     * This will check if the user is at root directory. If so, if they press back
     * again, it will close the application. 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        String current = mFileMag.getCurrentDir();

        if (keycode == KeyEvent.KEYCODE_SEARCH) {
            showDialog(SEARCH_B);

            return true;

        } else if (keycode == KeyEvent.KEYCODE_BACK && mUseBackKey && !current.equals("/")) {
            if (mHandler.isMultiSelected()) {
                mTable.killMultiSelect(true);
                Toast.makeText(FileManagerMain.this, "Multi-select is now off", Toast.LENGTH_SHORT).show();

            } else {
                //stop updating thumbnail icons if its running
                mHandler.stopThumbnailThread();
                mHandler.updateDirectory(mFileMag.getPreviousDir());
                mPathLabel.setText(mFileMag.getCurrentDir());
            }
            return true;

        } else if (keycode == KeyEvent.KEYCODE_BACK && mUseBackKey && current.equals("/")) {
            Toast.makeText(FileManagerMain.this, "Press back again to quit.", Toast.LENGTH_SHORT).show();

            if (mHandler.isMultiSelected()) {
                mTable.killMultiSelect(true);
                Toast.makeText(FileManagerMain.this, "Multi-select is now off", Toast.LENGTH_SHORT).show();
            }

            mUseBackKey = false;
            mPathLabel.setText(mFileMag.getCurrentDir());

            return false;

        } else if (keycode == KeyEvent.KEYCODE_BACK && !mUseBackKey && current.equals("/")) {
            finish();

            return false;
        }
        return false;
    }
}
