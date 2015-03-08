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

package com.example.xiner.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;

import java.io.File;
import java.util.ArrayList;

/**
 * This class sits between the Main activity and the FileManager class. 
 * To keep the FileManager class modular, this class exists to handle 
 * UI events and communicate that information to the FileManger class
 * 
 * This class is responsible for the buttons onClick method. If one needs
 * to change the functionality of the buttons found from the Main activity
 * or add button logic, this is the class that will need to be edited.
 * 
 * This class is responsible for handling the information that is displayed
 * from the list view (the files and folder) with a a nested class TableRow.
 * The TableRow class is responsible for displaying which icon is shown for each
 * entry. For example a folder will display the folder icon, a Word doc will 
 * display a word icon and so on. If more icons are to be added, the TableRow 
 * class must be updated to display those changes. 
 * 
 * @author Joe Berria
 */
public class EventHandler implements OnClickListener {
	/*
	 * Unique types to control which file operation gets
	 * performed in the background
	 */
	private final Context mContext;
	public final FileManager mFileMang;
	private ThumbnailCreator mThumbnail;

    public TableRow getmDelegate() {
        return mDelegate;
    }

    public void setmDelegate(TableRow mDelegate) {
        this.mDelegate = mDelegate;
    }

    private TableRow mDelegate;



    private boolean multi_select_flag = false;
	private boolean delete_after_copy = false;
	private boolean thumbnail_flag = true;
	private int mColor = Color.WHITE;
	
	//the list used to feed info into the array adapter and when multi-select is on
	private ArrayList<String> mDataSource;



    private ArrayList<String> mMultiSelectData;
	private TextView mPathLabel;
	private TextView mInfoLabel;

    public boolean isMulti_select_flag() {
        return multi_select_flag;
    }

    public void setMulti_select_flag(boolean multi_select_flag) {
        this.multi_select_flag = multi_select_flag;
    }
    public ArrayList<String> getmMultiSelectData() {
        return mMultiSelectData;
    }

    public void setmMultiSelectData(ArrayList<String> mMultiSelectData) {
        this.mMultiSelectData = mMultiSelectData;
    }
	/**
	 * Creates an EventHandler object. This object is used to communicate
	 * most work from the Main activity to the FileManager class.
	 */
	public EventHandler(Context context, final FileManager manager) {
		mContext = context;
		mFileMang = manager;
		
		mDataSource = new ArrayList<String>(mFileMang.setHomeDir
						(Environment.getExternalStorageDirectory().getPath()));
	}
	
	/**
	 * This constructor is called if the user has changed the screen orientation
	 * and does not want the directory to be reset to home. 
	 *
	 * @param manager	The FileManager object that was instantiated from Main
	 * @param location	The first directory to display to the user
	 */
	public EventHandler(Context context, final FileManager manager, String location) {
		mContext = context;
		mFileMang = manager;
		
		mDataSource = new ArrayList<String>(mFileMang.getNextDir(location, true));
	}

	/**
	 * This method is called from the Main activity and this has the same
	 * reference to the same object so when changes are made here or there
	 * they will display in the same way.
	 * 
	 * @param adapter	The TableRow object
	 */
	public void setListAdapter(TableRow adapter) {
		mDelegate = adapter;
	}
	
	/**
	 * This method is called from the Main activity and is passed
	 * the TextView that should be updated as the directory changes
	 * so the user knows which folder they are in.
	 * 
	 * @param path	The label to update as the directory changes
	 * @param label	the label to update information
	 */
	public void setUpdateLabels(TextView path, TextView label) {
		mPathLabel = path;
		mInfoLabel = label;
	}
	
	/**
	 *
	 * @param color
	 */
	public void setTextColor(int color) {
		mColor = color;
	}
	
	/**
	 * Set this true and thumbnails will be used as the icon for image files. False will
	 * show a default image. 
	 * 
	 * @param show
	 */
	public void setShowThumbnails(boolean show) {
		thumbnail_flag = show;
	}
	
	/**
	 * Indicates whether the user wants to select 
	 * multiple files or folders at a time.
	 * <br><br>
	 * false by default
	 * 
	 * @return	true if the user has turned on multi selection
	 */
	public boolean isMultiSelected() {
		return multi_select_flag;
	}

	/**
	 * this will stop our background thread that creates thumbnail icons
	 * if the thread is running. this should be stopped when ever 
	 * we leave the folder the image files are in.
	 */
	public void stopThumbnailThread() {
		if (mThumbnail != null) {
			mThumbnail.setCancelThumbnails(true);
			mThumbnail = null;
		}
	}

	/**
	 *  This method, handles the button presses of the top buttons found
	 *  in the Main activity. 
	 */
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
			case R.id.back_button:
				if (mFileMang.getCurrentDir() != "/") {
					if(multi_select_flag) {
						mDelegate.killMultiSelect(true);
						Toast.makeText(mContext, "Multi-select is now off", 
									   Toast.LENGTH_SHORT).show();
					}
					
					stopThumbnailThread();
					updateDirectory(mFileMang.getPreviousDir());
					if(mPathLabel != null)
						mPathLabel.setText(mFileMang.getCurrentDir());
				}
				break;
			
			case R.id.home_button:		
				if(multi_select_flag) {
					mDelegate.killMultiSelect(true);
					Toast.makeText(mContext, "Multi-select is now off", 
								   Toast.LENGTH_SHORT).show();
				}
				stopThumbnailThread();
                String external = Environment.getExternalStorageDirectory().getPath();
                Log.v("EventHandler",external+"externer");
				updateDirectory(mFileMang.setHomeDir(external));
				if(mPathLabel != null)
					mPathLabel.setText(mFileMang.getCurrentDir());
				break;
		}
	}

	/**
	 * will return the data in the ArrayList that holds the dir contents. 
	 * 
	 * @param position	the indext of the arraylist holding the dir content
	 * @return the data in the arraylist at position (position)
	 */
	public String getData(int position) {
		
		if(position > mDataSource.size() - 1 || position < 0)
			return null;
		
		return mDataSource.get(position);
	}

	/**
	 * called to update the file contents as the user navigates there
	 * phones file system. 
	 * 
	 * @param content	an ArrayList of the file/folders in the current directory.
	 */
	public void updateDirectory(ArrayList<String> content) {	
		if(!mDataSource.isEmpty())
			mDataSource.clear();
		
		for(String data : content)
			mDataSource.add(data);
		
		mDelegate.notifyDataSetChanged();
	}


	private static class ViewHolder {
		TextView topView;
		ImageView icon;
		ImageView mSelect;	//multi-select check mark icon
        CheckBox checkBox;
	}

	
	/**
	 * A nested class to handle displaying a custom view in the ListView that
	 * is used in the Main activity. If any icons are to be added, they must
	 * be implemented in the getView method. This class is instantiated once in Main
	 * and has no reason to be instantiated again. 
	 * 
	 * @author Joe Berria
	 */
    public class TableRow extends ArrayAdapter<String> {
    	private final int KB = 1024;
    	private final int MG = KB * KB;
    	private final int GB = MG * KB;    	
    	private String display_size;
    	private ArrayList<Integer> positions;
    	private LinearLayout hidden_layout;
        boolean mutis;
    	
    	public TableRow(boolean multi) {
    		super(mContext, R.layout.tablerow, mDataSource);
            this.mutis = multi;

    	}

        public void addMultiPosition(int index, String path) {
    		if(positions == null)
    			positions = new ArrayList<Integer>();
    		
    		if(mMultiSelectData == null) {
    			positions.add(index);
    			add_multiSelect_file(path);
    			
    		} else if(mMultiSelectData.contains(path)) {
    			if(positions.contains(index))
    				positions.remove(new Integer(index));
    			
    			mMultiSelectData.remove(path);
    			
    		} else {
    			positions.add(index);
    			add_multiSelect_file(path);
    		}
    		
    		notifyDataSetChanged();
    	}
   	
    	/**
    	 * This will turn off multi-select and hide the multi-select buttons at the
    	 * bottom of the view. 
    	 * 
    	 * @param clearData if this is true any files/folders the user selected for multi-select
    	 * 					will be cleared. If false, the data will be kept for later use. Note:
    	 * 					multi-select copy and move will usually be the only one to pass false, 
    	 * 					so we can later paste it to another folder.
    	 */
    	public void killMultiSelect(boolean clearData) {
    		hidden_layout = (LinearLayout)((Activity)mContext).findViewById(R.id.hidden_buttons);
    		hidden_layout.setVisibility(LinearLayout.GONE);
    		multi_select_flag = false;
    		
    		if(positions != null && !positions.isEmpty())
    			positions.clear();
    		
    		if(clearData)
    			if(mMultiSelectData != null && !mMultiSelectData.isEmpty())
    				mMultiSelectData.clear();
    		
    		notifyDataSetChanged();
    	}
    	
    	public String getFilePermissions(File file) {
    		String per = "-";
    	    		
    		if(file.isDirectory())
    			per += "d";
    		if(file.canRead())
    			per += "r";
    		if(file.canWrite())
    			per += "w";
    		
    		return per;
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder mViewHolder;
            int num_items = 0;
            String temp = mFileMang.getCurrentDir();
            File file = new File(temp + "/" + mDataSource.get(position));
            String[] list = file.list();

            if (list != null)
                num_items = list.length;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tablerow, parent, false);

                mViewHolder = new ViewHolder();
                mViewHolder.topView = (TextView) convertView.findViewById(R.id.top_view);
                mViewHolder.icon = (ImageView) convertView.findViewById(R.id.row_image);
                mViewHolder.mSelect = (ImageView) convertView.findViewById(R.id.multiselect_icon);

//                mViewHolder.checkBox =(CheckBox)convertView.findViewById(R.id.checkbox_list);

                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();

            }
            if(mutis){
                mViewHolder.checkBox.setVisibility(View.VISIBLE);
            }
            if (positions != null && positions.contains(position))
                mViewHolder.mSelect.setVisibility(ImageView.VISIBLE);
            else
                mViewHolder.mSelect.setVisibility(ImageView.GONE);

            if (mThumbnail == null)
                mThumbnail = new ThumbnailCreator(52, 52);

            if (file != null && file.isFile()) {
                String ext = file.toString();
                String sub_ext = ext.substring(ext.lastIndexOf(".") + 1);
    			
    			/* This series of else if statements will determine which 
    			 * icon is displayed 
    			 */
                if (sub_ext.equalsIgnoreCase("pdf")) {
                    mViewHolder.icon.setImageResource(R.drawable.pdf);

                } else if (sub_ext.equalsIgnoreCase("mp3") ||
                        sub_ext.equalsIgnoreCase("wma") ||
                        sub_ext.equalsIgnoreCase("m4a") ||
                        sub_ext.equalsIgnoreCase("m4p")) {

                    mViewHolder.icon.setImageResource(R.drawable.music);

                } else if (sub_ext.equalsIgnoreCase("png") ||
                        sub_ext.equalsIgnoreCase("jpg") ||
                        sub_ext.equalsIgnoreCase("jpeg") ||
                        sub_ext.equalsIgnoreCase("gif") ||
                        sub_ext.equalsIgnoreCase("tiff")) {

                    if (thumbnail_flag && file.length() != 0) {
                        Bitmap thumb = mThumbnail.isBitmapCached(file.getPath());

                        if (thumb == null) {
                            final Handler handle = new Handler(new Handler.Callback() {
                                public boolean handleMessage(Message msg) {
                                    notifyDataSetChanged();

                                    return true;
                                }
                            });

                            mThumbnail.createNewThumbnail(mDataSource, mFileMang.getCurrentDir(), handle);

                            if (!mThumbnail.isAlive())
                                mThumbnail.start();

                        } else {
                            mViewHolder.icon.setImageBitmap(thumb);
                        }

                    } else {
                        mViewHolder.icon.setImageResource(R.drawable.image);
                    }

                } else if (sub_ext.equalsIgnoreCase("zip") ||
                        sub_ext.equalsIgnoreCase("gzip") ||
                        sub_ext.equalsIgnoreCase("gz")) {

                    mViewHolder.icon.setImageResource(R.drawable.zip);

                } else if (sub_ext.equalsIgnoreCase("m4v") ||
                        sub_ext.equalsIgnoreCase("wmv") ||
                        sub_ext.equalsIgnoreCase("3gp") ||
                        sub_ext.equalsIgnoreCase("mp4")) {

                    mViewHolder.icon.setImageResource(R.drawable.movies);

                } else if (sub_ext.equalsIgnoreCase("doc") ||
                        sub_ext.equalsIgnoreCase("docx")) {

                    mViewHolder.icon.setImageResource(R.drawable.word);

                } else if (sub_ext.equalsIgnoreCase("xls") ||
                        sub_ext.equalsIgnoreCase("xlsx")) {

                    mViewHolder.icon.setImageResource(R.drawable.excel);

                } else if (sub_ext.equalsIgnoreCase("ppt") ||
                        sub_ext.equalsIgnoreCase("pptx")) {

                    mViewHolder.icon.setImageResource(R.drawable.ppt);

                } else if (sub_ext.equalsIgnoreCase("html")) {
                    mViewHolder.icon.setImageResource(R.drawable.html32);

                } else if (sub_ext.equalsIgnoreCase("xml")) {
                    mViewHolder.icon.setImageResource(R.drawable.xml32);

                } else if (sub_ext.equalsIgnoreCase("conf")) {
                    mViewHolder.icon.setImageResource(R.drawable.config32);

                } else if (sub_ext.equalsIgnoreCase("apk")) {
                    mViewHolder.icon.setImageResource(R.drawable.appicon);

                } else if (sub_ext.equalsIgnoreCase("jar")) {
                    mViewHolder.icon.setImageResource(R.drawable.jar32);

                } else {
                    mViewHolder.icon.setImageResource(R.drawable.text);
                }

            } else if (file != null && file.isDirectory()) {
                if (file.canRead() && file.list().length > 0)
                    mViewHolder.icon.setImageResource(R.drawable.folder_full);
                else
                    mViewHolder.icon.setImageResource(R.drawable.folder);
            }

            String permission = getFilePermissions(file);

            if (file.isFile()) {
                double size = file.length();
                if (size > GB)
                    display_size = String.format("%.2f Gb ", (double) size / GB);
                else if (size < GB && size > MG)
                    display_size = String.format("%.2f Mb ", (double) size / MG);
                else if (size < MG && size > KB)
                    display_size = String.format("%.2f Kb ", (double) size / KB);
                else
                    display_size = String.format("%.2f bytes ", (double) size);

    		}
                mViewHolder.topView.setText(file.getName());

                return convertView;
            }

    	private void add_multiSelect_file(String src) {
    		if(mMultiSelectData == null)
    			mMultiSelectData = new ArrayList<String>();
    		
    		mMultiSelectData.add(src);
    	}
    }

}
