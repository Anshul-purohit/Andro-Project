package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompat$BaseConnectivityManagerCompatImpl
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

/* Location:
 * Qualified Name:     android.support.v4.net.ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */