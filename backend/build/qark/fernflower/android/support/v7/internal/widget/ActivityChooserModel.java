package android.support.v7.internal.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChooserModel extends DataSetObservable {
   private static final String ATTRIBUTE_ACTIVITY = "activity";
   private static final String ATTRIBUTE_TIME = "time";
   private static final String ATTRIBUTE_WEIGHT = "weight";
   private static final boolean DEBUG = false;
   private static final int DEFAULT_ACTIVITY_INFLATION = 5;
   private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;
   public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
   public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
   private static final String HISTORY_FILE_EXTENSION = ".xml";
   private static final int INVALID_INDEX = -1;
   private static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
   private static final String TAG_HISTORICAL_RECORD = "historical-record";
   private static final String TAG_HISTORICAL_RECORDS = "historical-records";
   private static final Map sDataModelRegistry = new HashMap();
   private static final Object sRegistryLock = new Object();
   private final List mActivities = new ArrayList();
   private ActivityChooserModel.OnChooseActivityListener mActivityChoserModelPolicy;
   private ActivityChooserModel.ActivitySorter mActivitySorter = new ActivityChooserModel.DefaultSorter();
   private boolean mCanReadHistoricalData = true;
   private final Context mContext;
   private final List mHistoricalRecords = new ArrayList();
   private boolean mHistoricalRecordsChanged = true;
   private final String mHistoryFileName;
   private int mHistoryMaxSize = 50;
   private final Object mInstanceLock = new Object();
   private Intent mIntent;
   private boolean mReadShareHistoryCalled = false;
   private boolean mReloadActivities = false;

   private ActivityChooserModel(Context var1, String var2) {
      this.mContext = var1.getApplicationContext();
      if (!TextUtils.isEmpty(var2) && !var2.endsWith(".xml")) {
         this.mHistoryFileName = var2 + ".xml";
      } else {
         this.mHistoryFileName = var2;
      }
   }

   // $FF: synthetic method
   static Context access$200(ActivityChooserModel var0) {
      return var0.mContext;
   }

   // $FF: synthetic method
   static String access$300() {
      return LOG_TAG;
   }

   // $FF: synthetic method
   static String access$400(ActivityChooserModel var0) {
      return var0.mHistoryFileName;
   }

   // $FF: synthetic method
   static boolean access$502(ActivityChooserModel var0, boolean var1) {
      var0.mCanReadHistoricalData = var1;
      return var1;
   }

   private boolean addHisoricalRecord(ActivityChooserModel.HistoricalRecord var1) {
      boolean var2 = this.mHistoricalRecords.add(var1);
      if (var2) {
         this.mHistoricalRecordsChanged = true;
         this.pruneExcessiveHistoricalRecordsIfNeeded();
         this.persistHistoricalDataIfNeeded();
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

      return var2;
   }

   private void ensureConsistentState() {
      boolean var1 = this.loadActivitiesIfNeeded();
      boolean var2 = this.readHistoricalDataIfNeeded();
      this.pruneExcessiveHistoricalRecordsIfNeeded();
      if (var1 | var2) {
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

   }

   private void executePersistHistoryAsyncTaskBase() {
      (new ActivityChooserModel.PersistHistoryAsyncTask()).execute(new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
   }

   private void executePersistHistoryAsyncTaskSDK11() {
      (new ActivityChooserModel.PersistHistoryAsyncTask()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
   }

   public static ActivityChooserModel get(Context var0, String var1) {
      Object var4 = sRegistryLock;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label176: {
         ActivityChooserModel var3;
         try {
            var3 = (ActivityChooserModel)sDataModelRegistry.get(var1);
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label176;
         }

         ActivityChooserModel var2 = var3;
         if (var3 == null) {
            try {
               var2 = new ActivityChooserModel(var0, var1);
               sDataModelRegistry.put(var1, var2);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            return var2;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private boolean loadActivitiesIfNeeded() {
      boolean var4 = false;
      boolean var3 = var4;
      if (this.mReloadActivities) {
         var3 = var4;
         if (this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            List var5 = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            int var2 = var5.size();

            for(int var1 = 0; var1 < var2; ++var1) {
               ResolveInfo var6 = (ResolveInfo)var5.get(var1);
               this.mActivities.add(new ActivityChooserModel.ActivityResolveInfo(var6));
            }

            var3 = true;
         }
      }

      return var3;
   }

   private void persistHistoricalDataIfNeeded() {
      if (!this.mReadShareHistoryCalled) {
         throw new IllegalStateException("No preceding call to #readHistoricalData");
      } else {
         if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty(this.mHistoryFileName)) {
               if (VERSION.SDK_INT >= 11) {
                  this.executePersistHistoryAsyncTaskSDK11();
                  return;
               }

               this.executePersistHistoryAsyncTaskBase();
               return;
            }
         }

      }
   }

   private void pruneExcessiveHistoricalRecordsIfNeeded() {
      int var2 = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
      if (var2 > 0) {
         this.mHistoricalRecordsChanged = true;

         for(int var1 = 0; var1 < var2; ++var1) {
            ActivityChooserModel.HistoricalRecord var3 = (ActivityChooserModel.HistoricalRecord)this.mHistoricalRecords.remove(0);
         }
      }

   }

   private boolean readHistoricalDataIfNeeded() {
      if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty(this.mHistoryFileName)) {
         this.mCanReadHistoricalData = false;
         this.mReadShareHistoryCalled = true;
         this.readHistoricalDataImpl();
         return true;
      } else {
         return false;
      }
   }

   private void readHistoricalDataImpl() {
      // $FF: Couldn't be decompiled
   }

   private boolean sortActivitiesIfNeeded() {
      if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
         this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
         return true;
      } else {
         return false;
      }
   }

   public Intent chooseActivity(int var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label208: {
         try {
            if (this.mIntent == null) {
               return null;
            }
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label208;
         }

         Intent var4;
         ComponentName var26;
         try {
            this.ensureConsistentState();
            ActivityChooserModel.ActivityResolveInfo var3 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(var1);
            var26 = new ComponentName(var3.resolveInfo.activityInfo.packageName, var3.resolveInfo.activityInfo.name);
            var4 = new Intent(this.mIntent);
            var4.setComponent(var26);
            if (this.mActivityChoserModelPolicy != null) {
               Intent var5 = new Intent(var4);
               if (this.mActivityChoserModelPolicy.onChooseActivity(this, var5)) {
                  return null;
               }
            }
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label208;
         }

         label198:
         try {
            this.addHisoricalRecord(new ActivityChooserModel.HistoricalRecord(var26, System.currentTimeMillis(), 1.0F));
            return var4;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label198;
         }
      }

      while(true) {
         Throwable var27 = var10000;

         try {
            throw var27;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public ResolveInfo getActivity(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int getActivityCount() {
      // $FF: Couldn't be decompiled
   }

   public int getActivityIndex(ResolveInfo var1) {
      Object var4 = this.mInstanceLock;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label231: {
         int var3;
         List var5;
         try {
            this.ensureConsistentState();
            var5 = this.mActivities;
            var3 = var5.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label231;
         }

         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               try {
                  return -1;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }
            }

            try {
               if (((ActivityChooserModel.ActivityResolveInfo)var5.get(var2)).resolveInfo == var1) {
                  return var2;
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public ResolveInfo getDefaultActivity() {
      Object var1 = this.mInstanceLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
               ResolveInfo var15 = ((ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(0)).resolveInfo;
               return var15;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            return null;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public int getHistoryMaxSize() {
      // $FF: Couldn't be decompiled
   }

   public int getHistorySize() {
      // $FF: Couldn't be decompiled
   }

   public Intent getIntent() {
      // $FF: Couldn't be decompiled
   }

   public void setActivitySorter(ActivityChooserModel.ActivitySorter var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label197: {
         try {
            if (this.mActivitySorter == var1) {
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label197;
         }

         try {
            this.mActivitySorter = var1;
            if (this.sortActivitiesIfNeeded()) {
               this.notifyChanged();
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label197;
         }

         label187:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label187;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public void setDefaultActivity(int var1) {
      Object var3 = this.mInstanceLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label181: {
         ActivityChooserModel.ActivityResolveInfo var4;
         ActivityChooserModel.ActivityResolveInfo var5;
         try {
            this.ensureConsistentState();
            var4 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(var1);
            var5 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(0);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label181;
         }

         float var2;
         if (var5 != null) {
            try {
               var2 = var5.weight - var4.weight + 5.0F;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label181;
            }
         } else {
            var2 = 1.0F;
         }

         label170:
         try {
            this.addHisoricalRecord(new ActivityChooserModel.HistoricalRecord(new ComponentName(var4.resolveInfo.activityInfo.packageName, var4.resolveInfo.activityInfo.name), System.currentTimeMillis(), var2));
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label170;
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public void setHistoryMaxSize(int var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label197: {
         try {
            if (this.mHistoryMaxSize == var1) {
               return;
            }
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label197;
         }

         try {
            this.mHistoryMaxSize = var1;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
               this.notifyChanged();
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label197;
         }

         label187:
         try {
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label187;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public void setIntent(Intent var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (this.mIntent == var1) {
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.mIntent = var1;
            this.mReloadActivities = true;
            this.ensureConsistentState();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void setOnChooseActivityListener(ActivityChooserModel.OnChooseActivityListener param1) {
      // $FF: Couldn't be decompiled
   }

   public interface ActivityChooserModelClient {
      void setActivityChooserModel(ActivityChooserModel var1);
   }

   public final class ActivityResolveInfo implements Comparable {
      public final ResolveInfo resolveInfo;
      public float weight;

      public ActivityResolveInfo(ResolveInfo var2) {
         this.resolveInfo = var2;
      }

      public int compareTo(ActivityChooserModel.ActivityResolveInfo var1) {
         return Float.floatToIntBits(var1.weight) - Float.floatToIntBits(this.weight);
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 == null) {
               return false;
            }

            if (this.getClass() != var1.getClass()) {
               return false;
            }

            ActivityChooserModel.ActivityResolveInfo var2 = (ActivityChooserModel.ActivityResolveInfo)var1;
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(var2.weight)) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         return Float.floatToIntBits(this.weight) + 31;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("resolveInfo:").append(this.resolveInfo.toString());
         var1.append("; weight:").append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface ActivitySorter {
      void sort(Intent var1, List var2, List var3);
   }

   private final class DefaultSorter implements ActivityChooserModel.ActivitySorter {
      private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;
      private final Map mPackageNameToActivityMap;

      private DefaultSorter() {
         this.mPackageNameToActivityMap = new HashMap();
      }

      // $FF: synthetic method
      DefaultSorter(Object var2) {
         this();
      }

      public void sort(Intent var1, List var2, List var3) {
         Map var10 = this.mPackageNameToActivityMap;
         var10.clear();
         int var7 = var2.size();

         int var6;
         for(var6 = 0; var6 < var7; ++var6) {
            ActivityChooserModel.ActivityResolveInfo var8 = (ActivityChooserModel.ActivityResolveInfo)var2.get(var6);
            var8.weight = 0.0F;
            var10.put(var8.resolveInfo.activityInfo.packageName, var8);
         }

         var6 = var3.size();
         float var4 = 1.0F;
         --var6;

         while(var6 >= 0) {
            ActivityChooserModel.HistoricalRecord var11 = (ActivityChooserModel.HistoricalRecord)var3.get(var6);
            ActivityChooserModel.ActivityResolveInfo var9 = (ActivityChooserModel.ActivityResolveInfo)var10.get(var11.activity.getPackageName());
            float var5 = var4;
            if (var9 != null) {
               var9.weight += var11.weight * var4;
               var5 = var4 * 0.95F;
            }

            --var6;
            var4 = var5;
         }

         Collections.sort(var2);
      }
   }

   public static final class HistoricalRecord {
      public final ComponentName activity;
      public final long time;
      public final float weight;

      public HistoricalRecord(ComponentName var1, long var2, float var4) {
         this.activity = var1;
         this.time = var2;
         this.weight = var4;
      }

      public HistoricalRecord(String var1, long var2, float var4) {
         this(ComponentName.unflattenFromString(var1), var2, var4);
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 == null) {
               return false;
            }

            if (this.getClass() != var1.getClass()) {
               return false;
            }

            ActivityChooserModel.HistoricalRecord var2 = (ActivityChooserModel.HistoricalRecord)var1;
            if (this.activity == null) {
               if (var2.activity != null) {
                  return false;
               }
            } else if (!this.activity.equals(var2.activity)) {
               return false;
            }

            if (this.time != var2.time) {
               return false;
            }

            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(var2.weight)) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         int var1;
         if (this.activity == null) {
            var1 = 0;
         } else {
            var1 = this.activity.hashCode();
         }

         return ((var1 + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("; activity:").append(this.activity);
         var1.append("; time:").append(this.time);
         var1.append("; weight:").append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface OnChooseActivityListener {
      boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
   }

   private final class PersistHistoryAsyncTask extends AsyncTask {
      private PersistHistoryAsyncTask() {
      }

      // $FF: synthetic method
      PersistHistoryAsyncTask(Object var2) {
         this();
      }

      public Void doInBackground(Object... param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
