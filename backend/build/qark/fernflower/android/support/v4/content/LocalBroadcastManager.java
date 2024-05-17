package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager {
   private static final boolean DEBUG = false;
   static final int MSG_EXEC_PENDING_BROADCASTS = 1;
   private static final String TAG = "LocalBroadcastManager";
   private static LocalBroadcastManager mInstance;
   private static final Object mLock = new Object();
   private final HashMap mActions = new HashMap();
   private final Context mAppContext;
   private final Handler mHandler;
   private final ArrayList mPendingBroadcasts = new ArrayList();
   private final HashMap mReceivers = new HashMap();

   private LocalBroadcastManager(Context var1) {
      this.mAppContext = var1;
      this.mHandler = new Handler(var1.getMainLooper()) {
         public void handleMessage(Message var1) {
            switch(var1.what) {
            case 1:
               LocalBroadcastManager.this.executePendingBroadcasts();
               return;
            default:
               super.handleMessage(var1);
            }
         }
      };
   }

   private void executePendingBroadcasts() {
      label263:
      while(true) {
         HashMap var3 = this.mReceivers;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label260: {
            int var1;
            try {
               var1 = this.mPendingBroadcasts.size();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label260;
            }

            if (var1 <= 0) {
               label253:
               try {
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label253;
               }
            } else {
               label265: {
                  LocalBroadcastManager.BroadcastRecord[] var26;
                  try {
                     var26 = new LocalBroadcastManager.BroadcastRecord[var1];
                     this.mPendingBroadcasts.toArray(var26);
                     this.mPendingBroadcasts.clear();
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label265;
                  }

                  var1 = 0;

                  while(true) {
                     if (var1 >= var26.length) {
                        continue label263;
                     }

                     LocalBroadcastManager.BroadcastRecord var25 = var26[var1];

                     for(int var2 = 0; var2 < var25.receivers.size(); ++var2) {
                        ((LocalBroadcastManager.ReceiverRecord)var25.receivers.get(var2)).receiver.onReceive(this.mAppContext, var25.intent);
                     }

                     ++var1;
                  }
               }
            }
         }

         while(true) {
            Throwable var4 = var10000;

            try {
               throw var4;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static LocalBroadcastManager getInstance(Context var0) {
      Object var1 = mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (mInstance == null) {
               mInstance = new LocalBroadcastManager(var0.getApplicationContext());
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            LocalBroadcastManager var15 = mInstance;
            return var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var14 = var10000;

         try {
            throw var14;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public void registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
      HashMap var6 = this.mReceivers;
      synchronized(var6){}

      Throwable var10000;
      boolean var10001;
      label585: {
         ArrayList var5;
         LocalBroadcastManager.ReceiverRecord var7;
         try {
            var7 = new LocalBroadcastManager.ReceiverRecord(var2, var1);
            var5 = (ArrayList)this.mReceivers.get(var1);
         } catch (Throwable var79) {
            var10000 = var79;
            var10001 = false;
            break label585;
         }

         ArrayList var4 = var5;
         if (var5 == null) {
            try {
               var4 = new ArrayList(1);
               this.mReceivers.put(var1, var4);
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label585;
            }
         }

         try {
            var4.add(var2);
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label585;
         }

         int var3 = 0;

         while(true) {
            String var82;
            try {
               if (var3 >= var2.countActions()) {
                  break;
               }

               var82 = var2.getAction(var3);
               var4 = (ArrayList)this.mActions.get(var82);
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break label585;
            }

            ArrayList var80 = var4;
            if (var4 == null) {
               try {
                  var80 = new ArrayList(1);
                  this.mActions.put(var82, var80);
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label585;
               }
            }

            try {
               var80.add(var7);
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label585;
            }

            ++var3;
         }

         label557:
         try {
            return;
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label557;
         }
      }

      while(true) {
         Throwable var81 = var10000;

         try {
            throw var81;
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean sendBroadcast(Intent var1) {
      HashMap var7 = this.mReceivers;
      synchronized(var7){}

      Throwable var10000;
      boolean var10001;
      label3114: {
         boolean var2;
         String var8;
         String var9;
         Uri var10;
         String var11;
         Set var12;
         label3109: {
            label3108: {
               try {
                  var8 = var1.getAction();
                  var9 = var1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                  var10 = var1.getData();
                  var11 = var1.getScheme();
                  var12 = var1.getCategories();
                  if ((var1.getFlags() & 8) != 0) {
                     break label3108;
                  }
               } catch (Throwable var356) {
                  var10000 = var356;
                  var10001 = false;
                  break label3114;
               }

               var2 = false;
               break label3109;
            }

            var2 = true;
         }

         if (var2) {
            try {
               Log.v("LocalBroadcastManager", "Resolving type " + var9 + " scheme " + var11 + " of intent " + var1);
            } catch (Throwable var355) {
               var10000 = var355;
               var10001 = false;
               break label3114;
            }
         }

         ArrayList var13;
         try {
            var13 = (ArrayList)this.mActions.get(var1.getAction());
         } catch (Throwable var354) {
            var10000 = var354;
            var10001 = false;
            break label3114;
         }

         if (var13 != null) {
            if (var2) {
               try {
                  Log.v("LocalBroadcastManager", "Action list: " + var13);
               } catch (Throwable var351) {
                  var10000 = var351;
                  var10001 = false;
                  break label3114;
               }
            }

            ArrayList var6 = null;
            int var3 = 0;

            while(true) {
               LocalBroadcastManager.ReceiverRecord var14;
               try {
                  if (var3 >= var13.size()) {
                     break;
                  }

                  var14 = (LocalBroadcastManager.ReceiverRecord)var13.get(var3);
               } catch (Throwable var352) {
                  var10000 = var352;
                  var10001 = false;
                  break label3114;
               }

               if (var2) {
                  try {
                     Log.v("LocalBroadcastManager", "Matching against filter " + var14.filter);
                  } catch (Throwable var350) {
                     var10000 = var350;
                     var10001 = false;
                     break label3114;
                  }
               }

               ArrayList var5;
               label3117: {
                  label3087: {
                     try {
                        if (!var14.broadcasting) {
                           break label3087;
                        }
                     } catch (Throwable var353) {
                        var10000 = var353;
                        var10001 = false;
                        break label3114;
                     }

                     var5 = var6;
                     if (var2) {
                        try {
                           Log.v("LocalBroadcastManager", "  Filter's target already added");
                        } catch (Throwable var349) {
                           var10000 = var349;
                           var10001 = false;
                           break label3114;
                        }

                        var5 = var6;
                     }
                     break label3117;
                  }

                  int var4;
                  try {
                     var4 = var14.filter.match(var8, var9, var11, var10, var12, "LocalBroadcastManager");
                  } catch (Throwable var348) {
                     var10000 = var348;
                     var10001 = false;
                     break label3114;
                  }

                  if (var4 >= 0) {
                     if (var2) {
                        try {
                           Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(var4));
                        } catch (Throwable var347) {
                           var10000 = var347;
                           var10001 = false;
                           break label3114;
                        }
                     }

                     var5 = var6;
                     if (var6 == null) {
                        try {
                           var5 = new ArrayList();
                        } catch (Throwable var346) {
                           var10000 = var346;
                           var10001 = false;
                           break label3114;
                        }
                     }

                     try {
                        var5.add(var14);
                        var14.broadcasting = true;
                     } catch (Throwable var345) {
                        var10000 = var345;
                        var10001 = false;
                        break label3114;
                     }
                  } else {
                     var5 = var6;
                     if (var2) {
                        String var359;
                        switch(var4) {
                        case -4:
                           var359 = "category";
                           break;
                        case -3:
                           var359 = "action";
                           break;
                        case -2:
                           var359 = "data";
                           break;
                        case -1:
                           var359 = "type";
                           break;
                        default:
                           var359 = "unknown reason";
                        }

                        try {
                           Log.v("LocalBroadcastManager", "  Filter did not match: " + var359);
                        } catch (Throwable var344) {
                           var10000 = var344;
                           var10001 = false;
                           break label3114;
                        }

                        var5 = var6;
                     }
                  }
               }

               ++var3;
               var6 = var5;
            }

            if (var6 != null) {
               int var358 = 0;

               while(true) {
                  try {
                     if (var358 >= var6.size()) {
                        break;
                     }

                     ((LocalBroadcastManager.ReceiverRecord)var6.get(var358)).broadcasting = false;
                  } catch (Throwable var342) {
                     var10000 = var342;
                     var10001 = false;
                     break label3114;
                  }

                  ++var358;
               }

               try {
                  this.mPendingBroadcasts.add(new LocalBroadcastManager.BroadcastRecord(var1, var6));
                  if (!this.mHandler.hasMessages(1)) {
                     this.mHandler.sendEmptyMessage(1);
                  }
               } catch (Throwable var341) {
                  var10000 = var341;
                  var10001 = false;
                  break label3114;
               }

               try {
                  return true;
               } catch (Throwable var340) {
                  var10000 = var340;
                  var10001 = false;
                  break label3114;
               }
            }
         }

         label3052:
         try {
            return false;
         } catch (Throwable var343) {
            var10000 = var343;
            var10001 = false;
            break label3052;
         }
      }

      while(true) {
         Throwable var357 = var10000;

         try {
            throw var357;
         } catch (Throwable var339) {
            var10000 = var339;
            var10001 = false;
            continue;
         }
      }
   }

   public void sendBroadcastSync(Intent var1) {
      if (this.sendBroadcast(var1)) {
         this.executePendingBroadcasts();
      }

   }

   public void unregisterReceiver(BroadcastReceiver var1) {
      HashMap var6 = this.mReceivers;
      synchronized(var6){}

      Throwable var10000;
      boolean var10001;
      label955: {
         ArrayList var7;
         try {
            var7 = (ArrayList)this.mReceivers.remove(var1);
         } catch (Throwable var100) {
            var10000 = var100;
            var10001 = false;
            break label955;
         }

         if (var7 == null) {
            label915:
            try {
               return;
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label915;
            }
         } else {
            label959: {
               int var3 = 0;

               while(true) {
                  IntentFilter var8;
                  try {
                     if (var3 >= var7.size()) {
                        break;
                     }

                     var8 = (IntentFilter)var7.get(var3);
                  } catch (Throwable var96) {
                     var10000 = var96;
                     var10001 = false;
                     break label959;
                  }

                  int var4 = 0;

                  while(true) {
                     String var9;
                     ArrayList var10;
                     try {
                        if (var4 >= var8.countActions()) {
                           break;
                        }

                        var9 = var8.getAction(var4);
                        var10 = (ArrayList)this.mActions.get(var9);
                     } catch (Throwable var97) {
                        var10000 = var97;
                        var10001 = false;
                        break label959;
                     }

                     if (var10 != null) {
                        int var2 = 0;

                        while(true) {
                           try {
                              if (var2 >= var10.size()) {
                                 break;
                              }
                           } catch (Throwable var98) {
                              var10000 = var98;
                              var10001 = false;
                              break label959;
                           }

                           int var5 = var2;

                           label937: {
                              try {
                                 if (((LocalBroadcastManager.ReceiverRecord)var10.get(var2)).receiver != var1) {
                                    break label937;
                                 }

                                 var10.remove(var2);
                              } catch (Throwable var99) {
                                 var10000 = var99;
                                 var10001 = false;
                                 break label959;
                              }

                              var5 = var2 - 1;
                           }

                           var2 = var5 + 1;
                        }

                        try {
                           if (var10.size() <= 0) {
                              this.mActions.remove(var9);
                           }
                        } catch (Throwable var95) {
                           var10000 = var95;
                           var10001 = false;
                           break label959;
                        }
                     }

                     ++var4;
                  }

                  ++var3;
               }

               label917:
               try {
                  return;
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label917;
               }
            }
         }
      }

      while(true) {
         Throwable var101 = var10000;

         try {
            throw var101;
         } catch (Throwable var92) {
            var10000 = var92;
            var10001 = false;
            continue;
         }
      }
   }

   private static class BroadcastRecord {
      final Intent intent;
      final ArrayList receivers;

      BroadcastRecord(Intent var1, ArrayList var2) {
         this.intent = var1;
         this.receivers = var2;
      }
   }

   private static class ReceiverRecord {
      boolean broadcasting;
      final IntentFilter filter;
      final BroadcastReceiver receiver;

      ReceiverRecord(IntentFilter var1, BroadcastReceiver var2) {
         this.filter = var1;
         this.receiver = var2;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(128);
         var1.append("Receiver{");
         var1.append(this.receiver);
         var1.append(" filter=");
         var1.append(this.filter);
         var1.append("}");
         return var1.toString();
      }
   }
}
