package android.support.v4.util;

import java.util.Map;

public class SimpleArrayMap {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean DEBUG = false;
   private static final String TAG = "ArrayMap";
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   Object[] mArray;
   int[] mHashes;
   int mSize;

   public SimpleArrayMap() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public SimpleArrayMap(int var1) {
      if (var1 == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public SimpleArrayMap(SimpleArrayMap var1) {
      this();
      if (var1 != null) {
         this.putAll(var1);
      }

   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label756: {
         Object[] var2;
         label762: {
            if (var1 == 8) {
               label758: {
                  synchronized(ArrayMap.class){}

                  label759: {
                     label738: {
                        try {
                           if (mTwiceBaseCache != null) {
                              var2 = mTwiceBaseCache;
                              this.mArray = var2;
                              mTwiceBaseCache = (Object[])((Object[])var2[0]);
                              this.mHashes = (int[])((int[])var2[1]);
                              break label738;
                           }
                        } catch (Throwable var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label759;
                        }

                        try {
                           break label758;
                        } catch (Throwable var72) {
                           var10000 = var72;
                           var10001 = false;
                           break label759;
                        }
                     }

                     var2[1] = null;
                     var2[0] = null;

                     label719:
                     try {
                        --mTwiceBaseCacheSize;
                        return;
                     } catch (Throwable var69) {
                        var10000 = var69;
                        var10001 = false;
                        break label719;
                     }
                  }

                  while(true) {
                     var75 = var10000;

                     try {
                        throw var75;
                     } catch (Throwable var68) {
                        var10000 = var68;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            } else if (var1 == 4) {
               synchronized(ArrayMap.class){}

               try {
                  if (mBaseCache != null) {
                     var2 = mBaseCache;
                     this.mArray = var2;
                     mBaseCache = (Object[])((Object[])var2[0]);
                     this.mHashes = (int[])((int[])var2[1]);
                     break label762;
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label756;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label756;
               }
            }

            this.mHashes = new int[var1];
            this.mArray = new Object[var1 << 1];
            return;
         }

         var2[1] = null;
         var2[0] = null;

         label722:
         try {
            --mBaseCacheSize;
            return;
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label722;
         }
      }

      while(true) {
         var75 = var10000;

         try {
            throw var75;
         } catch (Throwable var67) {
            var10000 = var67;
            var10001 = false;
            continue;
         }
      }
   }

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(ArrayMap.class){}

         label801: {
            label828: {
               try {
                  if (mTwiceBaseCacheSize >= 10) {
                     break label828;
                  }

                  var1[0] = mTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label801;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mTwiceBaseCache = var1;
                        ++mTwiceBaseCacheSize;
                        break;
                     } catch (Throwable var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label801;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label788:
            try {
               return;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label788;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               continue;
            }
         }
      } else if (var0.length == 4) {
         synchronized(ArrayMap.class){}

         label820: {
            label830: {
               try {
                  if (mBaseCacheSize >= 10) {
                     break label830;
                  }

                  var1[0] = mBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label820;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mBaseCache = var1;
                        ++mBaseCacheSize;
                        break;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label820;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label807:
            try {
               return;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label807;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void clear() {
      if (this.mSize != 0) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      }

   }

   public boolean containsKey(Object var1) {
      if (var1 == null) {
         if (this.indexOfNull() < 0) {
            return false;
         }
      } else if (this.indexOf(var1, var1.hashCode()) < 0) {
         return false;
      }

      return true;
   }

   public boolean containsValue(Object var1) {
      return this.indexOfValue(var1) >= 0;
   }

   public void ensureCapacity(int var1) {
      if (this.mHashes.length < var1) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         this.allocArrays(var1);
         if (this.mSize > 0) {
            System.arraycopy(var2, 0, this.mHashes, 0, this.mSize);
            System.arraycopy(var3, 0, this.mArray, 0, this.mSize << 1);
         }

         freeArrays(var2, var3, this.mSize);
      }

   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof Map)) {
            return false;
         } else {
            Map var13 = (Map)var1;
            if (this.size() != var13.size()) {
               return false;
            } else {
               int var2 = 0;

               while(true) {
                  boolean var10001;
                  Object var4;
                  Object var5;
                  Object var6;
                  try {
                     if (var2 >= this.mSize) {
                        return true;
                     }

                     var4 = this.keyAt(var2);
                     var5 = this.valueAt(var2);
                     var6 = var13.get(var4);
                  } catch (NullPointerException var11) {
                     var10001 = false;
                     return false;
                  } catch (ClassCastException var12) {
                     var10001 = false;
                     return false;
                  }

                  if (var5 == null) {
                     if (var6 != null) {
                        break;
                     }

                     try {
                        if (!var13.containsKey(var4)) {
                           break;
                        }
                     } catch (NullPointerException var7) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var8) {
                        var10001 = false;
                        return false;
                     }
                  } else {
                     boolean var3;
                     try {
                        var3 = var5.equals(var6);
                     } catch (NullPointerException var9) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var10) {
                        var10001 = false;
                        return false;
                     }

                     if (!var3) {
                        return false;
                     }
                  }

                  ++var2;
               }

               return false;
            }
         }
      } else {
         return true;
      }
   }

   public Object get(Object var1) {
      int var2;
      if (var1 == null) {
         var2 = this.indexOfNull();
      } else {
         var2 = this.indexOf(var1, var1.hashCode());
      }

      return var2 >= 0 ? this.mArray[(var2 << 1) + 1] : null;
   }

   public int hashCode() {
      int[] var7 = this.mHashes;
      Object[] var8 = this.mArray;
      int var3 = 0;
      int var2 = 0;
      int var1 = 1;

      for(int var5 = this.mSize; var2 < var5; var1 += 2) {
         Object var9 = var8[var1];
         int var6 = var7[var2];
         int var4;
         if (var9 == null) {
            var4 = 0;
         } else {
            var4 = var9.hashCode();
         }

         var3 += var4 ^ var6;
         ++var2;
      }

      return var3;
   }

   int indexOf(Object var1, int var2) {
      int var5 = this.mSize;
      int var3;
      if (var5 == 0) {
         var3 = -1;
      } else {
         int var4 = ContainerHelpers.binarySearch(this.mHashes, var5, var2);
         var3 = var4;
         if (var4 >= 0) {
            var3 = var4;
            if (!var1.equals(this.mArray[var4 << 1])) {
               for(var3 = var4 + 1; var3 < var5 && this.mHashes[var3] == var2; ++var3) {
                  if (var1.equals(this.mArray[var3 << 1])) {
                     return var3;
                  }
               }

               --var4;

               while(var4 >= 0 && this.mHashes[var4] == var2) {
                  if (var1.equals(this.mArray[var4 << 1])) {
                     return var4;
                  }

                  --var4;
               }

               return ~var3;
            }
         }
      }

      return var3;
   }

   int indexOfNull() {
      int var3 = this.mSize;
      int var1;
      if (var3 == 0) {
         var1 = -1;
      } else {
         int var2 = ContainerHelpers.binarySearch(this.mHashes, var3, 0);
         var1 = var2;
         if (var2 >= 0) {
            var1 = var2;
            if (this.mArray[var2 << 1] != null) {
               for(var1 = var2 + 1; var1 < var3 && this.mHashes[var1] == 0; ++var1) {
                  if (this.mArray[var1 << 1] == null) {
                     return var1;
                  }
               }

               --var2;

               while(var2 >= 0 && this.mHashes[var2] == 0) {
                  if (this.mArray[var2 << 1] == null) {
                     return var2;
                  }

                  --var2;
               }

               return ~var1;
            }
         }
      }

      return var1;
   }

   int indexOfValue(Object var1) {
      int var3 = this.mSize * 2;
      Object[] var4 = this.mArray;
      int var2;
      if (var1 == null) {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if (var4[var2] == null) {
               return var2 >> 1;
            }
         }
      } else {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if (var1.equals(var4[var2])) {
               return var2 >> 1;
            }
         }
      }

      return -1;
   }

   public boolean isEmpty() {
      return this.mSize <= 0;
   }

   public Object keyAt(int var1) {
      return this.mArray[var1 << 1];
   }

   public Object put(Object var1, Object var2) {
      byte var5 = 8;
      int var3;
      int var4;
      if (var1 == null) {
         var4 = 0;
         var3 = this.indexOfNull();
      } else {
         var4 = var1.hashCode();
         var3 = this.indexOf(var1, var4);
      }

      if (var3 >= 0) {
         var3 = (var3 << 1) + 1;
         var1 = this.mArray[var3];
         this.mArray[var3] = var2;
         return var1;
      } else {
         int var6 = ~var3;
         if (this.mSize >= this.mHashes.length) {
            if (this.mSize >= 8) {
               var3 = this.mSize + (this.mSize >> 1);
            } else {
               var3 = var5;
               if (this.mSize < 4) {
                  var3 = 4;
               }
            }

            int[] var7 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var3);
            if (this.mHashes.length > 0) {
               System.arraycopy(var7, 0, this.mHashes, 0, var7.length);
               System.arraycopy(var8, 0, this.mArray, 0, var8.length);
            }

            freeArrays(var7, var8, this.mSize);
         }

         if (var6 < this.mSize) {
            System.arraycopy(this.mHashes, var6, this.mHashes, var6 + 1, this.mSize - var6);
            System.arraycopy(this.mArray, var6 << 1, this.mArray, var6 + 1 << 1, this.mSize - var6 << 1);
         }

         this.mHashes[var6] = var4;
         this.mArray[var6 << 1] = var1;
         this.mArray[(var6 << 1) + 1] = var2;
         ++this.mSize;
         return null;
      }
   }

   public void putAll(SimpleArrayMap var1) {
      int var3 = var1.mSize;
      this.ensureCapacity(this.mSize + var3);
      if (this.mSize == 0) {
         if (var3 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var3);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var3 << 1);
            this.mSize = var3;
         }
      } else {
         for(int var2 = 0; var2 < var3; ++var2) {
            this.put(var1.keyAt(var2), var1.valueAt(var2));
         }
      }

   }

   public Object remove(Object var1) {
      int var2;
      if (var1 == null) {
         var2 = this.indexOfNull();
      } else {
         var2 = this.indexOf(var1, var1.hashCode());
      }

      return var2 >= 0 ? this.removeAt(var2) : null;
   }

   public Object removeAt(int var1) {
      int var2 = 8;
      Object var3 = this.mArray[(var1 << 1) + 1];
      if (this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      } else {
         if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
            --this.mSize;
            if (var1 < this.mSize) {
               System.arraycopy(this.mHashes, var1 + 1, this.mHashes, var1, this.mSize - var1);
               System.arraycopy(this.mArray, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
            }

            this.mArray[this.mSize << 1] = null;
            this.mArray[(this.mSize << 1) + 1] = null;
            return var3;
         }

         if (this.mSize > 8) {
            var2 = this.mSize + (this.mSize >> 1);
         }

         int[] var4 = this.mHashes;
         Object[] var5 = this.mArray;
         this.allocArrays(var2);
         --this.mSize;
         if (var1 > 0) {
            System.arraycopy(var4, 0, this.mHashes, 0, var1);
            System.arraycopy(var5, 0, this.mArray, 0, var1 << 1);
         }

         if (var1 < this.mSize) {
            System.arraycopy(var4, var1 + 1, this.mHashes, var1, this.mSize - var1);
            System.arraycopy(var5, var1 + 1 << 1, this.mArray, var1 << 1, this.mSize - var1 << 1);
            return var3;
         }
      }

      return var3;
   }

   public Object setValueAt(int var1, Object var2) {
      var1 = (var1 << 1) + 1;
      Object var3 = this.mArray[var1];
      this.mArray[var1] = var2;
      return var3;
   }

   public int size() {
      return this.mSize;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 28);
         var2.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            Object var3 = this.keyAt(var1);
            if (var3 != this) {
               var2.append(var3);
            } else {
               var2.append("(this Map)");
            }

            var2.append('=');
            var3 = this.valueAt(var1);
            if (var3 != this) {
               var2.append(var3);
            } else {
               var2.append("(this Map)");
            }
         }

         var2.append('}');
         return var2.toString();
      }
   }

   public Object valueAt(int var1) {
      return this.mArray[(var1 << 1) + 1];
   }
}
