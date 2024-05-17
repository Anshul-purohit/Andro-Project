package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager
{
  private static final boolean DEBUG = false;
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  private static final String TAG = "LocalBroadcastManager";
  private static LocalBroadcastManager mInstance;
  private static final Object mLock = new Object();
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
  private final Context mAppContext;
  private final Handler mHandler;
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
  private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap();
  
  private LocalBroadcastManager(Context paramContext)
  {
    mAppContext = paramContext;
    mHandler = new Handler(paramContext.getMainLooper())
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        switch (what)
        {
        default: 
          super.handleMessage(paramAnonymousMessage);
        }
        for (;;)
        {
          return;
          LocalBroadcastManager.this.executePendingBroadcasts();
        }
      }
    };
  }
  
  private void executePendingBroadcasts()
  {
    for (;;)
    {
      int i;
      synchronized (mReceivers)
      {
        i = mPendingBroadcasts.size();
        if (i <= 0) {
          return;
        }
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        mPendingBroadcasts.clear();
        i = 0;
        if (i >= arrayOfBroadcastRecord.length) {
          continue;
        }
        ??? = arrayOfBroadcastRecord[i];
        int j = 0;
        if (j < receivers.size())
        {
          receivers.get(j)).receiver.onReceive(mAppContext, intent);
          j++;
        }
      }
      i++;
    }
  }
  
  public static LocalBroadcastManager getInstance(Context paramContext)
  {
    synchronized (mLock)
    {
      if (mInstance == null)
      {
        LocalBroadcastManager localLocalBroadcastManager = new android/support/v4/content/LocalBroadcastManager;
        localLocalBroadcastManager.<init>(paramContext.getApplicationContext());
        mInstance = localLocalBroadcastManager;
      }
      paramContext = mInstance;
      return paramContext;
    }
  }
  
  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    synchronized (mReceivers)
    {
      ReceiverRecord localReceiverRecord = new android/support/v4/content/LocalBroadcastManager$ReceiverRecord;
      localReceiverRecord.<init>(paramIntentFilter, paramBroadcastReceiver);
      Object localObject1 = (ArrayList)mReceivers.get(paramBroadcastReceiver);
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = new java/util/ArrayList;
        ((ArrayList)localObject2).<init>(1);
        mReceivers.put(paramBroadcastReceiver, localObject2);
      }
      ((ArrayList)localObject2).add(paramIntentFilter);
      for (int i = 0; i < paramIntentFilter.countActions(); i++)
      {
        localObject1 = paramIntentFilter.getAction(i);
        localObject2 = (ArrayList)mActions.get(localObject1);
        paramBroadcastReceiver = (BroadcastReceiver)localObject2;
        if (localObject2 == null)
        {
          paramBroadcastReceiver = new java/util/ArrayList;
          paramBroadcastReceiver.<init>(1);
          mActions.put(localObject1, paramBroadcastReceiver);
        }
        paramBroadcastReceiver.add(localReceiverRecord);
      }
      return;
    }
  }
  
  public boolean sendBroadcast(Intent paramIntent)
  {
    Object localObject1;
    int i;
    Object localObject2;
    Object localObject3;
    for (;;)
    {
      Object localObject4;
      int k;
      synchronized (mReceivers)
      {
        String str1 = paramIntent.getAction();
        String str2 = paramIntent.resolveTypeIfNeeded(mAppContext.getContentResolver());
        localObject1 = paramIntent.getData();
        String str3 = paramIntent.getScheme();
        Set localSet = paramIntent.getCategories();
        if ((paramIntent.getFlags() & 0x8) != 0)
        {
          i = 1;
          if (i != 0)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            Log.v("LocalBroadcastManager", "Resolving type " + str2 + " scheme " + str3 + " of intent " + paramIntent);
          }
          ArrayList localArrayList = (ArrayList)mActions.get(paramIntent.getAction());
          if (localArrayList == null) {
            break label591;
          }
          if (i != 0)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            Log.v("LocalBroadcastManager", "Action list: " + localArrayList);
          }
          localObject3 = null;
          int j = 0;
          if (j >= localArrayList.size()) {
            break;
          }
          localObject4 = (ReceiverRecord)localArrayList.get(j);
          if (i != 0)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            Log.v("LocalBroadcastManager", "Matching against filter " + filter);
          }
          if (broadcasting)
          {
            localObject2 = localObject3;
            if (i != 0)
            {
              Log.v("LocalBroadcastManager", "  Filter's target already added");
              localObject2 = localObject3;
            }
            j++;
            localObject3 = localObject2;
            continue;
          }
        }
        else
        {
          i = 0;
          continue;
        }
        k = filter.match(str1, str2, str3, (Uri)localObject1, localSet, "LocalBroadcastManager");
        if (k >= 0)
        {
          if (i != 0)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(k));
          }
          localObject2 = localObject3;
          if (localObject3 == null)
          {
            localObject2 = new java/util/ArrayList;
            ((ArrayList)localObject2).<init>();
          }
          ((ArrayList)localObject2).add(localObject4);
          broadcasting = true;
        }
      }
      localObject2 = localObject3;
      if (i != 0)
      {
        switch (k)
        {
        default: 
          localObject2 = "unknown reason";
        }
        for (;;)
        {
          localObject4 = new java/lang/StringBuilder;
          ((StringBuilder)localObject4).<init>();
          Log.v("LocalBroadcastManager", "  Filter did not match: " + (String)localObject2);
          localObject2 = localObject3;
          break;
          localObject2 = "action";
          continue;
          localObject2 = "category";
          continue;
          localObject2 = "data";
          continue;
          localObject2 = "type";
        }
      }
    }
    boolean bool;
    if (localObject3 != null)
    {
      for (i = 0; i < ((ArrayList)localObject3).size(); i++) {
        getbroadcasting = false;
      }
      localObject2 = mPendingBroadcasts;
      localObject1 = new android/support/v4/content/LocalBroadcastManager$BroadcastRecord;
      ((BroadcastRecord)localObject1).<init>(paramIntent, (ArrayList)localObject3);
      ((ArrayList)localObject2).add(localObject1);
      if (!mHandler.hasMessages(1)) {
        mHandler.sendEmptyMessage(1);
      }
      bool = true;
    }
    for (;;)
    {
      return bool;
      label591:
      bool = false;
    }
  }
  
  public void sendBroadcastSync(Intent paramIntent)
  {
    if (sendBroadcast(paramIntent)) {
      executePendingBroadcasts();
    }
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    synchronized (mReceivers)
    {
      ArrayList localArrayList1 = (ArrayList)mReceivers.remove(paramBroadcastReceiver);
      if (localArrayList1 == null) {
        return;
      }
      for (int i = 0; i < localArrayList1.size(); i++)
      {
        IntentFilter localIntentFilter = (IntentFilter)localArrayList1.get(i);
        for (int j = 0; j < localIntentFilter.countActions(); j++)
        {
          String str = localIntentFilter.getAction(j);
          ArrayList localArrayList2 = (ArrayList)mActions.get(str);
          if (localArrayList2 != null)
          {
            int m;
            for (int k = 0; k < localArrayList2.size(); k = m + 1)
            {
              m = k;
              if (getreceiver == paramBroadcastReceiver)
              {
                localArrayList2.remove(k);
                m = k - 1;
              }
            }
            if (localArrayList2.size() <= 0) {
              mActions.remove(str);
            }
          }
        }
      }
    }
  }
  
  private static class BroadcastRecord
  {
    final Intent intent;
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;
    
    BroadcastRecord(Intent paramIntent, ArrayList<LocalBroadcastManager.ReceiverRecord> paramArrayList)
    {
      intent = paramIntent;
      receivers = paramArrayList;
    }
  }
  
  private static class ReceiverRecord
  {
    boolean broadcasting;
    final IntentFilter filter;
    final BroadcastReceiver receiver;
    
    ReceiverRecord(IntentFilter paramIntentFilter, BroadcastReceiver paramBroadcastReceiver)
    {
      filter = paramIntentFilter;
      receiver = paramBroadcastReceiver;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Receiver{");
      localStringBuilder.append(receiver);
      localStringBuilder.append(" filter=");
      localStringBuilder.append(filter);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.content.LocalBroadcastManager
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */