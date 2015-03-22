//package com.example.xiner.entity;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.Set;
//
//
///**
// *
// * 类型需要更改成更具体的类型
// *
// * 比如 PPT、音频、视频等等
// *
// * 求助的时候，可能需要 图片与语音同时存在
// *
// * 每一个消息都有可能是 文字、图片、语音 与 其他文件
// *
// * 那么，我们以文字消息作为基本单位，文字消息可能跟随着多个文件， 文件可以是图片、语音以及其他的文件
// *
// * @author seal
// *
// */
//
//public class Item implements Serializable{
//
//
//	private Long id;
//
//	private String content;
//
//
//	private Set<FileItem> fileItems;
//
//
//	private Date createdTime;
//
////	private Set<User> starBys;
//
//	private Long starNumber;
//
//    public Long getPraiseNumber() {
//        return praiseNumber;
//    }
//
//    public void setPraiseNumber(Long praiseNumber) {
//        this.praiseNumber = praiseNumber;
//    }
//
//    private Long praiseNumber;
//
//
//
//	private User owner;
//
//	/*
//	 * 第一次保存实体前
//	 */
//
//	void onUpdate() {
//		starNumber = 0L;
//		createdTime = new Date();
//	}
//
//    private String subject;
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}
//
//	/**
//	 * 当为文件类型的时候是 URL，
//	 *
//	 * @return
//	 */
//	public String getContent() {
//		return content;
//	}
//
////	public void setStarBys(Set<User> starBys) {
////		this.starBys = starBys;
////	}
////
////	public Set<User> getStarBys() {
////		return starBys;
////	}
//
//	public void setStarNumber(Long starNumber) {
//		this.starNumber = starNumber;
//	}
//
//	public Long getStarNumber() {
//		return starNumber;
//	}
//
//	public void setCreatedTime(Date createdTime) {
//		this.createdTime = createdTime;
//	}
//
//	public Date getCreatedTime() {
//		return createdTime;
//	}
//
//	public Set<FileItem> getFileItems() {
//		return fileItems;
//	}
//
//	public void setFileItems(Set<FileItem> fileItems) {
//		this.fileItems = fileItems;
//	}
//
//	public void setOwner(User owner) {
//		this.owner = owner;
//	}
//
//	public User getOwner() {
//		return owner;
//	}
//}
