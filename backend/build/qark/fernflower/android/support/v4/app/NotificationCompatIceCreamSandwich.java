package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

class NotificationCompatIceCreamSandwich {
   static Notification add(Context var0, Notification var1, CharSequence var2, CharSequence var3, CharSequence var4, RemoteViews var5, int var6, PendingIntent var7, PendingIntent var8, Bitmap var9, int var10, int var11, boolean var12) {
      Builder var14 = (new Builder(var0)).setWhen(var1.when).setSmallIcon(var1.icon, var1.iconLevel).setContent(var1.contentView).setTicker(var1.tickerText, var5).setSound(var1.sound, var1.audioStreamType).setVibrate(var1.vibrate).setLights(var1.ledARGB, var1.ledOnMS, var1.ledOffMS);
      boolean var13;
      if ((var1.flags & 2) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      var14 = var14.setOngoing(var13);
      if ((var1.flags & 8) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      var14 = var14.setOnlyAlertOnce(var13);
      if ((var1.flags & 16) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      var14 = var14.setAutoCancel(var13).setDefaults(var1.defaults).setContentTitle(var2).setContentText(var3).setContentInfo(var4).setContentIntent(var7).setDeleteIntent(var1.deleteIntent);
      if ((var1.flags & 128) != 0) {
         var13 = true;
      } else {
         var13 = false;
      }

      return var14.setFullScreenIntent(var8, var13).setLargeIcon(var9).setNumber(var6).setProgress(var10, var11, var12).getNotification();
   }
}
