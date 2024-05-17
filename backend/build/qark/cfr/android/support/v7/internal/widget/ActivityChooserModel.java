/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.database.DataSetObservable
 *  android.os.AsyncTask
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.Xml
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Comparable
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.Void
 *  java.math.BigDecimal
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.Executor
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 *  org.xmlpull.v1.XmlSerializer
 */
package android.support.v7.internal.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ActivityChooserModel
extends DataSetObservable {
    private static final String ATTRIBUTE_ACTIVITY = "activity";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_WEIGHT = "weight";
    private static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    private static final String TAG_HISTORICAL_RECORD = "historical-record";
    private static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities = new ArrayList();
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    private boolean mCanReadHistoricalData;
    private final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords = new ArrayList();
    private boolean mHistoricalRecordsChanged;
    private final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;

    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap();
    }

    private ActivityChooserModel(Context context, String string2) {
        this.mActivitySorter = new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)string2) && !string2.endsWith(".xml")) {
            this.mHistoryFileName = string2 + ".xml";
            return;
        }
        this.mHistoryFileName = string2;
    }

    private boolean addHisoricalRecord(HistoricalRecord historicalRecord) {
        boolean bl = this.mHistoricalRecords.add((Object)historicalRecord);
        if (bl) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return bl;
    }

    private void ensureConsistentState() {
        boolean bl = this.loadActivitiesIfNeeded();
        boolean bl2 = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (bl | bl2) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }

    private void executePersistHistoryAsyncTaskBase() {
        new PersistHistoryAsyncTask().execute(new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
    }

    private void executePersistHistoryAsyncTaskSDK11() {
        new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ActivityChooserModel get(Context context, String string2) {
        Object object = sRegistryLock;
        synchronized (object) {
            ActivityChooserModel activityChooserModel;
            ActivityChooserModel activityChooserModel2 = activityChooserModel = (ActivityChooserModel)((Object)sDataModelRegistry.get((Object)string2));
            if (activityChooserModel == null) {
                activityChooserModel2 = new ActivityChooserModel(context, string2);
                sDataModelRegistry.put((Object)string2, (Object)activityChooserModel2);
            }
            return activityChooserModel2;
        }
    }

    private boolean loadActivitiesIfNeeded() {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.mReloadActivities) {
            bl2 = bl;
            if (this.mIntent != null) {
                this.mReloadActivities = false;
                this.mActivities.clear();
                List list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
                int n = list.size();
                for (int i = 0; i < n; ++i) {
                    ResolveInfo resolveInfo = (ResolveInfo)list.get(i);
                    this.mActivities.add((Object)new ActivityResolveInfo(resolveInfo));
                }
                bl2 = true;
            }
        }
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void persistHistoricalDataIfNeeded() {
        block6 : {
            block5 : {
                if (!this.mReadShareHistoryCalled) {
                    throw new IllegalStateException("No preceding call to #readHistoricalData");
                }
                if (!this.mHistoricalRecordsChanged) break block5;
                this.mHistoricalRecordsChanged = false;
                if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) break block6;
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 11) {
            this.executePersistHistoryAsyncTaskSDK11();
            return;
        }
        this.executePersistHistoryAsyncTaskBase();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        int n2 = 0;
        while (n2 < n) {
            HistoricalRecord historicalRecord = (HistoricalRecord)this.mHistoricalRecords.remove(0);
            ++n2;
        }
    }

    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void readHistoricalDataImpl() {
        var2_1 = this.mContext.openFileInput(this.mHistoryFileName);
        try {
            var3_7 = Xml.newPullParser();
            var3_7.setInput((InputStream)var2_1, null);
            var1_11 = 0;
            while (var1_11 != 1 && var1_11 != 2) {
                var1_11 = var3_7.next();
            }
            if (!"historical-records".equals((Object)var3_7.getName())) {
                throw new XmlPullParserException("Share records file does not start with historical-records tag.");
            }
            var4_12 = this.mHistoricalRecords;
            var4_12.clear();
            ** GOTO lbl27
            catch (FileNotFoundException var2_6) {
                // empty catch block
            }
            return;
            {
                catch (XmlPullParserException var3_8) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error reading historical recrod file: " + this.mHistoryFileName), (Throwable)var3_8);
                    if (var2_1 == null) return;
                    try {
                        var2_1.close();
                        return;
                    }
                    catch (IOException var2_2) {
                        return;
                    }
                }
lbl27: // 1 sources:
                do {
                    if ((var1_11 = var3_7.next()) == 1) {
                        if (var2_1 == null) return;
                        try {
                            var2_1.close();
                            return;
                        }
                        catch (IOException var2_3) {
                            return;
                        }
                    }
                    if (var1_11 == 3 || var1_11 == 4) continue;
                    ** try [egrp 6[TRYBLOCK] [21 : 160->217)] { 
lbl38: // 2 sources:
                    if ("historical-record".equals((Object)var3_7.getName())) ** break block23
                    throw new XmlPullParserException("Share records file not well-formed.");
                    break;
                } while (true);
                catch (IOException var3_9) {
                    Log.e((String)ActivityChooserModel.LOG_TAG, (String)("Error reading historical recrod file: " + this.mHistoryFileName), (Throwable)var3_9);
                    if (var2_1 == null) return;
                    try {
                        var2_1.close();
                        return;
                    }
                    catch (IOException var2_4) {
                        return;
                    }
                }
                {
                    
                    var4_12.add((Object)new HistoricalRecord(var3_7.getAttributeValue(null, "activity"), Long.parseLong((String)var3_7.getAttributeValue(null, "time")), Float.parseFloat((String)var3_7.getAttributeValue(null, "weight"))));
                    continue;
                }
            }
        }
lbl52: // 3 sources:
        catch (Throwable var3_10) {
            if (var2_1 == null) throw var3_10;
            try {
                var2_1.close();
            }
            catch (IOException var2_5) {
                throw var3_10;
            }
            throw var3_10;
        }
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent chooseActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            Intent intent;
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = (ActivityResolveInfo)this.mActivities.get(n);
            activityResolveInfo = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            Intent intent2 = new Intent(this.mIntent);
            intent2.setComponent((ComponentName)activityResolveInfo);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, intent = new Intent(intent2))) {
                return null;
            }
            this.addHisoricalRecord(new HistoricalRecord((ComponentName)activityResolveInfo, System.currentTimeMillis(), 1.0f));
            return intent2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return ((ActivityResolveInfo)this.mActivities.get((int)n)).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityCount() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityIndex(ResolveInfo resolveInfo) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            List<ActivityResolveInfo> list = this.mActivities;
            int n = list.size();
            int n2 = 0;
            while (n2 < n) {
                if (((ActivityResolveInfo)list.get((int)n2)).resolveInfo == resolveInfo) {
                    return n2;
                }
                ++n2;
            }
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getDefaultActivity() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            if (this.mActivities.isEmpty()) return null;
            return ((ActivityResolveInfo)this.mActivities.get((int)0)).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistoryMaxSize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mHistoryMaxSize;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistorySize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent getIntent() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mIntent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setActivitySorter(ActivitySorter activitySorter) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mActivitySorter == activitySorter) {
                return;
            }
            this.mActivitySorter = activitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDefaultActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = (ActivityResolveInfo)this.mActivities.get(n);
            ActivityResolveInfo activityResolveInfo2 = (ActivityResolveInfo)this.mActivities.get(0);
            float f = activityResolveInfo2 != null ? activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f : 1.0f;
            this.addHisoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), f));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setHistoryMaxSize(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mHistoryMaxSize == n) {
                return;
            }
            this.mHistoryMaxSize = n;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setIntent(Intent intent) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnChooseActivityListener(OnChooseActivityListener onChooseActivityListener) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.mActivityChoserModelPolicy = onChooseActivityListener;
            return;
        }
    }

    public static interface ActivityChooserModelClient {
        public void setActivityChooserModel(ActivityChooserModel var1);
    }

    public final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        public int compareTo(ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits((float)activityResolveInfo.weight) - Float.floatToIntBits((float)this.weight);
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block6 : {
                block5 : {
                    if (this == object) break block5;
                    if (object == null) {
                        return false;
                    }
                    if (this.getClass() != object.getClass()) {
                        return false;
                    }
                    object = (ActivityResolveInfo)object;
                    if (Float.floatToIntBits((float)this.weight) != Float.floatToIntBits((float)object.weight)) break block6;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Float.floatToIntBits((float)this.weight) + 31;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("resolveInfo:").append(this.resolveInfo.toString());
            stringBuilder.append("; weight:").append((Object)new BigDecimal((double)this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface ActivitySorter {
        public void sort(Intent var1, List<ActivityResolveInfo> var2, List<HistoricalRecord> var3);
    }

    private final class DefaultSorter
    implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<String, ActivityResolveInfo> mPackageNameToActivityMap;

        private DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap();
        }

        @Override
        public void sort(Intent map, List<ActivityResolveInfo> list, List<HistoricalRecord> list2) {
            Object object;
            int n;
            map = this.mPackageNameToActivityMap;
            map.clear();
            int n2 = list.size();
            for (n = 0; n < n2; ++n) {
                object = (ActivityResolveInfo)list.get(n);
                object.weight = 0.0f;
                map.put((Object)object.resolveInfo.activityInfo.packageName, object);
            }
            n = list2.size();
            float f = 1.0f;
            --n;
            while (n >= 0) {
                object = (HistoricalRecord)list2.get(n);
                ActivityResolveInfo activityResolveInfo = (ActivityResolveInfo)map.get((Object)object.activity.getPackageName());
                float f2 = f;
                if (activityResolveInfo != null) {
                    activityResolveInfo.weight += object.weight * f;
                    f2 = f * 0.95f;
                }
                --n;
                f = f2;
            }
            Collections.sort(list);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(ComponentName componentName, long l, float f) {
            this.activity = componentName;
            this.time = l;
            this.weight = f;
        }

        public HistoricalRecord(String string2, long l, float f) {
            this(ComponentName.unflattenFromString((String)string2), l, f);
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block8 : {
                block7 : {
                    if (this == object) break block7;
                    if (object == null) {
                        return false;
                    }
                    if (this.getClass() != object.getClass()) {
                        return false;
                    }
                    object = (HistoricalRecord)object;
                    if (this.activity == null ? object.activity != null : !this.activity.equals((Object)object.activity)) {
                        return false;
                    }
                    if (this.time != object.time) {
                        return false;
                    }
                    if (Float.floatToIntBits((float)this.weight) != Float.floatToIntBits((float)object.weight)) break block8;
                }
                return true;
            }
            return false;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public int hashCode() {
            int n;
            if (this.activity == null) {
                n = 0;
                do {
                    return ((n + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits((float)this.weight);
                    break;
                } while (true);
            }
            n = this.activity.hashCode();
            return ((n + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits((float)this.weight);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("; activity:").append((Object)this.activity);
            stringBuilder.append("; time:").append(this.time);
            stringBuilder.append("; weight:").append((Object)new BigDecimal((double)this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface OnChooseActivityListener {
        public boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
    }

    private final class PersistHistoryAsyncTask
    extends AsyncTask<Object, Void, Void> {
        private PersistHistoryAsyncTask() {
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public /* varargs */ Void doInBackground(Object ... fileOutputStream) {
            List list = (List)fileOutputStream[0];
            String string2 = (String)fileOutputStream[1];
            fileOutputStream = ActivityChooserModel.this.mContext.openFileOutput(string2, 0);
            string2 = Xml.newSerializer();
            try {
                block23 : {
                    string2.setOutput((OutputStream)fileOutputStream, null);
                    string2.startDocument("UTF-8", Boolean.valueOf((boolean)true));
                    string2.startTag(null, "historical-records");
                    int n = list.size();
                    for (int i = 0; i < n; ++i) {
                        HistoricalRecord historicalRecord = (HistoricalRecord)list.remove(0);
                        string2.startTag(null, "historical-record");
                        string2.attribute(null, "activity", historicalRecord.activity.flattenToString());
                        string2.attribute(null, "time", String.valueOf((long)historicalRecord.time));
                        string2.attribute(null, "weight", String.valueOf((float)historicalRecord.weight));
                        string2.endTag(null, "historical-record");
                    }
                    break block23;
                    catch (FileNotFoundException fileNotFoundException) {
                        Log.e((String)LOG_TAG, (String)("Error writing historical recrod file: " + string2), (Throwable)fileNotFoundException);
                        return null;
                    }
                }
                string2.endTag(null, "historical-records");
                string2.endDocument();
                return null;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e((String)LOG_TAG, (String)("Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)illegalArgumentException);
                return null;
            }
            catch (IllegalStateException illegalStateException) {
                Throwable throwable222;
                Log.e((String)LOG_TAG, (String)("Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)illegalStateException);
                return null;
                {
                    catch (Throwable throwable222) {}
                }
                catch (IOException iOException) {
                    Log.e((String)LOG_TAG, (String)("Error writing historical recrod file: " + ActivityChooserModel.this.mHistoryFileName), (Throwable)iOException);
                    return null;
                    throw throwable222;
                }
            }
            finally {
                ActivityChooserModel.this.mCanReadHistoricalData = true;
                if (fileOutputStream == null) return null;
                fileOutputStream.close();
                return null;
            }
        }
    }

}

