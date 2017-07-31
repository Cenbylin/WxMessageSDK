package com.github.cenbylin.wxmessage.sdk.core;

import java.util.Map;

public class MessageBean {
	private String ToUserName;// 各方式共有
	private String FromUserName;// 各方式共有
	private String CreateTime;// 各方式共有
	private String MsgType;// 各方式共有
	private String Content;
	private String MsgId;// 各方式共有
	private String PicUrl;
	private String MediaId;// 图片、语音共有
	private String Format;
	private String ThumbMediaId;// 视频、小视频共有
	private String Event;
	private String EventKey;
	private String ticket;

	/**
	 * 用属性集合map初始化MessageBean
	 * 
	 * @param map
	 */
	public void loadMap(Map<String, String> map) {
		ToUserName = map.get("ToUserName");
		FromUserName = map.get("FromUserName");
		CreateTime = map.get("CreateTime");
		MsgType = map.get("MsgType");
		Content = map.get("Content");
		MsgId = map.get("MsgId");
		PicUrl = map.get("PicUrl");
		MediaId = map.get("MediaId");
		Format = map.get("Format");
		ThumbMediaId = map.get("ThumbMediaId");
		Event = map.get("Event");
		EventKey = map.get("EventKey");
	}

	@Override
	public String toString() {

		return super.toString();
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
