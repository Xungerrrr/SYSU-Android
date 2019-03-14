package com.xungerrrr.httpapi;

public class PreviewObj {
    private String code;
    private String message;
    private int ttl;
    private Data data;
    public static class Data  {
        private String pvdata;
        private int img_x_len;
        private int img_y_len;
        private int img_x_size;
        private int img_y_size;
        private String[] image;
        private int[] index;

        public String getPvdata() { return pvdata; }
        public int getImg_x_len() { return img_x_len; }
        public int getImg_y_len() { return img_y_len; }
        public int getImg_x_size() { return  img_x_size; }
        public int getImg_y_size() { return img_y_size; }
        public String[] getImage() { return image; }
        public int[] getIndex() { return index; }
    }
    public String getCode() { return code; }
    public String getMessage() { return  message; }
    public int getTtl() { return ttl; }
    public Data getData() { return data; }
}
