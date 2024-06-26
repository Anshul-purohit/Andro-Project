package android.support.v4.app;

import android.graphics.Bitmap;

public class NotificationCompat$BigPictureStyle
  extends NotificationCompat.Style
{
  Bitmap mPicture;
  
  public NotificationCompat$BigPictureStyle() {}
  
  public NotificationCompat$BigPictureStyle(NotificationCompat.Builder paramBuilder)
  {
    setBuilder(paramBuilder);
  }
  
  public BigPictureStyle bigPicture(Bitmap paramBitmap)
  {
    mPicture = paramBitmap;
    return this;
  }
  
  public BigPictureStyle setBigContentTitle(CharSequence paramCharSequence)
  {
    mBigContentTitle = paramCharSequence;
    return this;
  }
  
  public BigPictureStyle setSummaryText(CharSequence paramCharSequence)
  {
    mSummaryText = paramCharSequence;
    mSummaryTextSet = true;
    return this;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.NotificationCompat.BigPictureStyle
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */