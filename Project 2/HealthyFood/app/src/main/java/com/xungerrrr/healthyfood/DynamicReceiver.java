package com.xungerrrr.healthyfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "com.xungerrrr.healthyfood.MyDynamicFilter";
    private static final String WIDGETDYNAMICACTION = "com.xungerrrr.healthyfood.MyWidgetDynamicFilter";
    @Override
    public void onReceive(Context context, Intent intent)  {
        if (intent.getAction().equals(DYNAMICACTION)) {
            Bundle bundle = intent.getExtras();
            Notification.Builder builder = new Notification.Builder(context);
            Intent contentIntent = new Intent(context, MainActivity.class);
            contentIntent.putExtras(bundle);
            contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //对Builder进行配置，此处仅选取了几个
            builder.setContentTitle("已收藏")   //设置通知栏标题：发件人
                    .setContentText(bundle.getString("name"))   //设置通知栏显示内容：短信内容
                    .setTicker("已收藏 " + bundle.getString("name"))   //通知首次出现在通知栏，带上升动画效果的
                    .setSmallIcon(R.mipmap.full_star)   //设置通知小ICON（通知栏），可以用以前的素材，例如空星
                    .setContentIntent(resultPendingIntent)   //传递内容
                    .setAutoCancel(true)   //设置这个标志当用户单击面板就可以让通知将自动取消
                    .setWhen(System.currentTimeMillis());

            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.collect_channel_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("collect", name, importance);
                manager.createNotificationChannel(channel);
                builder.setChannelId("collect");
            }
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(1, notify);
        }
        if (intent.getAction().equals(WIDGETDYNAMICACTION)){
            Bundle bundle = intent.getExtras();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.healthy_food_widget);
            views.setTextViewText(R.id.appwidget_text, "已收藏 " + bundle.getString("name"));
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent);
            ComponentName me = new ComponentName(context, HealthyFoodWidget.class);
            appWidgetManager.updateAppWidget(me, views);
        }
    }
}
