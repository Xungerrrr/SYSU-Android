package com.xungerrrr.healthyfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class StaticReceiver extends BroadcastReceiver {
    private static final String STATICACTION = "com.xungerrrr.healthyfood.MyStaticFilter";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(STATICACTION)) {
            Bundle bundle = intent.getExtras();
            Notification.Builder builder = new Notification.Builder(context);
            Intent contentIntent = new Intent(context, DetailActivity.class);
            contentIntent.putExtras(bundle);
            contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //对Builder进行配置，此处仅选取了几个
            builder.setContentTitle("今日推荐")   //设置通知栏标题：发件人
                    .setContentText(bundle.getString("name"))   //设置通知栏显示内容：短信内容
                    .setTicker("今日推荐 " + bundle.getString("name"))   //通知首次出现在通知栏，带上升动画效果的
                    .setSmallIcon(R.mipmap.empty_star)   //设置通知小ICON（通知栏），可以用以前的素材，例如空星
                    .setContentIntent(resultPendingIntent)   //传递内容
                    .setAutoCancel(true)   //设置这个标志当用户单击面板就可以让通知将自动取消
                    .setWhen(System.currentTimeMillis());
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.startup_channel_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("start", name, importance);
                manager.createNotificationChannel(channel);
                builder.setChannelId("start");
            }
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(0, notify);
        }
    }
}
