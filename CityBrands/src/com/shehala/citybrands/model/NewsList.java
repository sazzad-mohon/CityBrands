package com.shehala.citybrands.model;

public class NewsList {

	private String id = "";
	private String head_line = "".replaceAll(" ", "%20");
	private String description = "";
	private String imageurl = "";
	private String video_url = "";
	private String created = "";
	private String modified = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHead_line() {
		return head_line;
	}
	public void setHead_line(String head_line) {
		this.head_line = head_line;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	@Override
	public String toString() {
		return "NewsList [id=" + id + ", head_line=" + head_line
				+ ", description=" + description + ", imageurl=" + imageurl
				+ ", video_url=" + video_url + ", created=" + created
				+ ", modified=" + modified + "]";
	}

	
}
