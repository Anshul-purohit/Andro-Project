package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

class NavUtils$NavUtilsImplJB
  extends NavUtils.NavUtilsImplBase
{
  public Intent getParentActivityIntent(Activity paramActivity)
  {
    Intent localIntent1 = NavUtilsJB.getParentActivityIntent(paramActivity);
    Intent localIntent2 = localIntent1;
    if (localIntent1 == null) {
      localIntent2 = superGetParentActivityIntent(paramActivity);
    }
    return localIntent2;
  }
  
  public String getParentActivityName(Context paramContext, ActivityInfo paramActivityInfo)
  {
    String str1 = NavUtilsJB.getParentActivityName(paramActivityInfo);
    String str2 = str1;
    if (str1 == null) {
      str2 = super.getParentActivityName(paramContext, paramActivityInfo);
    }
    return str2;
  }
  
  public void navigateUpTo(Activity paramActivity, Intent paramIntent)
  {
    NavUtilsJB.navigateUpTo(paramActivity, paramIntent);
  }
  
  public boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent)
  {
    return NavUtilsJB.shouldUpRecreateTask(paramActivity, paramIntent);
  }
  
  Intent superGetParentActivityIntent(Activity paramActivity)
  {
    return super.getParentActivityIntent(paramActivity);
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.NavUtils.NavUtilsImplJB
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */