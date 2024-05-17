package android.support.v4.app;

public class NotificationCompat$BigTextStyle
  extends NotificationCompat.Style
{
  CharSequence mBigText;
  
  public NotificationCompat$BigTextStyle() {}
  
  public NotificationCompat$BigTextStyle(NotificationCompat.Builder paramBuilder)
  {
    setBuilder(paramBuilder);
  }
  
  public BigTextStyle bigText(CharSequence paramCharSequence)
  {
    mBigText = paramCharSequence;
    return this;
  }
  
  public BigTextStyle setBigContentTitle(CharSequence paramCharSequence)
  {
    mBigContentTitle = paramCharSequence;
    return this;
  }
  
  public BigTextStyle setSummaryText(CharSequence paramCharSequence)
  {
    mSummaryText = paramCharSequence;
    mSummaryTextSet = true;
    return this;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.NotificationCompat.BigTextStyle
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */