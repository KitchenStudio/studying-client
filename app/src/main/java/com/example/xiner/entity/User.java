package com.example.xiner.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * TODO 使用更简单的用户权限，而不是使用 Authorities 和 User 两个实体 是不是有必要做这样一次变化呢？
 * 
 * @author seal
 *
 */
/*
 * @manytomany 是多对多的关系，两个实体之间有主从之分也可以没有主从之分。
 * 如果有主从之分就使用 mappedBy 标识出谁是主（在从属的实体属性上标识）
 */


public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	public FileFigure getFileFigure() {
		return fileFigure;
	}

	public void setFileFigure(FileFigure fileFigure) {
		this.fileFigure = fileFigure;
	}

	private FileFigure fileFigure;
	private String username;

	private String password;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	int age;


//	private boolean accountNonExpired;
//
//	private boolean accountNonLocked;
//
//	private boolean credentialsNonExpired;

//	private boolean enabled;

	private String realname;

	private String nickname;


	private String mail;
	

//	private Set<Item> stars;
	

//	private Set<Item> items;






//	public User(String username, String password, Set<Authority> authorities) {
//		this.username = username;
//		this.password = password;
//		this.authorities = authorities;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealname() {
		return realname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMail() {
		return mail;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
