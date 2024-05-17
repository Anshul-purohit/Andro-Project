package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

class NavUtils$NavUtilsImplBase
  implements NavUtils.NavUtilsImpl
{
  public Intent getParentActivityIntent(Activity paramActivity)
  {
    String str = NavUtils.getParentActivityName(paramActivity);
    if (str == null) {}
    for (paramActivity = null;; paramActivity = new Intent().setClassName(paramActivity, str)) {
      return paramActivity;
    }
  }
  
  public String getParentActivityName(Context paramContext, ActivityInfo paramActivityInfo)
  {
    if (metaData == null) {
      paramActivityInfo = null;
    }
    for (;;)
    {
      return paramActivityInfo;
      String str = metaData.getString("android.support.PARENT_ACTIVITY");
      if (str == null)
      {
        paramActivityInfo = null;
      }
      else
      {
        paramActivityInfo = str;
        if (str.charAt(0) == '.') {
          paramActivityInfo = paramContext.getPackageName() + str;
        }
      }
    }
  }
  
  public void navigateUpTo(Activity paramActivity, Intent paramIntent)
  {
    paramIntent.addFlags(67108864);
    paramActivity.startActivity(paramIntent);
    paramActivity.finish();
  }
  
  public boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent)
  {
    paramActivity = paramActivity.getIntent().getAction();
    if ((paramActivity != null) && (!paramActivity.equals("android.intent.action.MAIN"))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.NavUtils.NavUtilsImplBase
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */