package com.xungerrrr.httpapi;

import android.graphics.Bitmap;

public class RecyclerObj {
    private Boolean status;
    private Data data;
    public static class Data  {
        private int aid;
		private int state;
		private String cover;
		private String title;
		private String content;
		private int play;
		private String duration;
        private int video_review;
        private String create;
        private String rec;
        private int count;
        private Bitmap bitmap;

        public int getAid() { return aid; }
        public int getState() { return state; }
        public String getCover() { return cover; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public int getPlay() { return play; }
        public String getDuration() { return duration; }
        public int getVideo_review() { return video_review; }
        public String getCreate() { return create; }
        public String getRec() { return rec; }
        public int getCount() { return count; }
        public Bitmap getBitmap() { return bitmap; }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    public Boolean getStatus() { return status; }
    public Data getData() { return data; }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
