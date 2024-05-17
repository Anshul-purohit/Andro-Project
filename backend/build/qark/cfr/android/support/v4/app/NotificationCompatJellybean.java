/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$BigPictureStyle
 *  android.app.Notification$BigTextStyle
 *  android.app.Notification$Builder
 *  android.app.Notification$InboxStyle
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.widget.RemoteViews
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

class NotificationCompatJellybean {
    private Notification.Builder b;

    /*
     * Enabled aggressive block sorting
     */
    public NotificationCompatJellybean(Context context, Notification notification, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n2, int n3, boolean bl, boolean bl2, int n4, CharSequence charSequence4) {
        context = new Notification.Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
        boolean bl3 = (notification.flags & 2) != 0;
        context = context.setOngoing(bl3);
        bl3 = (notification.flags & 8) != 0;
        context = context.setOnlyAlertOnce(bl3);
        bl3 = (notification.flags & 16) != 0;
        context = context.setAutoCancel(bl3).setDefaults(notification.defaults).setContentTitle(charSequence).setContentText(charSequence2).setSubText(charSequence4).setContentInfo(charSequence3).setContentIntent(pendingIntent).setDeleteIntent(notification.deleteIntent);
        bl3 = (notification.flags & 128) != 0;
        this.b = context.setFullScreenIntent(pendingIntent2, bl3).setLargeIcon(bitmap).setNumber(n).setUsesChronometer(bl2).setPriority(n4).setProgress(n2, n3, bl);
    }

    public void addAction(int n, CharSequence charSequence, PendingIntent pendingIntent) {
        this.b.addAction(n, charSequence, pendingIntent);
    }

    public void addBigPictureStyle(CharSequence charSequence, boolean bl, CharSequence charSequence2, Bitmap bitmap, Bitmap bitmap2, boolean bl2) {
        charSequence = new Notification.BigPictureStyle(this.b).setBigContentTitle(charSequence).bigPicture(bitmap);
        if (bl2) {
            charSequence.bigLargeIcon(bitmap2);
        }
        if (bl) {
            charSequence.setSummaryText(charSequence2);
        }
    }

    public void addBigTextStyle(CharSequence charSequence, boolean bl, CharSequence charSequence2, CharSequence charSequence3) {
        charSequence = new Notification.BigTextStyle(this.b).setBigContentTitle(charSequence).bigText(charSequence3);
        if (bl) {
            charSequence.setSummaryText(charSequence2);
        }
    }

    public void addInboxStyle(CharSequence charSequence, boolean bl, CharSequence charSequence2, ArrayList<CharSequence> arrayList) {
        charSequence = new Notification.InboxStyle(this.b).setBigContentTitle(charSequence);
        if (bl) {
            charSequence.setSummaryText(charSequence2);
        }
        charSequence2 = arrayList.iterator();
        while (charSequence2.hasNext()) {
            charSequence.addLine((CharSequence)charSequence2.next());
        }
    }

    public Notification build() {
        return this.b.build();
    }
}

