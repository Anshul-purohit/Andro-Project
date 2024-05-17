package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.Notification.InboxStyle;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

class NotificationCompatJellybean {
   // $FF: renamed from: b android.app.Notification.Builder
   private Builder field_0;

   public NotificationCompatJellybean(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, int var15, CharSequence var16) {
      Builder var18 = (new Builder(var1)).setWhen(var2.when).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
      boolean var17;
      if ((var2.flags & 2) != 0) {
         var17 = true;
      } else {
         var17 = false;
      }

      var18 = var18.setOngoing(var17);
      if ((var2.flags & 8) != 0) {
         var17 = true;
      } else {
         var17 = false;
      }

      var18 = var18.setOnlyAlertOnce(var17);
      if ((var2.flags & 16) != 0) {
         var17 = true;
      } else {
         var17 = false;
      }

      var18 = var18.setAutoCancel(var17).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var16).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
      if ((var2.flags & 128) != 0) {
         var17 = true;
      } else {
         var17 = false;
      }

      this.field_0 = var18.setFullScreenIntent(var9, var17).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var14).setPriority(var15).setProgress(var11, var12, var13);
   }

   public void addAction(int var1, CharSequence var2, PendingIntent var3) {
      this.field_0.addAction(var1, var2, var3);
   }

   public void addBigPictureStyle(CharSequence var1, boolean var2, CharSequence var3, Bitmap var4, Bitmap var5, boolean var6) {
      BigPictureStyle var7 = (new BigPictureStyle(this.field_0)).setBigContentTitle(var1).bigPicture(var4);
      if (var6) {
         var7.bigLargeIcon(var5);
      }

      if (var2) {
         var7.setSummaryText(var3);
      }

   }

   public void addBigTextStyle(CharSequence var1, boolean var2, CharSequence var3, CharSequence var4) {
      BigTextStyle var5 = (new BigTextStyle(this.field_0)).setBigContentTitle(var1).bigText(var4);
      if (var2) {
         var5.setSummaryText(var3);
      }

   }

   public void addInboxStyle(CharSequence var1, boolean var2, CharSequence var3, ArrayList var4) {
      InboxStyle var5 = (new InboxStyle(this.field_0)).setBigContentTitle(var1);
      if (var2) {
         var5.setSummaryText(var3);
      }

      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.addLine((CharSequence)var6.next());
      }

   }

   public Notification build() {
      return this.field_0.build();
   }
}
