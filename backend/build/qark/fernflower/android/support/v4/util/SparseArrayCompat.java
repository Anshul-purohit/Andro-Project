package android.support.v4.util;

public class SparseArrayCompat implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private int[] mKeys;
   private int mSize;
   private Object[] mValues;

   public SparseArrayCompat() {
      this(10);
   }

   public SparseArrayCompat(int var1) {
      this.mGarbage = false;
      if (var1 == 0) {
         this.mKeys = ContainerHelpers.EMPTY_INTS;
         this.mValues = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = ContainerHelpers.idealIntArraySize(var1);
         this.mKeys = new int[var1];
         this.mValues = new Object[var1];
      }

      this.mSize = 0;
   }

   // $FF: renamed from: gc () void
   private void method_0() {
      int var4 = this.mSize;
      int var2 = 0;
      int[] var5 = this.mKeys;
      Object[] var6 = this.mValues;

      int var3;
      for(int var1 = 0; var1 < var4; var2 = var3) {
         Object var7 = var6[var1];
         var3 = var2;
         if (var7 != DELETED) {
            if (var1 != var2) {
               var5[var2] = var5[var1];
               var6[var2] = var7;
               var6[var1] = null;
            }

            var3 = var2 + 1;
         }

         ++var1;
      }

      this.mGarbage = false;
      this.mSize = var2;
   }

   public void append(int var1, Object var2) {
      if (this.mSize != 0 && var1 <= this.mKeys[this.mSize - 1]) {
         this.put(var1, var2);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.method_0();
         }

         int var3 = this.mSize;
         if (var3 >= this.mKeys.length) {
            int var4 = ContainerHelpers.idealIntArraySize(var3 + 1);
            int[] var5 = new int[var4];
            Object[] var6 = new Object[var4];
            System.arraycopy(this.mKeys, 0, var5, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
            this.mKeys = var5;
            this.mValues = var6;
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         this.mSize = var3 + 1;
      }
   }

   public void clear() {
      int var2 = this.mSize;
      Object[] var3 = this.mValues;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = null;
      }

      this.mSize = 0;
      this.mGarbage = false;
   }

   public SparseArrayCompat clone() {
      SparseArrayCompat var1 = null;

      boolean var10001;
      SparseArrayCompat var2;
      try {
         var2 = (SparseArrayCompat)super.clone();
      } catch (CloneNotSupportedException var5) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mKeys = (int[])this.mKeys.clone();
      } catch (CloneNotSupportedException var4) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mValues = (Object[])this.mValues.clone();
         return var2;
      } catch (CloneNotSupportedException var3) {
         var10001 = false;
         return var1;
      }
   }

   public void delete(int var1) {
      var1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var1 >= 0 && this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public Object get(int var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(int var1, Object var2) {
      var1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      return var1 >= 0 && this.mValues[var1] != DELETED ? this.mValues[var1] : var2;
   }

   public int indexOfKey(int var1) {
      if (this.mGarbage) {
         this.method_0();
      }

      return ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
   }

   public int indexOfValue(Object var1) {
      if (this.mGarbage) {
         this.method_0();
      }

      for(int var2 = 0; var2 < this.mSize; ++var2) {
         if (this.mValues[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public int keyAt(int var1) {
      if (this.mGarbage) {
         this.method_0();
      }

      return this.mKeys[var1];
   }

   public void put(int var1, Object var2) {
      int var3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var3 >= 0) {
         this.mValues[var3] = var2;
      } else {
         int var4 = ~var3;
         if (var4 < this.mSize && this.mValues[var4] == DELETED) {
            this.mKeys[var4] = var1;
            this.mValues[var4] = var2;
         } else {
            var3 = var4;
            if (this.mGarbage) {
               var3 = var4;
               if (this.mSize >= this.mKeys.length) {
                  this.method_0();
                  var3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
               }
            }

            if (this.mSize >= this.mKeys.length) {
               var4 = ContainerHelpers.idealIntArraySize(this.mSize + 1);
               int[] var5 = new int[var4];
               Object[] var6 = new Object[var4];
               System.arraycopy(this.mKeys, 0, var5, 0, this.mKeys.length);
               System.arraycopy(this.mValues, 0, var6, 0, this.mValues.length);
               this.mKeys = var5;
               this.mValues = var6;
            }

            if (this.mSize - var3 != 0) {
               System.arraycopy(this.mKeys, var3, this.mKeys, var3 + 1, this.mSize - var3);
               System.arraycopy(this.mValues, var3, this.mValues, var3 + 1, this.mSize - var3);
            }

            this.mKeys[var3] = var1;
            this.mValues[var3] = var2;
            ++this.mSize;
         }
      }
   }

   public void remove(int var1) {
      this.delete(var1);
   }

   public void removeAt(int var1) {
      if (this.mValues[var1] != DELETED) {
         this.mValues[var1] = DELETED;
         this.mGarbage = true;
      }

   }

   public void removeAtRange(int var1, int var2) {
      for(var2 = Math.min(this.mSize, var1 + var2); var1 < var2; ++var1) {
         this.removeAt(var1);
      }

   }

   public void setValueAt(int var1, Object var2) {
      if (this.mGarbage) {
         this.method_0();
      }

      this.mValues[var1] = var2;
   }

   public int size() {
      if (this.mGarbage) {
         this.method_0();
      }

      return this.mSize;
   }

   public String toString() {
      if (this.size() <= 0) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 28);
         var2.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            var2.append(this.keyAt(var1));
            var2.append('=');
            Object var3 = this.valueAt(var1);
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
      if (this.mGarbage) {
         this.method_0();
      }

      return this.mValues[var1];
   }
}
