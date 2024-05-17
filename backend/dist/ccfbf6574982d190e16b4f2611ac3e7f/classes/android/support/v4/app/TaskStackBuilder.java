package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskStackBuilder
  implements Iterable<Intent>
{
  private static final TaskStackBuilderImpl IMPL;
  private static final String TAG = "TaskStackBuilder";
  private final ArrayList<Intent> mIntents = new ArrayList();
  private final Context mSourceContext;
  
  static
  {
    if (Build.VERSION.SDK_INT >= 11) {}
    for (IMPL = new TaskStackBuilderImplHoneycomb();; IMPL = new TaskStackBuilderImplBase()) {
      return;
    }
  }
  
  private TaskStackBuilder(Context paramContext)
  {
    mSourceContext = paramContext;
  }
  
  public static TaskStackBuilder create(Context paramContext)
  {
    return new TaskStackBuilder(paramContext);
  }
  
  public static TaskStackBuilder from(Context paramContext)
  {
    return create(paramContext);
  }
  
  public TaskStackBuilder addNextIntent(Intent paramIntent)
  {
    mIntents.add(paramIntent);
    return this;
  }
  
  public TaskStackBuilder addParentStack(Activity paramActivity)
  {
    int i = mIntents.size();
    Intent localIntent = NavUtils.getParentActivityIntent(paramActivity);
    while (localIntent != null)
    {
      mIntents.add(i, localIntent);
      try
      {
        localIntent = NavUtils.getParentActivityIntent(paramActivity, localIntent.getComponent());
      }
      catch (PackageManager.NameNotFoundException paramActivity)
      {
        Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
        throw new IllegalArgumentException(paramActivity);
      }
    }
    return this;
  }
  
  public TaskStackBuilder addParentStack(Class<?> paramClass)
  {
    int i = mIntents.size();
    try
    {
      for (paramClass = NavUtils.getParentActivityIntent(mSourceContext, paramClass); paramClass != null; paramClass = NavUtils.getParentActivityIntent(mSourceContext, paramClass.getComponent())) {
        mIntents.add(i, paramClass);
      }
      return this;
    }
    catch (PackageManager.NameNotFoundException paramClass)
    {
      Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
      throw new IllegalArgumentException(paramClass);
    }
  }
  
  public Intent editIntentAt(int paramInt)
  {
    return (Intent)mIntents.get(paramInt);
  }
  
  public Intent getIntent(int paramInt)
  {
    return editIntentAt(paramInt);
  }
  
  public int getIntentCount()
  {
    return mIntents.size();
  }
  
  public Intent[] getIntents()
  {
    return (Intent[])mIntents.toArray(new Intent[mIntents.size()]);
  }
  
  public PendingIntent getPendingIntent(int paramInt1, int paramInt2)
  {
    return getPendingIntent(paramInt1, paramInt2, null);
  }
  
  public PendingIntent getPendingIntent(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if (mIntents.isEmpty()) {
      throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    }
    Intent[] arrayOfIntent = (Intent[])mIntents.toArray(new Intent[mIntents.size()]);
    arrayOfIntent[0].addFlags(268484608);
    return IMPL.getPendingIntent(mSourceContext, arrayOfIntent, paramInt1, paramInt2, paramBundle);
  }
  
  public Iterator<Intent> iterator()
  {
    return mIntents.iterator();
  }
  
  public void startActivities()
  {
    startActivities(null);
  }
  
  public void startActivities(Bundle paramBundle)
  {
    if (mIntents.isEmpty()) {
      throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }
    Intent[] arrayOfIntent = (Intent[])mIntents.toArray(new Intent[mIntents.size()]);
    arrayOfIntent[0].addFlags(268484608);
    if (!ContextCompat.startActivities(mSourceContext, arrayOfIntent, paramBundle))
    {
      paramBundle = arrayOfIntent[(arrayOfIntent.length - 1)];
      paramBundle.addFlags(268435456);
      mSourceContext.startActivity(paramBundle);
    }
  }
  
  static abstract interface TaskStackBuilderImpl
  {
    public abstract PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle);
  }
  
  static class TaskStackBuilderImplBase
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      paramArrayOfIntent = paramArrayOfIntent[(paramArrayOfIntent.length - 1)];
      paramArrayOfIntent.addFlags(268435456);
      return PendingIntent.getActivity(paramContext, paramInt1, paramArrayOfIntent, paramInt2);
    }
  }
  
  static class TaskStackBuilderImplHoneycomb
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      paramArrayOfIntent[0].addFlags(268468224);
      return TaskStackBuilderHoneycomb.getActivitiesPendingIntent(paramContext, paramInt1, paramArrayOfIntent, paramInt2);
    }
  }
  
  static class TaskStackBuilderImplJellybean
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      paramArrayOfIntent[0].addFlags(268468224);
      return TaskStackBuilderJellybean.getActivitiesPendingIntent(paramContext, paramInt1, paramArrayOfIntent, paramInt2, paramBundle);
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.TaskStackBuilder
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */