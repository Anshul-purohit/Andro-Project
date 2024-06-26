/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.RemoteViews
 *  java.lang.CharSequence
 *  java.lang.Deprecated
 *  java.lang.Object
 *  java.lang.System
 *  java.util.ArrayList
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompatGingerbread;
import android.support.v4.app.NotificationCompatHoneycomb;
import android.support.v4.app.NotificationCompatIceCreamSandwich;
import android.support.v4.app.NotificationCompatJellybean;
import android.widget.RemoteViews;
import java.util.ArrayList;

public class NotificationCompat {
    public static final int FLAG_HIGH_PRIORITY = 128;
    private static final NotificationCompatImpl IMPL = Build.VERSION.SDK_INT >= 16 ? new NotificationCompatImplJellybean() : (Build.VERSION.SDK_INT >= 14 ? new NotificationCompatImplIceCreamSandwich() : (Build.VERSION.SDK_INT >= 11 ? new NotificationCompatImplHoneycomb() : (Build.VERSION.SDK_INT >= 9 ? new NotificationCompatImplGingerbread() : new NotificationCompatImplBase())));
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;

    public static class Action {
        public PendingIntent actionIntent;
        public int icon;
        public CharSequence title;

        public Action(int n, CharSequence charSequence, PendingIntent pendingIntent) {
            this.icon = n;
            this.title = charSequence;
            this.actionIntent = pendingIntent;
        }
    }

    public static class BigPictureStyle
    extends Style {
        Bitmap mBigLargeIcon;
        boolean mBigLargeIconSet;
        Bitmap mPicture;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            this.setBuilder(builder);
        }

        public BigPictureStyle bigLargeIcon(Bitmap bitmap) {
            this.mBigLargeIcon = bitmap;
            this.mBigLargeIconSet = true;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap bitmap) {
            this.mPicture = bitmap;
            return this;
        }

        public BigPictureStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class BigTextStyle
    extends Style {
        CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            this.setBuilder(builder);
        }

        public BigTextStyle bigText(CharSequence charSequence) {
            this.mBigText = charSequence;
            return this;
        }

        public BigTextStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class Builder {
        ArrayList<Action> mActions = new ArrayList();
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        Context mContext;
        PendingIntent mFullScreenIntent;
        Bitmap mLargeIcon;
        Notification mNotification = new Notification();
        int mNumber;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        boolean mUseChronometer;

        public Builder(Context context) {
            this.mContext = context;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
        }

        private void setFlag(int n, boolean bl) {
            if (bl) {
                Notification notification = this.mNotification;
                notification.flags |= n;
                return;
            }
            Notification notification = this.mNotification;
            notification.flags &= ~ n;
        }

        public Builder addAction(int n, CharSequence charSequence, PendingIntent pendingIntent) {
            this.mActions.add((Object)new Action(n, charSequence, pendingIntent));
            return this;
        }

        public Notification build() {
            return IMPL.build(this);
        }

        @Deprecated
        public Notification getNotification() {
            return IMPL.build(this);
        }

        public Builder setAutoCancel(boolean bl) {
            this.setFlag(16, bl);
            return this;
        }

        public Builder setContent(RemoteViews remoteViews) {
            this.mNotification.contentView = remoteViews;
            return this;
        }

        public Builder setContentInfo(CharSequence charSequence) {
            this.mContentInfo = charSequence;
            return this;
        }

        public Builder setContentIntent(PendingIntent pendingIntent) {
            this.mContentIntent = pendingIntent;
            return this;
        }

        public Builder setContentText(CharSequence charSequence) {
            this.mContentText = charSequence;
            return this;
        }

        public Builder setContentTitle(CharSequence charSequence) {
            this.mContentTitle = charSequence;
            return this;
        }

        public Builder setDefaults(int n) {
            this.mNotification.defaults = n;
            if ((n & 4) != 0) {
                Notification notification = this.mNotification;
                notification.flags |= 1;
            }
            return this;
        }

        public Builder setDeleteIntent(PendingIntent pendingIntent) {
            this.mNotification.deleteIntent = pendingIntent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent pendingIntent, boolean bl) {
            this.mFullScreenIntent = pendingIntent;
            this.setFlag(128, bl);
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            this.mLargeIcon = bitmap;
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder setLights(int n, int n2, int n3) {
            int n4 = 1;
            this.mNotification.ledARGB = n;
            this.mNotification.ledOnMS = n2;
            this.mNotification.ledOffMS = n3;
            n = this.mNotification.ledOnMS != 0 && this.mNotification.ledOffMS != 0 ? 1 : 0;
            Notification notification = this.mNotification;
            n2 = this.mNotification.flags;
            n = n != 0 ? n4 : 0;
            notification.flags = n | n2 & -2;
            return this;
        }

        public Builder setNumber(int n) {
            this.mNumber = n;
            return this;
        }

        public Builder setOngoing(boolean bl) {
            this.setFlag(2, bl);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean bl) {
            this.setFlag(8, bl);
            return this;
        }

        public Builder setPriority(int n) {
            this.mPriority = n;
            return this;
        }

        public Builder setProgress(int n, int n2, boolean bl) {
            this.mProgressMax = n;
            this.mProgress = n2;
            this.mProgressIndeterminate = bl;
            return this;
        }

        public Builder setSmallIcon(int n) {
            this.mNotification.icon = n;
            return this;
        }

        public Builder setSmallIcon(int n, int n2) {
            this.mNotification.icon = n;
            this.mNotification.iconLevel = n2;
            return this;
        }

        public Builder setSound(Uri uri) {
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = -1;
            return this;
        }

        public Builder setSound(Uri uri, int n) {
            this.mNotification.sound = uri;
            this.mNotification.audioStreamType = n;
            return this;
        }

        public Builder setStyle(Style style2) {
            if (this.mStyle != style2) {
                this.mStyle = style2;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }

        public Builder setSubText(CharSequence charSequence) {
            this.mSubText = charSequence;
            return this;
        }

        public Builder setTicker(CharSequence charSequence) {
            this.mNotification.tickerText = charSequence;
            return this;
        }

        public Builder setTicker(CharSequence charSequence, RemoteViews remoteViews) {
            this.mNotification.tickerText = charSequence;
            this.mTickerView = remoteViews;
            return this;
        }

        public Builder setUsesChronometer(boolean bl) {
            this.mUseChronometer = bl;
            return this;
        }

        public Builder setVibrate(long[] arrl) {
            this.mNotification.vibrate = arrl;
            return this;
        }

        public Builder setWhen(long l) {
            this.mNotification.when = l;
            return this;
        }
    }

    public static class InboxStyle
    extends Style {
        ArrayList<CharSequence> mTexts = new ArrayList();

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            this.setBuilder(builder);
        }

        public InboxStyle addLine(CharSequence charSequence) {
            this.mTexts.add((Object)charSequence);
            return this;
        }

        public InboxStyle setBigContentTitle(CharSequence charSequence) {
            this.mBigContentTitle = charSequence;
            return this;
        }

        public InboxStyle setSummaryText(CharSequence charSequence) {
            this.mSummaryText = charSequence;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    static interface NotificationCompatImpl {
        public Notification build(Builder var1);
    }

    static class NotificationCompatImplBase
    implements NotificationCompatImpl {
        NotificationCompatImplBase() {
        }

        @Override
        public Notification build(Builder builder) {
            Notification notification = builder.mNotification;
            notification.setLatestEventInfo(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentIntent);
            if (builder.mPriority > 0) {
                notification.flags |= 128;
            }
            return notification;
        }
    }

    static class NotificationCompatImplGingerbread
    extends NotificationCompatImplBase {
        NotificationCompatImplGingerbread() {
        }

        @Override
        public Notification build(Builder builder) {
            Notification notification = builder.mNotification;
            notification.setLatestEventInfo(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentIntent);
            notification = NotificationCompatGingerbread.add(notification, builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentIntent, builder.mFullScreenIntent);
            if (builder.mPriority > 0) {
                notification.flags |= 128;
            }
            return notification;
        }
    }

    static class NotificationCompatImplHoneycomb
    implements NotificationCompatImpl {
        NotificationCompatImplHoneycomb() {
        }

        @Override
        public Notification build(Builder builder) {
            return NotificationCompatHoneycomb.add(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon);
        }
    }

    static class NotificationCompatImplIceCreamSandwich
    implements NotificationCompatImpl {
        NotificationCompatImplIceCreamSandwich() {
        }

        @Override
        public Notification build(Builder builder) {
            return NotificationCompatIceCreamSandwich.add(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        }
    }

    static class NotificationCompatImplJellybean
    implements NotificationCompatImpl {
        NotificationCompatImplJellybean() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public Notification build(Builder object) {
            NotificationCompatJellybean notificationCompatJellybean = new NotificationCompatJellybean(object.mContext, object.mNotification, object.mContentTitle, object.mContentText, object.mContentInfo, object.mTickerView, object.mNumber, object.mContentIntent, object.mFullScreenIntent, object.mLargeIcon, object.mProgressMax, object.mProgress, object.mProgressIndeterminate, object.mUseChronometer, object.mPriority, object.mSubText);
            for (Action action : object.mActions) {
                notificationCompatJellybean.addAction(action.icon, action.title, action.actionIntent);
            }
            if (object.mStyle == null) return notificationCompatJellybean.build();
            if (object.mStyle instanceof BigTextStyle) {
                object = (BigTextStyle)object.mStyle;
                notificationCompatJellybean.addBigTextStyle(object.mBigContentTitle, object.mSummaryTextSet, object.mSummaryText, object.mBigText);
                return notificationCompatJellybean.build();
            }
            if (object.mStyle instanceof InboxStyle) {
                object = (InboxStyle)object.mStyle;
                notificationCompatJellybean.addInboxStyle(object.mBigContentTitle, object.mSummaryTextSet, object.mSummaryText, object.mTexts);
                return notificationCompatJellybean.build();
            }
            if (!(object.mStyle instanceof BigPictureStyle)) return notificationCompatJellybean.build();
            object = (BigPictureStyle)object.mStyle;
            notificationCompatJellybean.addBigPictureStyle(object.mBigContentTitle, object.mSummaryTextSet, object.mSummaryText, object.mPicture, object.mBigLargeIcon, object.mBigLargeIconSet);
            return notificationCompatJellybean.build();
        }
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        public Notification build() {
            Notification notification = null;
            if (this.mBuilder != null) {
                notification = this.mBuilder.build();
            }
            return notification;
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (this.mBuilder != null) {
                    this.mBuilder.setStyle(this);
                }
            }
        }
    }

}

