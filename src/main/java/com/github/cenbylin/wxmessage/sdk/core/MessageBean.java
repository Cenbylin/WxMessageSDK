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
	private String Location_X;
	private String Location_Y;
	private String Scale;
	private String Label;
	private String Recognition;
	private String ThumbMediaId;// 视频、小视频共有
	private String Event;
	private String EventKey;
	private String Latitude;
	private String Longitude;
	private String Precision;
	private String ticket;
	private String Url;//链接消息
	private String Title;//链接消息
	private String Description;//链接消息


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

    public String getLocation_X() {
        return Location_X;
    }

    public void setLocation_X(String location_X) {
        Location_X = location_X;
    }

    public String getLocation_Y() {
        return Location_Y;
    }

    public void setLocation_Y(String location_Y) {
        Location_Y = location_Y;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    /**

	 * 用属性集合map初始化MessageBean
	 * 
	 * @param map map
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
		Recognition = map.get("Recognition");
		ThumbMediaId = map.get("ThumbMediaId");
		Event = map.get("Event");
		EventKey = map.get("EventKey");
		Url = map.get("Url");
        Title = map.get("Title");
        Description = map.get("Description");
        ticket = map.get("ticket");
        Location_X = map.get("Location_X");
        Location_Y = map.get("Location_Y");
        Scale = map.get("Scale");
        Label = map.get("Label");
		Latitude = map.get("Latitude");
		Longitude = map.get("Longitude");
		Precision = map.get("Precision");
	}

	@Override
	public String toString() {

		return super.toString();
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
