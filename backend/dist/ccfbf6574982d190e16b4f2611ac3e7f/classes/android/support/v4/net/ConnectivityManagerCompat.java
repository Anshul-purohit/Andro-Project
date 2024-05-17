package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

public class ConnectivityManagerCompat
{
  private static final ConnectivityManagerCompatImpl IMPL;
  
  static
  {
    if (Build.VERSION.SDK_INT >= 16) {
      IMPL = new JellyBeanConnectivityManagerCompatImpl();
    }
    for (;;)
    {
      return;
      if (Build.VERSION.SDK_INT >= 13) {
        IMPL = new HoneycombMR2ConnectivityManagerCompatImpl();
      } else if (Build.VERSION.SDK_INT >= 8) {
        IMPL = new GingerbreadConnectivityManagerCompatImpl();
      } else {
        IMPL = new BaseConnectivityManagerCompatImpl();
      }
    }
  }
  
  public boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
  {
    return IMPL.isActiveNetworkMetered(paramConnectivityManager);
  }
  
  static class BaseConnectivityManagerCompatImpl
    implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl
  {
    public boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
    {
      boolean bool1 = true;
      paramConnectivityManager = paramConnectivityManager.getActiveNetworkInfo();
      boolean bool2;
      if (paramConnectivityManager == null) {
        bool2 = bool1;
      }
      for (;;)
      {
        return bool2;
        bool2 = bool1;
        switch (paramConnectivityManager.getType())
        {
        case 0: 
        default: 
          bool2 = bool1;
          break;
        case 1: 
          bool2 = false;
        }
      }
    }
  }
  
  static abstract interface ConnectivityManagerCompatImpl
  {
    public abstract boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager);
  }
  
  static class GingerbreadConnectivityManagerCompatImpl
    implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl
  {
    public boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
    {
      return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(paramConnectivityManager);
    }
  }
  
  static class HoneycombMR2ConnectivityManagerCompatImpl
    implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl
  {
    public boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
    {
      return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(paramConnectivityManager);
    }
  }
  
  static class JellyBeanConnectivityManagerCompatImpl
    implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl
  {
    public boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
    {
      return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(paramConnectivityManager);
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.net.ConnectivityManagerCompat
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */