# 中山大学数据科学与计算机学院本科生实验报告
## （2018年秋季学期）
| 课程名称 | 手机平台应用开发 | 任课老师 | 郑贵锋 |
| :------------: | :-------------: | :------------: | :-------------: |
| 年级 | 2016级 | 专业（方向） | 软件工程（数字媒体） |
| 学号 | 16340109 | 姓名 | 黎汛言 |
| 电话 | 15899822193 | Email | lixy248@mail2.sysu.edu.cn |
| 开始日期 | 2018.12.06 | 完成日期 | 2018.12.19 |

---

## 一、实验题目
## WEB API

---

## 二、实现内容

### 十四周任务
#### 实现一个bilibili的用户视频信息获取软件
<table>
    <tr>
        <td ><img src="/manual/images/img1.png" >打开程序主页面</td>
        <td ><img src="/manual/images/img2.png" >输入用户id，要求正整数int类型，不满足的弹Toast提示即可</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img3.png" >输入用户id，点击搜索，网络没打开则弹Toast提示网络连接失败</td>
        <td ><img src="/manual/images/img4.png" >网络打开情况下，输入用户id，不存在相应数据的弹Toast提示</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img5.png" >输入用户id = 2，点击搜索，展示图片/播放数/评论/时长/创建时间/标题/简介内容</td>
        <td ><img src="/manual/images/img6.png" >再次输入用户id = 7，接着上次结果继续展示以上内容</td>
    </tr>
</table>

* 搜索框只允许正整数int类型，不符合的需要弹Toast提示
*  当手机处于飞行模式或关闭wifi和移动数据的网络连接时，需要弹Toast提示
*  由于bilibili的API返回状态有很多，这次我们特别的限制在以下几点
    * 基础信息API接口为： `https://space.bilibili.com/ajax/top/showTop?mid=<user_id>`
    * 图片信息API接口为基础信息API返回的URL，cover字段
    * 只针对前40的用户id进行处理，即`user_id <= 40`
    * [2,7,10,19,20,24,32]都存在数据，需要正确显示
* **在图片加载出来前需要有一个加载条，不要求与加载进度同步**
* 布局和样式没有强制要求，只需要展示图片/播放数/评论/时长/创建时间/标题/简介的内容即可，可以自由发挥
* **布局需要使用到CardView和RecyclerView**
* 每个item最少使用2个CardView，布局怎样好看可以自由发挥，不发挥也行
* 不完成加分项的同学可以不显示SeekBar
* 输入框以及按钮需要一直处于顶部

#### 加分项
<table>
    <tr>
        <td ><img src="/manual/images/img7.png" >拖动SeekBar，显示相应位置的预览图</td>
        <td ><img src="/manual/images/img8.png" >拖动SeekBar，显示相应位置的预览图</td>
    </tr>
</table>

* 拖动前后均显示原图片
* 模拟bilibili网页PC端，完成可拖动的预览功能
* 拖动seekBar，预览图会相应改变
* 前40的用户id中，32不存在预览图，可以忽略也可以跟demo一样将seekbar的enable设置为false
* 需要额外使用两个API接口，分别为
    * 利用之前API获得的信息，得到aid传入`https://api.bilibili.com/pvideo?aid=<aid>`
    * 利用`api.bilibili.com`得到的信息，解析image字段得到`"http://i3.hdslb.com/bfs/videoshot/3668745.jpg` 的图片
    * 分割该图片即可完成预览功能
* 加分项存在一定难度，需要不少额外编码，**可不做**。
* 32不存在预览图，可忽略或处理该异常情况

### 十五周任务
#### 实现一个github用户repos以及issues应用
<table>
    <tr>
        <td ><img src="/manual/images/img9.png" >主界面有两个跳转按钮分别对应两次作业</td>
        <td ><img src="/manual/images/img10.png" >github界面，输入用户名搜索该用户所有可提交issue的repo，每个item可点击</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img11.png" >repo详情界面，显示该repo所有的issues</td>
        <td ><img src="/manual/images/img12.png" >加分项：在该用户的该repo下增加一条issue，输入title和body即可</td>
    </tr>
</table>

* 教程位于`./manual/tutorial_retrofit.md`
* 每次点击搜索按钮都会清空上次搜索结果再进行新一轮的搜索
* 获取repos时需要处理以下异常：HTTP 404 以及 用户没有任何repo
* 只显示 has_issues = true 的repo（即fork的他人的项目不会显示）
* repo显示的样式自由发挥，显示的内容可以自由增加（不能减少）
* repo的item可以点击跳转至下一界面
* 该repo不存在任何issue时需要弹Toast提示
* 不完成加分项的同学只需要显示所有issues即可，样式自由发挥，内容可以增加

#### 加分项
* 加分项旨在学习Retrofit2的POST请求，加深Rest API的理解
* demo需要你们额外提供TOKEN参数，这点不必实现，实现了反而不利于检查
* 提交的代码可以删除掉你的token等授权信息
* 仅处理提交成功与提交失败两种状态
* issue提交成功后需要刷新展示出来

* **加分项注意事项**
* **不要在他人repo下随意提交issue测试，自己新建一个repo即可**
* **管理好自己的github授权（token，client id等），验收结束后请删除对应授权**
---

## 三、实验结果

### 十四周任务
#### (1)实验截图

* 打开程序主页面

  ![1](./Screenshots/1.jpg)

* 输入非整数，显示toast提示

  ![2](./Screenshots/2.jpg)

* 输入ID 2，获取到数据，显示在卡片上

  ![3](./Screenshots/3.jpg)

* 输入ID 3，找不到数据，显示toast提示

  ![4](./Screenshots/4.jpg)

* 关闭网络连接后搜索，显示toast提示

  ![5](./Screenshots/5.jpg)

* 继续搜索ID 20，接着上次结果继续展示内容

  ![6](./Screenshots/6.jpg)

* 拖动 SeekBar，显示相应位置的预览图

  ![7](./Screenshots/7.jpg)

* 继续拖动 SeekBar，显示相应位置的预览图

  ![8](./Screenshots/8.jpg)

#### (2)实验步骤以及关键代码

* 主界面的布局

  ```xml
  <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context=".MainActivity"
      android:background="#EEEEEE">

      <EditText
          android:id="@+id/search_src_text"
          android:layout_width="0dp"
          android:layout_height="wrap_content"
          android:layout_marginLeft="15dp"
          android:layout_marginRight="15dp"
          android:layout_marginTop="15dp"
          android:layout_marginBottom="15dp"
          android:inputType="numberDecimal"
          android:hint="input user_id"
          android:gravity="center"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintRight_toLeftOf="@id/search_button"/>

      <Button
          android:id="@+id/search_button"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_marginRight="15dp"
          android:layout_marginEnd="15dp"
          android:text="搜索"
          app:layout_constraintRight_toRightOf="parent"
          app:layout_constraintLeft_toRightOf="@id/search_src_text"
          app:layout_constraintBottom_toBottomOf="@id/search_src_text"/>

      <android.support.v7.widget.RecyclerView
          android:id="@+id/recycler_view"
          android:layout_width="match_parent"
          android:layout_height="0dp"
          android:layout_marginTop="10dp"
          app:layout_constraintTop_toBottomOf="@id/search_src_text"
          app:layout_constraintBottom_toBottomOf="parent">

      </android.support.v7.widget.RecyclerView>

  </android.support.constraint.ConstraintLayout>
  ```

* RecyclerView Item的布局：

  ```xml
  <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      android:layout_width="match_parent"
      android:layout_height="wrap_content">

      <android.support.v7.widget.CardView
          android:id="@+id/card_1"
          xmlns:app="http://schemas.android.com/apk/res-auto"
          app:cardBackgroundColor="@android:color/transparent"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_marginEnd="8dp"
          android:layout_marginStart="8dp"
          android:layout_marginTop="8dp"
          app:cardCornerRadius="22dp"
          app:cardElevation="2dp"
          app:layout_constraintTop_toTopOf="parent">
          <android.support.constraint.ConstraintLayout
              android:layout_width="match_parent"
              android:layout_height="wrap_content">

              <ProgressBar
                  android:id="@+id/progress"
                  android:layout_width="match_parent"
                  android:layout_height="wrap_content"
                  android:layout_marginLeft="20dp"
                  android:layout_marginRight="20dp"
                  android:progress="50"
                  app:layout_constraintTop_toTopOf="@id/card_3"
                  app:layout_constraintBottom_toBottomOf="@id/card_3"/>

              <android.support.v7.widget.CardView
                  android:id="@+id/card_3"
                  xmlns:app="http://schemas.android.com/apk/res-auto"
                  app:cardBackgroundColor="@android:color/transparent"
                  android:layout_width="match_parent"
                  android:layout_height="wrap_content"
                  android:layout_marginLeft="10dp"
                  android:layout_marginRight="10dp"
                  android:layout_marginTop="10dp"
                  app:cardCornerRadius="15dp"
                  app:cardElevation="0dp"
                  app:layout_constraintTop_toTopOf="parent">

                  <android.support.constraint.ConstraintLayout
                      android:layout_width="match_parent"
                      android:layout_height="wrap_content">
                      <ImageView
                          android:id="@+id/cover"
                          android:layout_width="0dp"
                          android:layout_height="0dp"
                          android:scaleType="centerCrop"
                          app:layout_constraintStart_toStartOf="parent"
                          app:layout_constraintEnd_toEndOf="parent"
                          app:layout_constraintDimensionRatio="H, 16:9" />
                  </android.support.constraint.ConstraintLayout>

              </android.support.v7.widget.CardView>


              <SeekBar
                  android:id="@+id/seek"
                  android:layout_width="match_parent"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="10dp"
                  android:layout_marginBottom="10dp"
                  style="@style/Base.Widget.AppCompat.ProgressBar.Horizontal"
                  app:layout_constraintTop_toBottomOf="@id/card_3" />

              <TextView
                  android:id="@+id/title"
                  android:layout_width="0dp"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="10dp"
                  android:layout_marginLeft="10dp"
                  android:layout_marginRight="10dp"
                  android:text="标题"
                  android:textSize="18dp"
                  android:textColor="@color/black"
                  android:gravity="center"
                  app:layout_constraintTop_toBottomOf="@id/seek"
                  app:layout_constraintLeft_toLeftOf="parent"
                  app:layout_constraintRight_toRightOf="parent" />
              <TextView
                  android:id="@+id/play"
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:layout_marginLeft="10dp"
                  android:layout_marginTop="10dp"
                  android:text="播放："
                  app:layout_constraintLeft_toLeftOf="parent"
                  app:layout_constraintRight_toLeftOf="@id/video_review"
                  app:layout_constraintTop_toBottomOf="@id/title"/>
              <TextView
                  android:id="@+id/video_review"
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="10dp"
                  android:text="评论："
                  app:layout_constraintLeft_toRightOf="@id/play"
                  app:layout_constraintRight_toLeftOf="@id/duration"
                  app:layout_constraintTop_toBottomOf="@id/title"/>
              <TextView
                  android:id="@+id/duration"
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="10dp"
                  android:layout_marginRight="10dp"
                  android:text="时长："
                  app:layout_constraintLeft_toRightOf="@id/video_review"
                  app:layout_constraintRight_toRightOf="parent"
                  app:layout_constraintTop_toBottomOf="@id/title"/>
              <TextView
                  android:id="@+id/create"
                  android:layout_width="0dp"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="10dp"
                  android:layout_marginBottom="10dp"
                  android:layout_marginLeft="10dp"
                  android:layout_marginRight="10dp"
                  android:text="创建时间："
                  android:gravity="center"
                  app:layout_constraintTop_toBottomOf="@id/play"
                  app:layout_constraintBottom_toBottomOf="parent"
                  app:layout_constraintLeft_toLeftOf="parent"
                  app:layout_constraintRight_toRightOf="parent"/>


          </android.support.constraint.ConstraintLayout>
      </android.support.v7.widget.CardView>

      <android.support.v7.widget.CardView
          android:id="@+id/card_2"
          xmlns:app="http://schemas.android.com/apk/res-auto"
          app:cardBackgroundColor="@color/colorPrimary"
          android:outlineAmbientShadowColor="@color/black"
          android:outlineSpotShadowColor="@color/black"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_marginEnd="8dp"
          android:layout_marginStart="8dp"
          android:layout_marginTop="8dp"
          android:layout_marginBottom="8dp"
          app:cardCornerRadius="22dp"
          app:cardElevation="2dp"
          app:layout_constraintTop_toBottomOf="@id/card_1"
          app:layout_constraintBottom_toBottomOf="parent"
          app:cardPreventCornerOverlap="true">
          <android.support.constraint.ConstraintLayout
              android:layout_width="match_parent"
              android:layout_height="wrap_content">
              <TextView
                  android:id="@+id/content"
                  android:layout_width="0dp"
                  android:layout_height="wrap_content"
                  android:layout_marginTop="20dp"
                  android:layout_marginBottom="20dp"
                  android:layout_marginLeft="20dp"
                  android:layout_marginRight="20dp"
                  android:text="内容"
                  android:textColor="@color/white"
                  android:gravity="center"
                  app:layout_constraintTop_toTopOf="parent"
                  app:layout_constraintBottom_toBottomOf="parent"
                  app:layout_constraintLeft_toLeftOf="parent"
                  app:layout_constraintRight_toRightOf="parent" />
          </android.support.constraint.ConstraintLayout>

      </android.support.v7.widget.CardView>

  </android.support.constraint.ConstraintLayout>
  ```

* 点击搜索按钮，判断网络状态和输入信息格式，进行搜索

  ```java
  search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          ConnectivityManager connectivityManager
                  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
          if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
              try {
                  int integer = Integer.parseInt(id.getText().toString());
                  observable.subscribe(observer);
              } catch (NumberFormatException e) {
                  Toast.makeText(getApplicationContext(), "需要整数类型数据", Toast.LENGTH_SHORT).show();
              }
          }
          else {
              Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
          }
      }
  });
  ```

* 声明一个类，用于解析第一次请求的json

  ```java
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
  ```

* 声明一个类，用于解析第二次请求的json（加分项）

  ```java
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
  ```

* 定义观察者和被观察者，进行第一次API请求，并解析数据

  ```java
  observer = new Observer<RecyclerObj>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(RecyclerObj obj) {
          if (obj.getStatus()) {
              data.add(obj);
              adapter.notifyDataSetChanged();
          }
          else {
              Toast.makeText(getApplicationContext(), "数据库中不存在记录", Toast.LENGTH_SHORT).show();
          }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
  };

  observable = Observable.create(new ObservableOnSubscribe<RecyclerObj>() {
      @Override
      public void subscribe(ObservableEmitter<RecyclerObj> emitter) throws Exception {
          String text = "https://space.bilibili.com/ajax/top/showTop?mid=" + id.getText();
          URL url = new URL(text);
          HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
          httpURLConnection.setReadTimeout(3000);
          httpURLConnection.setConnectTimeout(3000);
          httpURLConnection.setRequestMethod("GET");
          httpURLConnection.setDoInput(true);
          httpURLConnection.connect();
          int responseCode = httpURLConnection.getResponseCode();
          if (responseCode != HttpURLConnection.HTTP_OK) {
              throw new IOException("HTTP error code:" + responseCode);
          }
          BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
              sb.append(line + "\n");
          }
          br.close();
          String jsonString = sb.toString();
          RecyclerObj recyclerObj = new RecyclerObj();
          if (TextUtils.equals(jsonString.substring(10, 15), "false")) {
              recyclerObj.setStatus(false);

          }
          else {
              recyclerObj = new Gson().fromJson(jsonString, RecyclerObj.class);
          }
          emitter.onNext(recyclerObj);
      }
  }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  ```

* 在RecyclerVierAdapter的convert函数中，定义新的观察者和被观察者，进行第二、三次API请求，获得图片和预览图，并进行图片的裁剪：

  ```java
  final ImageView cover = holder.getView(R.id.cover);
  final ProgressBar progress = holder.getView(R.id.progress);
  final SeekBar seek = holder.getView(R.id.seek);
  TextView play = holder.getView(R.id.play);
  TextView video_review = holder.getView(R.id.video_review);
  TextView duration = holder.getView(R.id.duration);
  TextView create = holder.getView(R.id.create);
  TextView title = holder.getView(R.id.title);
  TextView content = holder.getView(R.id.content);

  io.reactivex.Observer<Bitmap[]> observer = new Observer<Bitmap[]>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(Bitmap[] bitmaps) {
          holder.bitmaps = bitmaps;
          cover.setImageDrawable(new BitmapDrawable(bitmaps[0]));
          progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
  };
  io.reactivex.Observable<Bitmap[]> observable = Observable.create(new ObservableOnSubscribe<Bitmap[]>() {
      @Override
      public void subscribe(ObservableEmitter<Bitmap[]> emitter) throws Exception {
          URL url = new URL(obj.getData().getCover());
          url.openConnection();

          Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
          url = new URL("https://api.bilibili.com/pvideo?aid=" + obj.getData().getAid());
          HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
          httpURLConnection.setReadTimeout(3000);
          httpURLConnection.setConnectTimeout(3000);
          httpURLConnection.setRequestMethod("GET");
          httpURLConnection.setDoInput(true);
          httpURLConnection.connect();
          int responseCode = httpURLConnection.getResponseCode();
          if (responseCode != HttpURLConnection.HTTP_OK) {
              throw new IOException("HTTP error code:" + responseCode);
          }
          BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
              sb.append(line + "\n");
          }
          br.close();
          String jsonString = sb.toString();
          PreviewObj previewObj = new Gson().fromJson(jsonString, PreviewObj.class);
          int x = previewObj.getData().getImg_x_len();
          int y = previewObj.getData().getImg_y_len();
          int width = previewObj.getData().getImg_x_size();
          int height = previewObj.getData().getImg_y_size();
          Bitmap[] bitmaps = new Bitmap[x * y + 1];
          seek.setMax(x * y - 1);
          bitmaps[0] = image;
          url = new URL(previewObj.getData().getImage()[0]);
          Bitmap preview = BitmapFactory.decodeStream(url.openConnection().getInputStream());
          if (preview != null && preview.getWidth() == x * width && preview.getHeight() == y * height) {
              for (int i = 0; i < y; i++) {
                  for (int j = 0; j < x; j++) {
                      Bitmap bitmap = Bitmap.createBitmap(preview, j * width, i * height, width, height);
                      bitmaps[i * x + j + 1] = bitmap;
                  }
              }
          }
          else {
              seek.setEnabled(false);
          }
          //bitmaps[1] = Bitmap.createBitmap(preview, width, 0, width, height);
          emitter.onNext(bitmaps);
      }
  }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

  seek.setProgress(0);
  play.setText("播放：" + obj.getData().getPlay());
  video_review.setText("评论：" + obj.getData().getVideo_review());
  duration.setText("时长：" + obj.getData().getDuration());
  create.setText("创建时间：" + obj.getData().getCreate());
  title.setText(obj.getData().getTitle());
  content.setText(obj.getData().getContent());
  observable.subscribe(observer);
  ```

* 为SeekBar设置监听器，拖动时显示预览图：

  ```java
  seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          if (fromUser) {
              cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[progress + 1]));
          }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
          cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[1]));
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
          seekBar.setProgress(0);
          cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[0]));
      }
  });
  ```

#### (3)实验遇到的困难以及解决思路

困难：实验一开始，我的CardView不能够显示出来，RecyclerView中一添加新的Item就会闪退。

解决思路：经过测试，删掉CardView中的android:foreground属性后，CardView能够正确显示。

### 十五周任务
#### (1)实验截图

* 增加一个页面，用于跳转到两周的任务

  ![9](./Screenshots/9.jpg)

* 输入用户名，搜索，显示所有能提交issue的repo

  ![10](./Screenshots/10.jpg)

* 点击卡片，跳转到详情界面，显示所有的该repo所有开放的issue

  ![11](./Screenshots/11.jpg)

* 输入标题和内容，添加issue

  ![12](./Screenshots/12.jpg)

* 若repo下没有任何issue，弹Toast提示

  ![13](./Screenshots/13.jpg)

* 若用户不存在（404），弹Toast提示

  ![14](./Screenshots/14.jpg)

#### (2)实验步骤以及关键代码

* 新建两个Retrofit需要用到的Model类，提取repo和issue的信息：

  ```java
  public class Repo {
      int id;
      String name;
      String description;
      Boolean has_issues;
      int open_issues;
  }
  ```

  ```java
  public class Issue {
      String title;
      String state;
      String created_at;
      String body;
  }
  ```

* 创建访问API的接口，包括两个GET请求和一个POST请求：

  ```java
  public interface GitHubService {
      @GET("/users/{user_name}/repos")
      Observable<List<Repo>> getRepo(@Path("user_name") String user_name);
      @GET("/repos/{user_name}/{repo_name}/issues")
      Observable<List<Issue>> getIssue(@Path("user_name") String user_name, @Path("repo_name") String repo_name);
      @POST("/repos/{user_name}/{repo_name}/issues")
      @Headers("Authorization: token b60eb03fa3121381a3ff78485bd6341d0f7667d7")
      Observable<Issue> postIssue(@Body Issue issue, @Path("user_name") String user_name, @Path("repo_name") String repo_name);
  }
  ```

* 构造OkHttpClient、Retrofit对象，使用Retrofit实例化接口。

  ```java
  OkHttpClient build = new OkHttpClient.Builder()
          .connectTimeout(2, TimeUnit.SECONDS)
          .readTimeout(2, TimeUnit.SECONDS)
          .writeTimeout(2, TimeUnit.SECONDS)
          .build();
  Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://api.github.com/")
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(build)
          .build();
  final GitHubService gitHubService = retrofit.create(GitHubService.class);
  ```

* 点击搜索按钮，通过rxJava提交GET请求，获取repo，并更新UI

  ```java
  search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          ConnectivityManager connectivityManager
                  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
          if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
              Observable<List<Repo>> observable = gitHubService.getRepo(username.getText().toString())
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread());
              observable.subscribe(new Observer<List<Repo>>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(List<Repo> repos) {
                      data.clear();
                      adapter.notifyDataSetChanged();
                      if (repos.size() == 0) {
                          Toast.makeText(getApplicationContext(), "该用户没有任何repo", Toast.LENGTH_SHORT).show();
                          return;
                      }
                      for (int i = 0; i < repos.size(); i++) {
                          if (repos.get(i).has_issues) {
                              data.add(repos.get(i));
                          }
                      }
                      adapter.notifyDataSetChanged();
                  }

                  @Override
                  public void onError(Throwable e) {
                      data.clear();
                      adapter.notifyDataSetChanged();
                      Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onComplete() {

                  }
              });
          }
          else {
              Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
          }

      }
  });
  ```

* 点击卡片，跳转到详情页面

  ```java
  ((MyRecyclerViewAdapter) adapter).setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
      @Override
      public void onClick(int position) {
          Intent intent = new Intent(GitHubActivity.this, DetailActivity.class);
          Bundle bundle = new Bundle();
          bundle.putString("user", username.getText().toString());
          bundle.putString("repo", data.get(position).name);
          intent.putExtras(bundle);
          startActivity(intent);
      }

      @Override
      public void onLongClick(int position) {

      }
  });
  ```

* 在详情界面的onCreate中，通过rxJava提交GET请求，获取issue，并更新UI

  ```java
  Observable<List<Issue>> observable = gitHubService.getIssue(user, repo)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
  observable.subscribe(new Observer<List<Issue>>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(List<Issue> issues) {
          if (issues.size() == 0) {
              Toast.makeText(getApplicationContext(), "该repo不存在任何issue", Toast.LENGTH_SHORT).show();
          }
          for (int i = 0; i < issues.size(); i++) {
              data.add(issues.get(i));
          }
          adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
  });
  ```

* 点击添加按钮，通过rxJava提交POST请求，提交issue，并更新UI

  ```java
  add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(body.getText())) {
              Toast.makeText(getApplicationContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
              return;
          }
          ConnectivityManager connectivityManager
                  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
          if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
              Issue issue = new Issue();
              issue.title = title.getText().toString();
              issue.body = body.getText().toString();
              Observable<Issue> observable = gitHubService.postIssue(issue, user, repo)
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread());
              observable.subscribe(new Observer<Issue>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(Issue issue) {
                      data.add(0, issue);
                      adapter.notifyItemInserted(0);
                      Toast.makeText(getApplicationContext(), "增加成功", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onError(Throwable e) {
                      Toast.makeText(getApplicationContext(), "增加失败", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onComplete() {

                  }
              });
          }
          else {
              Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
          }
      }
  });
  ```

#### (3)实验遇到的困难以及解决思路

困难：做加分项时，在调用接口函数时将token作为参数传入，无法成功提交issue。

解决思路：经过查询，貌似Retrofit的@Headers只能添加静态头。在接口声明处将token写死在Headers中，就能成功提交POST请求，提交issue，成功解决问题。

---

## 四、实验思考及感想

在本次实验中，我实现了一个简单的访问Web API的应用，初步了解了安卓中多种网络请求的方法。

在实验中，我学到了许多有趣并且有用的知识，例如HttpURLConnection、rxJava、Retrofit、OkHttpClient等，对RESTFUL的概念有了更加深入的理解，对安卓应用的多线程编程、网络编程有了初步了解。通过复杂的编程实验训练，我对安卓应用的调试方法更加熟悉，代码出错的次数明显减少，程序的结构也更加规范和完善。