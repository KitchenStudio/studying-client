package com.example.xiner.entity;


import java.io.Serializable;

public class FileItem implements Serializable{
	

	private Long id;
	
	private String filename;
	
	private String url;
	
	private String type;

	private Item item;

	protected FileItem() {
		
	}
	
	public FileItem(String filename, String url, String type) {
		this.filename = filename;
		this.url = url;
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
	public static final String PICTURE = "PICTURE";
	public static final String AUDIO = "AUDIO";
	public static final String OTHER = "OTHER";
}
