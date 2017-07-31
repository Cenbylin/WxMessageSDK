package com.github.cenbylin.wxmessage.sdk.dev;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Cenbylin on 2017/7/31.
 */
public class NewsResBean {
    public List<Article> list = new LinkedList<Article>();
    public class Article {
        private String title;
        private String description;
        private String url;
        private String picurl;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public String getPicurl() {
            return picurl;
        }

        public Article(String title, String description, String url, String picurl) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.picurl = picurl;
        }
    }

    public void addArticle(String title, String description, String url, String picurl){
        list.add(new Article(title, description, url, picurl));
    }

}
