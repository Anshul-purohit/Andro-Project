package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Adapter;
import android.widget.AdapterView;

abstract class AdapterViewICS extends ViewGroup {
   public static final int INVALID_POSITION = -1;
   public static final long INVALID_ROW_ID = Long.MIN_VALUE;
   static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
   static final int ITEM_VIEW_TYPE_IGNORE = -1;
   static final int SYNC_FIRST_POSITION = 1;
   static final int SYNC_MAX_DURATION_MILLIS = 100;
   static final int SYNC_SELECTED_POSITION = 0;
   boolean mBlockLayoutRequests = false;
   boolean mDataChanged;
   private boolean mDesiredFocusableInTouchModeState;
   private boolean mDesiredFocusableState;
   private View mEmptyView;
   @ExportedProperty(
      category = "scrolling"
   )
   int mFirstPosition = 0;
   boolean mInLayout = false;
   @ExportedProperty(
      category = "list"
   )
   int mItemCount;
   private int mLayoutHeight;
   boolean mNeedSync = false;
   @ExportedProperty(
      category = "list"
   )
   int mNextSelectedPosition = -1;
   long mNextSelectedRowId = Long.MIN_VALUE;
   int mOldItemCount;
   int mOldSelectedPosition = -1;
   long mOldSelectedRowId = Long.MIN_VALUE;
   AdapterViewICS.OnItemClickListener mOnItemClickListener;
   AdapterViewICS.OnItemLongClickListener mOnItemLongClickListener;
   AdapterViewICS.OnItemSelectedListener mOnItemSelectedListener;
   @ExportedProperty(
      category = "list"
   )
   int mSelectedPosition = -1;
   long mSelectedRowId = Long.MIN_VALUE;
   private AdapterViewICS.SelectionNotifier mSelectionNotifier;
   int mSpecificTop;
   long mSyncHeight;
   int mSyncMode;
   int mSyncPosition;
   long mSyncRowId = Long.MIN_VALUE;

   AdapterViewICS(Context var1) {
      super(var1);
   }

   AdapterViewICS(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   AdapterViewICS(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   private void fireOnSelected() {
      if (this.mOnItemSelectedListener != null) {
         int var1 = this.getSelectedItemPosition();
         if (var1 >= 0) {
            View var2 = this.getSelectedView();
            this.mOnItemSelectedListener.onItemSelected(this, var2, var1, this.getAdapter().getItemId(var1));
         } else {
            this.mOnItemSelectedListener.onNothingSelected(this);
         }
      }
   }

   private void updateEmptyStatus(boolean var1) {
      if (this.isInFilterMode()) {
         var1 = false;
      }

      if (var1) {
         if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(0);
            this.setVisibility(8);
         } else {
            this.setVisibility(0);
         }

         if (this.mDataChanged) {
            this.onLayout(false, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
         }

      } else {
         if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(8);
         }

         this.setVisibility(0);
      }
   }

   public void addView(View var1) {
      throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
   }

   public void addView(View var1, int var2) {
      throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
   }

   public void addView(View var1, int var2, LayoutParams var3) {
      throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
   }

   public void addView(View var1, LayoutParams var2) {
      throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
   }

   protected boolean canAnimate() {
      return super.canAnimate() && this.mItemCount > 0;
   }

   void checkFocus() {
      boolean var3 = false;
      Adapter var4 = this.getAdapter();
      boolean var1;
      if (var4 != null && var4.getCount() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      if (var1 && !this.isInFilterMode()) {
         var1 = false;
      } else {
         var1 = true;
      }

      boolean var2;
      if (var1 && this.mDesiredFocusableInTouchModeState) {
         var2 = true;
      } else {
         var2 = false;
      }

      super.setFocusableInTouchMode(var2);
      if (var1 && this.mDesiredFocusableState) {
         var2 = true;
      } else {
         var2 = false;
      }

      super.setFocusable(var2);
      if (this.mEmptyView != null) {
         label29: {
            if (var4 != null) {
               var2 = var3;
               if (!var4.isEmpty()) {
                  break label29;
               }
            }

            var2 = true;
         }

         this.updateEmptyStatus(var2);
      }

   }

   void checkSelectionChanged() {
      if (this.mSelectedPosition != this.mOldSelectedPosition || this.mSelectedRowId != this.mOldSelectedRowId) {
         this.selectionChanged();
         this.mOldSelectedPosition = this.mSelectedPosition;
         this.mOldSelectedRowId = this.mSelectedRowId;
      }

   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      View var2 = this.getSelectedView();
      return var2 != null && var2.getVisibility() == 0 && var2.dispatchPopulateAccessibilityEvent(var1);
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.dispatchThawSelfOnly(var1);
   }

   protected void dispatchSaveInstanceState(SparseArray var1) {
      this.dispatchFreezeSelfOnly(var1);
   }

   int findSyncPosition() {
      int var7 = this.mItemCount;
      int var5;
      if (var7 == 0) {
         var5 = -1;
         return var5;
      } else {
         long var8 = this.mSyncRowId;
         int var1 = this.mSyncPosition;
         if (var8 == Long.MIN_VALUE) {
            return -1;
         } else {
            var1 = Math.min(var7 - 1, Math.max(0, var1));
            long var10 = SystemClock.uptimeMillis();
            int var3 = var1;
            int var4 = var1;
            boolean var2 = false;
            Adapter var12 = this.getAdapter();
            if (var12 == null) {
               return -1;
            } else {
               while(SystemClock.uptimeMillis() <= var10 + 100L) {
                  var5 = var1;
                  if (var12.getItemId(var1) == var8) {
                     return var5;
                  }

                  boolean var13;
                  if (var4 == var7 - 1) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  boolean var6;
                  if (var3 == 0) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  if (var13 && var6) {
                     break;
                  }

                  if (!var6 && (!var2 || var13)) {
                     if (var13 || !var2 && !var6) {
                        --var3;
                        var1 = var3;
                        var2 = true;
                     }
                  } else {
                     ++var4;
                     var1 = var4;
                     var2 = false;
                  }
               }

               return -1;
            }
         }
      }
   }

   public abstract Adapter getAdapter();

   @CapturedViewProperty
   public int getCount() {
      return this.mItemCount;
   }

   public View getEmptyView() {
      return this.mEmptyView;
   }

   public int getFirstVisiblePosition() {
      return this.mFirstPosition;
   }

   public Object getItemAtPosition(int var1) {
      Adapter var2 = this.getAdapter();
      return var2 != null && var1 >= 0 ? var2.getItem(var1) : null;
   }

   public long getItemIdAtPosition(int var1) {
      Adapter var2 = this.getAdapter();
      return var2 != null && var1 >= 0 ? var2.getItemId(var1) : Long.MIN_VALUE;
   }

   public int getLastVisiblePosition() {
      return this.mFirstPosition + this.getChildCount() - 1;
   }

   public final AdapterViewICS.OnItemClickListener getOnItemClickListener() {
      return this.mOnItemClickListener;
   }

   public final AdapterViewICS.OnItemLongClickListener getOnItemLongClickListener() {
      return this.mOnItemLongClickListener;
   }

   public final AdapterViewICS.OnItemSelectedListener getOnItemSelectedListener() {
      return this.mOnItemSelectedListener;
   }

   public int getPositionForView(View var1) {
      while(true) {
         boolean var4;
         View var5;
         try {
            var5 = (View)var1.getParent();
            var4 = var5.equals(this);
         } catch (ClassCastException var6) {
            return -1;
         }

         if (var4) {
            int var3 = this.getChildCount();

            for(int var2 = 0; var2 < var3; ++var2) {
               if (this.getChildAt(var2).equals(var1)) {
                  return this.mFirstPosition + var2;
               }
            }

            return -1;
         }

         var1 = var5;
      }
   }

   public Object getSelectedItem() {
      Adapter var2 = this.getAdapter();
      int var1 = this.getSelectedItemPosition();
      return var2 != null && var2.getCount() > 0 && var1 >= 0 ? var2.getItem(var1) : null;
   }

   @CapturedViewProperty
   public long getSelectedItemId() {
      return this.mNextSelectedRowId;
   }

   @CapturedViewProperty
   public int getSelectedItemPosition() {
      return this.mNextSelectedPosition;
   }

   public abstract View getSelectedView();

   void handleDataChanged() {
      int var4 = this.mItemCount;
      boolean var2 = false;
      boolean var3 = false;
      if (var4 > 0) {
         boolean var1 = var3;
         int var5;
         if (this.mNeedSync) {
            this.mNeedSync = false;
            var5 = this.findSyncPosition();
            var1 = var3;
            if (var5 >= 0) {
               var1 = var3;
               if (this.lookForSelectablePosition(var5, true) == var5) {
                  this.setNextSelectedPositionInt(var5);
                  var1 = true;
               }
            }
         }

         var2 = var1;
         if (!var1) {
            int var6 = this.getSelectedItemPosition();
            var5 = var6;
            if (var6 >= var4) {
               var5 = var4 - 1;
            }

            var6 = var5;
            if (var5 < 0) {
               var6 = 0;
            }

            var5 = this.lookForSelectablePosition(var6, true);
            var4 = var5;
            if (var5 < 0) {
               var4 = this.lookForSelectablePosition(var6, false);
            }

            var2 = var1;
            if (var4 >= 0) {
               this.setNextSelectedPositionInt(var4);
               this.checkSelectionChanged();
               var2 = true;
            }
         }
      }

      if (!var2) {
         this.mSelectedPosition = -1;
         this.mSelectedRowId = Long.MIN_VALUE;
         this.mNextSelectedPosition = -1;
         this.mNextSelectedRowId = Long.MIN_VALUE;
         this.mNeedSync = false;
         this.checkSelectionChanged();
      }

   }

   boolean isInFilterMode() {
      return false;
   }

   int lookForSelectablePosition(int var1, boolean var2) {
      return var1;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mSelectionNotifier);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mLayoutHeight = this.getHeight();
   }

   public boolean performItemClick(View var1, int var2, long var3) {
      boolean var5 = false;
      if (this.mOnItemClickListener != null) {
         this.playSoundEffect(0);
         if (var1 != null) {
            var1.sendAccessibilityEvent(1);
         }

         this.mOnItemClickListener.onItemClick(this, var1, var2, var3);
         var5 = true;
      }

      return var5;
   }

   void rememberSyncState() {
      if (this.getChildCount() > 0) {
         this.mNeedSync = true;
         this.mSyncHeight = (long)this.mLayoutHeight;
         View var1;
         if (this.mSelectedPosition < 0) {
            var1 = this.getChildAt(0);
            Adapter var2 = this.getAdapter();
            if (this.mFirstPosition >= 0 && this.mFirstPosition < var2.getCount()) {
               this.mSyncRowId = var2.getItemId(this.mFirstPosition);
            } else {
               this.mSyncRowId = -1L;
            }

            this.mSyncPosition = this.mFirstPosition;
            if (var1 != null) {
               this.mSpecificTop = var1.getTop();
            }

            this.mSyncMode = 1;
            return;
         }

         var1 = this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
         this.mSyncRowId = this.mNextSelectedRowId;
         this.mSyncPosition = this.mNextSelectedPosition;
         if (var1 != null) {
            this.mSpecificTop = var1.getTop();
         }

         this.mSyncMode = 0;
      }

   }

   public void removeAllViews() {
      throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
   }

   public void removeView(View var1) {
      throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
   }

   public void removeViewAt(int var1) {
      throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
   }

   void selectionChanged() {
      if (this.mOnItemSelectedListener != null) {
         if (!this.mInLayout && !this.mBlockLayoutRequests) {
            this.fireOnSelected();
         } else {
            if (this.mSelectionNotifier == null) {
               this.mSelectionNotifier = new AdapterViewICS.SelectionNotifier();
            }

            this.post(this.mSelectionNotifier);
         }
      }

      if (this.mSelectedPosition != -1 && this.isShown() && !this.isInTouchMode()) {
         this.sendAccessibilityEvent(4);
      }

   }

   public abstract void setAdapter(Adapter var1);

   public void setEmptyView(View var1) {
      this.mEmptyView = var1;
      Adapter var3 = this.getAdapter();
      boolean var2;
      if (var3 != null && !var3.isEmpty()) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.updateEmptyStatus(var2);
   }

   public void setFocusable(boolean var1) {
      boolean var3 = true;
      Adapter var4 = this.getAdapter();
      boolean var2;
      if (var4 != null && var4.getCount() != 0) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.mDesiredFocusableState = var1;
      if (!var1) {
         this.mDesiredFocusableInTouchModeState = false;
      }

      label22: {
         if (var1) {
            var1 = var3;
            if (!var2) {
               break label22;
            }

            if (this.isInFilterMode()) {
               var1 = var3;
               break label22;
            }
         }

         var1 = false;
      }

      super.setFocusable(var1);
   }

   public void setFocusableInTouchMode(boolean var1) {
      boolean var3 = true;
      Adapter var4 = this.getAdapter();
      boolean var2;
      if (var4 != null && var4.getCount() != 0) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.mDesiredFocusableInTouchModeState = var1;
      if (var1) {
         this.mDesiredFocusableState = true;
      }

      label22: {
         if (var1) {
            var1 = var3;
            if (!var2) {
               break label22;
            }

            if (this.isInFilterMode()) {
               var1 = var3;
               break label22;
            }
         }

         var1 = false;
      }

      super.setFocusableInTouchMode(var1);
   }

   void setNextSelectedPositionInt(int var1) {
      this.mNextSelectedPosition = var1;
      this.mNextSelectedRowId = this.getItemIdAtPosition(var1);
      if (this.mNeedSync && this.mSyncMode == 0 && var1 >= 0) {
         this.mSyncPosition = var1;
         this.mSyncRowId = this.mNextSelectedRowId;
      }

   }

   public void setOnClickListener(OnClickListener var1) {
      throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
   }

   public void setOnItemClickListener(AdapterViewICS.OnItemClickListener var1) {
      this.mOnItemClickListener = var1;
   }

   public void setOnItemLongClickListener(AdapterViewICS.OnItemLongClickListener var1) {
      if (!this.isLongClickable()) {
         this.setLongClickable(true);
      }

      this.mOnItemLongClickListener = var1;
   }

   public void setOnItemSelectedListener(AdapterViewICS.OnItemSelectedListener var1) {
      this.mOnItemSelectedListener = var1;
   }

   void setSelectedPositionInt(int var1) {
      this.mSelectedPosition = var1;
      this.mSelectedRowId = this.getItemIdAtPosition(var1);
   }

   public abstract void setSelection(int var1);

   public static class AdapterContextMenuInfo implements ContextMenuInfo {
      // $FF: renamed from: id long
      public long field_1;
      public int position;
      public View targetView;

      public AdapterContextMenuInfo(View var1, int var2, long var3) {
         this.targetView = var1;
         this.position = var2;
         this.field_1 = var3;
      }
   }

   class AdapterDataSetObserver extends DataSetObserver {
      private Parcelable mInstanceState = null;

      public void clearSavedState() {
         this.mInstanceState = null;
      }

      public void onChanged() {
         AdapterViewICS.this.mDataChanged = true;
         AdapterViewICS.this.mOldItemCount = AdapterViewICS.this.mItemCount;
         AdapterViewICS.this.mItemCount = AdapterViewICS.this.getAdapter().getCount();
         if (AdapterViewICS.this.getAdapter().hasStableIds() && this.mInstanceState != null && AdapterViewICS.this.mOldItemCount == 0 && AdapterViewICS.this.mItemCount > 0) {
            AdapterViewICS.this.onRestoreInstanceState(this.mInstanceState);
            this.mInstanceState = null;
         } else {
            AdapterViewICS.this.rememberSyncState();
         }

         AdapterViewICS.this.checkFocus();
         AdapterViewICS.this.requestLayout();
      }

      public void onInvalidated() {
         AdapterViewICS.this.mDataChanged = true;
         if (AdapterViewICS.this.getAdapter().hasStableIds()) {
            this.mInstanceState = AdapterViewICS.this.onSaveInstanceState();
         }

         AdapterViewICS.this.mOldItemCount = AdapterViewICS.this.mItemCount;
         AdapterViewICS.this.mItemCount = 0;
         AdapterViewICS.this.mSelectedPosition = -1;
         AdapterViewICS.this.mSelectedRowId = Long.MIN_VALUE;
         AdapterViewICS.this.mNextSelectedPosition = -1;
         AdapterViewICS.this.mNextSelectedRowId = Long.MIN_VALUE;
         AdapterViewICS.this.mNeedSync = false;
         AdapterViewICS.this.checkFocus();
         AdapterViewICS.this.requestLayout();
      }
   }

   public interface OnItemClickListener {
      void onItemClick(AdapterViewICS var1, View var2, int var3, long var4);
   }

   class OnItemClickListenerWrapper implements android.widget.AdapterView.OnItemClickListener {
      private final AdapterViewICS.OnItemClickListener mWrappedListener;

      public OnItemClickListenerWrapper(AdapterViewICS.OnItemClickListener var2) {
         this.mWrappedListener = var2;
      }

      public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
         this.mWrappedListener.onItemClick(AdapterViewICS.this, var2, var3, var4);
      }
   }

   public interface OnItemLongClickListener {
      boolean onItemLongClick(AdapterViewICS var1, View var2, int var3, long var4);
   }

   public interface OnItemSelectedListener {
      void onItemSelected(AdapterViewICS var1, View var2, int var3, long var4);

      void onNothingSelected(AdapterViewICS var1);
   }

   private class SelectionNotifier implements Runnable {
      private SelectionNotifier() {
      }

      // $FF: synthetic method
      SelectionNotifier(Object var2) {
         this();
      }

      public void run() {
         if (AdapterViewICS.this.mDataChanged) {
            if (AdapterViewICS.this.getAdapter() != null) {
               AdapterViewICS.this.post(this);
            }

         } else {
            AdapterViewICS.this.fireOnSelected();
         }
      }
   }
}
