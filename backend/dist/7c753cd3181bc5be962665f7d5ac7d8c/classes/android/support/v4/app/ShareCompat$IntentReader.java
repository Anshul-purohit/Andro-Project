package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import java.util.ArrayList;

public class ShareCompat$IntentReader
{
  private static final String TAG = "IntentReader";
  private Activity mActivity;
  private ComponentName mCallingActivity;
  private String mCallingPackage;
  private Intent mIntent;
  private ArrayList<Uri> mStreams;
  
  private ShareCompat$IntentReader(Activity paramActivity)
  {
    mActivity = paramActivity;
    mIntent = paramActivity.getIntent();
    mCallingPackage = ShareCompat.getCallingPackage(paramActivity);
    mCallingActivity = ShareCompat.getCallingActivity(paramActivity);
  }
  
  public static IntentReader from(Activity paramActivity)
  {
    return new IntentReader(paramActivity);
  }
  
  public ComponentName getCallingActivity()
  {
    return mCallingActivity;
  }
  
  public Drawable getCallingActivityIcon()
  {
    Object localObject1 = null;
    if (mCallingActivity == null) {}
    for (;;)
    {
      return (Drawable)localObject1;
      Object localObject2 = mActivity.getPackageManager();
      try
      {
        localObject2 = ((PackageManager)localObject2).getActivityIcon(mCallingActivity);
        localObject1 = localObject2;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("IntentReader", "Could not retrieve icon for calling activity", localNameNotFoundException);
      }
    }
  }
  
  public Drawable getCallingApplicationIcon()
  {
    Object localObject1 = null;
    if (mCallingPackage == null) {}
    for (;;)
    {
      return (Drawable)localObject1;
      Object localObject2 = mActivity.getPackageManager();
      try
      {
        localObject2 = ((PackageManager)localObject2).getApplicationIcon(mCallingPackage);
        localObject1 = localObject2;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("IntentReader", "Could not retrieve icon for calling application", localNameNotFoundException);
      }
    }
  }
  
  public CharSequence getCallingApplicationLabel()
  {
    Object localObject1 = null;
    if (mCallingPackage == null) {}
    for (;;)
    {
      return (CharSequence)localObject1;
      Object localObject2 = mActivity.getPackageManager();
      try
      {
        localObject2 = ((PackageManager)localObject2).getApplicationLabel(((PackageManager)localObject2).getApplicationInfo(mCallingPackage, 0));
        localObject1 = localObject2;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("IntentReader", "Could not retrieve label for calling application", localNameNotFoundException);
      }
    }
  }
  
  public String getCallingPackage()
  {
    return mCallingPackage;
  }
  
  public String[] getEmailBcc()
  {
    return mIntent.getStringArrayExtra("android.intent.extra.BCC");
  }
  
  public String[] getEmailCc()
  {
    return mIntent.getStringArrayExtra("android.intent.extra.CC");
  }
  
  public String[] getEmailTo()
  {
    return mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
  }
  
  public String getHtmlText()
  {
    String str1 = mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
    String str2 = str1;
    CharSequence localCharSequence;
    if (mIntent == null)
    {
      localCharSequence = getText();
      if (!(localCharSequence instanceof Spanned)) {
        break label41;
      }
      str2 = Html.toHtml((Spanned)localCharSequence);
    }
    for (;;)
    {
      return str2;
      label41:
      str2 = str1;
      if (localCharSequence != null) {
        str2 = ShareCompat.access$000().escapeHtml(localCharSequence);
      }
    }
  }
  
  public Uri getStream()
  {
    return (Uri)mIntent.getParcelableExtra("android.intent.extra.STREAM");
  }
  
  public Uri getStream(int paramInt)
  {
    if ((mStreams == null) && (isMultipleShare())) {
      mStreams = mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
    }
    if (mStreams != null) {}
    for (Uri localUri = (Uri)mStreams.get(paramInt);; localUri = (Uri)mIntent.getParcelableExtra("android.intent.extra.STREAM"))
    {
      return localUri;
      if (paramInt != 0) {
        break;
      }
    }
    throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + paramInt);
  }
  
  public int getStreamCount()
  {
    if ((mStreams == null) && (isMultipleShare())) {
      mStreams = mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
    }
    int i;
    if (mStreams != null) {
      i = mStreams.size();
    }
    for (;;)
    {
      return i;
      if (mIntent.hasExtra("android.intent.extra.STREAM")) {
        i = 1;
      } else {
        i = 0;
      }
    }
  }
  
  public String getSubject()
  {
    return mIntent.getStringExtra("android.intent.extra.SUBJECT");
  }
  
  public CharSequence getText()
  {
    return mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
  }
  
  public String getType()
  {
    return mIntent.getType();
  }
  
  public boolean isMultipleShare()
  {
    return mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
  }
  
  public boolean isShareIntent()
  {
    String str = mIntent.getAction();
    if ((str.equals("android.intent.action.SEND")) || (str.equals("android.intent.action.SEND_MULTIPLE"))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isSingleShare()
  {
    return mIntent.getAction().equals("android.intent.action.SEND");
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.ShareCompat.IntentReader
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */