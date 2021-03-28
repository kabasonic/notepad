package com.kabasonic.notepad.ui.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.kabasonic.notepad.MainActivity;
import com.kabasonic.notepad.R;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "TAG";
    public static final String ONE_TIME = "onetime";




    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,TAG);
        //blocking thread

        wakeLock.acquire();

        //Создаем события, устанавливаем флаги (сброс уведомления, уведомление стирается при нажатии)
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent int1 = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Создаем уведомление, которое появится при срабатывании даты
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Напоминание")
                .setContentText("Текст вашего напоминания")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(int1)
                .setAutoCancel(true); // Важный параметр указывает, что нужно очистить уведомление при нажатии

        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, mBuilder.build());

        Log.d("Alarm manager thread", " Thread is a activated");
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra("openFragment", "AlarmDialogFragment");
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
        //unblocking thread
        wakeLock.release();
        Log.d("Alarm manager thread","Thread is a not activated");
    }



    public void setAlarm(Context context, long intervalMillis, int id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (context,AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME,Boolean.FALSE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),intervalMillis,pendingIntent);
    }

    public void cancelAlarm(Context context){
        Intent intent = new Intent(context,AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOneTimeTimer(Context context, int id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME,Boolean.TRUE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pendingIntent);
    }




}
