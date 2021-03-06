/*
    Open Manager For Tablets, an open source file manager for the Android system
    Copyright (C) 2011  Joe Berria <nexesdevelopment@gmail.com>

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

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class is completely modular, which is to say that it has
 * no reference to the any GUI activity. This class could be taken
 * and placed into in other java (not just Android) project and work.
 * <br>
 * <br>
 * This class handles all file and folder operations on the system.
 * This class dictates how files and folders are copied/pasted, (un)zipped
 * renamed and searched. The EventHandler class will generally call these
 * methods and have them performed in a background thread. Threading is not
 * done in this class.  
 * 
 * @author Joe Berria
 *
 */
public class FileManager {
	private static final int BUFFER = 		2048;
	private static final int SORT_NONE = 	0;
	private static final int SORT_ALPHA = 	1;
	private static final int SORT_TYPE = 	2;
	private static final int SORT_SIZE = 	3;
	
	private boolean mShowHiddenFiles = false;
	private int mSortType = SORT_ALPHA;
	private Stack<String> mPathStack;
	private ArrayList<String> mDirContent;
	
	/**
	 * Constructs an object of the class
	 * <br>
	 * this class uses a stack to handle the navigation of directories.
	 */
	public FileManager() {
		mDirContent = new ArrayList<String>();
		mPathStack = new Stack<String>();
		
		mPathStack.push("/");
		mPathStack.push(mPathStack.peek() + "sdcard");

	}
	
	/**
	 * This will return a string of the current directory path
	 * @return the current directory
	 */
	public String getCurrentDir() {
		return mPathStack.peek();
	}

	/**
	 * This will return a string of the current home path.
	 * @return	the home directory
	 */
	public ArrayList<String> setHomeDir(String name) {
		//This will eventually be placed as a settings item
		mPathStack.clear();
		mPathStack.push("/");
		mPathStack.push(name);
		
		return populate_list();
	}
	
	/**
	 * This will determine if hidden files and folders will be visible to the
	 * user.
	 * @param choice	true if user is veiwing hidden files, false otherwise
	 */
	public void setShowHiddenFiles(boolean choice) {
		mShowHiddenFiles = choice;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setSortType(int type) {
		mSortType = type;
	}
	
	/**
	 * This will return a string that represents the path of the previous path
	 * @return	returns the previous path
	 */
	public ArrayList<String> getPreviousDir() {
		int size = mPathStack.size();
		
		if (size >= 2)
			mPathStack.pop();
		
		else if(size == 0)
			mPathStack.push("/");
		
		return populate_list();
	}
	
	/**
	 * 
	 * @param path
	 * @param isFullPath
	 * @return
	 */
	public ArrayList<String> getNextDir(String path, boolean isFullPath) {
		int size = mPathStack.size();
		
		if(!path.equals(mPathStack.peek()) && !isFullPath) {
			if(size == 1)
				mPathStack.push("/" + path);
			else
				mPathStack.push(mPathStack.peek() + "/" + path);
		}
		
		else if(!path.equals(mPathStack.peek()) && isFullPath) {
			mPathStack.push(path);
		}
		
		return populate_list();
	}

	private static final Comparator alph = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			return arg0.toLowerCase().compareTo(arg1.toLowerCase());
		}
	};
	
	private final Comparator size = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String dir = mPathStack.peek();
			Long first = new File(dir + "/" + arg0).length();
			Long second = new File(dir + "/" + arg1).length();
			
			return first.compareTo(second);
		}
	};
	
	private final Comparator type = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String ext = null;
			String ext2 = null;
			int ret;
			
			try {
				ext = arg0.substring(arg0.lastIndexOf(".") + 1, arg0.length()).toLowerCase();
				ext2 = arg1.substring(arg1.lastIndexOf(".") + 1, arg1.length()).toLowerCase();
				
			} catch (IndexOutOfBoundsException e) {
				return 0;
			}
			ret = ext.compareTo(ext2);
			
			if (ret == 0)
					return arg0.toLowerCase().compareTo(arg1.toLowerCase());
			
			return ret;
		}
	};
	
	/* (non-Javadoc)
	 * this function will take the string from the top of the directory stack
	 * and list all files/folders that are in it and return that list so 
	 * it can be displayed. Since this function is called every time we need
	 * to update the the list of files to be shown to the user, this is where 
	 * we do our sorting (by type, alphabetical, etc).
	 * 
	 * @return
	 */
	private ArrayList<String> populate_list() {
		
		if(!mDirContent.isEmpty())
			mDirContent.clear();
		
		File file = new File(mPathStack.peek());
		
		if(file.exists() && file.canRead()) {
			String[] list = file.list();
			int len = list.length;
			
			/* add files/folder to arraylist depending on hidden status */
			for (int i = 0; i < len; i++) {
				if(!mShowHiddenFiles) {
					if(list[i].toString().charAt(0) != '.')
						mDirContent.add(list[i]);
					
				} else {
					mDirContent.add(list[i]);
				}
			}
			
			/* sort the arraylist that was made from above for loop */
			switch(mSortType) {
				case SORT_NONE:
					//no sorting needed
					break;
					
				case SORT_ALPHA:
					Object[] tt = mDirContent.toArray();
					mDirContent.clear();
					
					Arrays.sort(tt, alph);
					
					for (Object a : tt){
						mDirContent.add((String)a);
					}
					break;
					
				case SORT_SIZE:
					int index = 0;
					Object[] size_ar = mDirContent.toArray();
					String dir = mPathStack.peek();
					
					Arrays.sort(size_ar, size);
					
					mDirContent.clear();
					for (Object a : size_ar) {
						if(new File(dir + "/" + (String)a).isDirectory())
							mDirContent.add(index++, (String)a);
						else
							mDirContent.add((String)a);
					}
					break;
					
				case SORT_TYPE:
					int dirindex = 0;
					Object[] type_ar = mDirContent.toArray();
					String current = mPathStack.peek();
					
					Arrays.sort(type_ar, type);
					mDirContent.clear();
					
					for (Object a : type_ar) {
						if(new File(current + "/" + (String)a).isDirectory())
							mDirContent.add(dirindex++, (String)a);
						else
							mDirContent.add((String)a);
					}
					break;
			}
				
		} else {
			mDirContent.add("Emtpy");
		}
		
		return mDirContent;
	}
}
