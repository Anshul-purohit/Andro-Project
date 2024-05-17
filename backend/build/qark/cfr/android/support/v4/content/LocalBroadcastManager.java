/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.HashMap
 */
package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap();

    static {
        mLock = new Object();
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()){

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        super.handleMessage(message);
                        return;
                    }
                    case 1: 
                }
                LocalBroadcastManager.this.executePendingBroadcasts();
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void executePendingBroadcasts() {
        block3 : do {
            int n;
            Object[] arrobject;
            Object object = this.mReceivers;
            synchronized (object) {
                n = this.mPendingBroadcasts.size();
                if (n <= 0) {
                    return;
                }
                arrobject = new BroadcastRecord[n];
                this.mPendingBroadcasts.toArray(arrobject);
                this.mPendingBroadcasts.clear();
            }
            n = 0;
            do {
                if (n >= arrobject.length) continue block3;
                object = arrobject[n];
                for (int i = 0; i < object.receivers.size(); ++i) {
                    ((ReceiverRecord)object.receivers.get((int)i)).receiver.onReceive(this.mAppContext, object.intent);
                }
                ++n;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static LocalBroadcastManager getInstance(Context object) {
        Object object2 = mLock;
        synchronized (object2) {
            if (mInstance != null) return mInstance;
            mInstance = new LocalBroadcastManager(object.getApplicationContext());
            return mInstance;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<IntentFilter>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
            Object object = (ArrayList)this.mReceivers.get((Object)broadcastReceiver);
            ArrayList arrayList = object;
            if (object == null) {
                arrayList = new ArrayList(1);
                this.mReceivers.put((Object)broadcastReceiver, (Object)arrayList);
            }
            arrayList.add((Object)intentFilter);
            int n = 0;
            while (n < intentFilter.countActions()) {
                object = intentFilter.getAction(n);
                arrayList = (ArrayList)this.mActions.get(object);
                broadcastReceiver = arrayList;
                if (arrayList == null) {
                    broadcastReceiver = new ArrayList(1);
                    this.mActions.put(object, (Object)broadcastReceiver);
                }
                broadcastReceiver.add((Object)receiverRecord);
                ++n;
            }
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public boolean sendBroadcast(Intent var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public void sendBroadcastSync(Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        HashMap<BroadcastReceiver, ArrayList<IntentFilter>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ArrayList arrayList = (ArrayList)this.mReceivers.remove((Object)broadcastReceiver);
            if (arrayList == null) {
                return;
            }
            int n = 0;
            block3 : while (n < arrayList.size()) {
                IntentFilter intentFilter = (IntentFilter)arrayList.get(n);
                int n2 = 0;
                do {
                    block11 : {
                        String string2;
                        ArrayList arrayList2;
                        block12 : {
                            block10 : {
                                if (n2 >= intentFilter.countActions()) break block10;
                                string2 = intentFilter.getAction(n2);
                                arrayList2 = (ArrayList)this.mActions.get((Object)string2);
                                if (arrayList2 == null) break block11;
                                break block12;
                            }
                            ++n;
                            continue block3;
                        }
                        int n3 = 0;
                        do {
                            int n4;
                            if (n3 < arrayList2.size()) {
                                n4 = n3;
                                if (((ReceiverRecord)arrayList2.get((int)n3)).receiver == broadcastReceiver) {
                                    arrayList2.remove(n3);
                                    n4 = n3 - 1;
                                }
                            } else {
                                if (arrayList2.size() > 0) break;
                                this.mActions.remove((Object)string2);
                                break;
                            }
                            n3 = n4 + 1;
                        } while (true);
                    }
                    ++n2;
                } while (true);
                break;
            }
            return;
        }
    }

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            this.intent = intent;
            this.receivers = arrayList;
        }
    }

    private static class ReceiverRecord {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            this.filter = intentFilter;
            this.receiver = broadcastReceiver;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append("Receiver{");
            stringBuilder.append((Object)this.receiver);
            stringBuilder.append(" filter=");
            stringBuilder.append((Object)this.filter);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

