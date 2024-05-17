package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.CursorFilterClient {
   @Deprecated
   public static final int FLAG_AUTO_REQUERY = 1;
   public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
   protected boolean mAutoRequery;
   protected CursorAdapter.ChangeObserver mChangeObserver;
   protected Context mContext;
   protected Cursor mCursor;
   protected CursorFilter mCursorFilter;
   protected DataSetObserver mDataSetObserver;
   protected boolean mDataValid;
   protected FilterQueryProvider mFilterQueryProvider;
   protected int mRowIDColumn;

   @Deprecated
   public CursorAdapter(Context var1, Cursor var2) {
      this.init(var1, var2, 1);
   }

   public CursorAdapter(Context var1, Cursor var2, int var3) {
      this.init(var1, var2, var3);
   }

   public CursorAdapter(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
   }

   public abstract void bindView(View var1, Context var2, Cursor var3);

   public void changeCursor(Cursor var1) {
      var1 = this.swapCursor(var1);
      if (var1 != null) {
         var1.close();
      }

   }

   public CharSequence convertToString(Cursor var1) {
      return var1 == null ? "" : var1.toString();
   }

   public int getCount() {
      return this.mDataValid && this.mCursor != null ? this.mCursor.getCount() : 0;
   }

   public Cursor getCursor() {
      return this.mCursor;
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         this.mCursor.moveToPosition(var1);
         if (var2 == null) {
            var2 = this.newDropDownView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var2, this.mContext, this.mCursor);
         return var2;
      } else {
         return null;
      }
   }

   public Filter getFilter() {
      if (this.mCursorFilter == null) {
         this.mCursorFilter = new CursorFilter(this);
      }

      return this.mCursorFilter;
   }

   public FilterQueryProvider getFilterQueryProvider() {
      return this.mFilterQueryProvider;
   }

   public Object getItem(int var1) {
      if (this.mDataValid && this.mCursor != null) {
         this.mCursor.moveToPosition(var1);
         return this.mCursor;
      } else {
         return null;
      }
   }

   public long getItemId(int var1) {
      long var4 = 0L;
      long var2 = var4;
      if (this.mDataValid) {
         var2 = var4;
         if (this.mCursor != null) {
            var2 = var4;
            if (this.mCursor.moveToPosition(var1)) {
               var2 = this.mCursor.getLong(this.mRowIDColumn);
            }
         }
      }

      return var2;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (!this.mDataValid) {
         throw new IllegalStateException("this should only be called when the cursor is valid");
      } else if (!this.mCursor.moveToPosition(var1)) {
         throw new IllegalStateException("couldn't move cursor to position " + var1);
      } else {
         if (var2 == null) {
            var2 = this.newView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var2, this.mContext, this.mCursor);
         return var2;
      }
   }

   public boolean hasStableIds() {
      return true;
   }

   void init(Context var1, Cursor var2, int var3) {
      boolean var5 = true;
      if ((var3 & 1) == 1) {
         var3 |= 2;
         this.mAutoRequery = true;
      } else {
         this.mAutoRequery = false;
      }

      if (var2 == null) {
         var5 = false;
      }

      this.mCursor = var2;
      this.mDataValid = var5;
      this.mContext = var1;
      int var4;
      if (var5) {
         var4 = var2.getColumnIndexOrThrow("_id");
      } else {
         var4 = -1;
      }

      this.mRowIDColumn = var4;
      if ((var3 & 2) == 2) {
         this.mChangeObserver = new CursorAdapter.ChangeObserver();
         this.mDataSetObserver = new CursorAdapter.MyDataSetObserver();
      } else {
         this.mChangeObserver = null;
         this.mDataSetObserver = null;
      }

      if (var5) {
         if (this.mChangeObserver != null) {
            var2.registerContentObserver(this.mChangeObserver);
         }

         if (this.mDataSetObserver != null) {
            var2.registerDataSetObserver(this.mDataSetObserver);
         }
      }

   }

   @Deprecated
   protected void init(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
   }

   public View newDropDownView(Context var1, Cursor var2, ViewGroup var3) {
      return this.newView(var1, var2, var3);
   }

   public abstract View newView(Context var1, Cursor var2, ViewGroup var3);

   protected void onContentChanged() {
      if (this.mAutoRequery && this.mCursor != null && !this.mCursor.isClosed()) {
         this.mDataValid = this.mCursor.requery();
      }

   }

   public Cursor runQueryOnBackgroundThread(CharSequence var1) {
      return this.mFilterQueryProvider != null ? this.mFilterQueryProvider.runQuery(var1) : this.mCursor;
   }

   public void setFilterQueryProvider(FilterQueryProvider var1) {
      this.mFilterQueryProvider = var1;
   }

   public Cursor swapCursor(Cursor var1) {
      if (var1 == this.mCursor) {
         return null;
      } else {
         Cursor var2 = this.mCursor;
         if (var2 != null) {
            if (this.mChangeObserver != null) {
               var2.unregisterContentObserver(this.mChangeObserver);
            }

            if (this.mDataSetObserver != null) {
               var2.unregisterDataSetObserver(this.mDataSetObserver);
            }
         }

         this.mCursor = var1;
         if (var1 != null) {
            if (this.mChangeObserver != null) {
               var1.registerContentObserver(this.mChangeObserver);
            }

            if (this.mDataSetObserver != null) {
               var1.registerDataSetObserver(this.mDataSetObserver);
            }

            this.mRowIDColumn = var1.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
            return var2;
         } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            this.notifyDataSetInvalidated();
            return var2;
         }
      }
   }

   private class ChangeObserver extends ContentObserver {
      public ChangeObserver() {
         super(new Handler());
      }

      public boolean deliverSelfNotifications() {
         return true;
      }

      public void onChange(boolean var1) {
         CursorAdapter.this.onContentChanged();
      }
   }

   private class MyDataSetObserver extends DataSetObserver {
      private MyDataSetObserver() {
      }

      // $FF: synthetic method
      MyDataSetObserver(Object var2) {
         this();
      }

      public void onChanged() {
         CursorAdapter.this.mDataValid = true;
         CursorAdapter.this.notifyDataSetChanged();
      }

      public void onInvalidated() {
         CursorAdapter.this.mDataValid = false;
         CursorAdapter.this.notifyDataSetInvalidated();
      }
   }
}
