package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

public class ConnectivityManagerCompat {
   private static final ConnectivityManagerCompat.ConnectivityManagerCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new ConnectivityManagerCompat.JellyBeanConnectivityManagerCompatImpl();
      } else if (VERSION.SDK_INT >= 13) {
         IMPL = new ConnectivityManagerCompat.HoneycombMR2ConnectivityManagerCompatImpl();
      } else if (VERSION.SDK_INT >= 8) {
         IMPL = new ConnectivityManagerCompat.GingerbreadConnectivityManagerCompatImpl();
      } else {
         IMPL = new ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl();
      }
   }

   public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager var0, Intent var1) {
      return var0.getNetworkInfo(((NetworkInfo)var1.getParcelableExtra("networkInfo")).getType());
   }

   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      return IMPL.isActiveNetworkMetered(var0);
   }

   static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         NetworkInfo var2 = var1.getActiveNetworkInfo();
         if (var2 != null) {
            switch(var2.getType()) {
            case 0:
               break;
            case 1:
               return false;
            default:
               return true;
            }
         }

         return true;
      }
   }

   interface ConnectivityManagerCompatImpl {
      boolean isActiveNetworkMetered(ConnectivityManager var1);
   }

   static class GingerbreadConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(var1);
      }
   }

   static class HoneycombMR2ConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(var1);
      }
   }

   static class JellyBeanConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(var1);
      }
   }
}
