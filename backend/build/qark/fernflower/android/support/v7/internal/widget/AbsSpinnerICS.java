package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.SpinnerAdapter;

abstract class AbsSpinnerICS extends AdapterViewICS {
   SpinnerAdapter mAdapter;
   boolean mBlockLayoutRequests;
   private DataSetObserver mDataSetObserver;
   int mHeightMeasureSpec;
   final AbsSpinnerICS.RecycleBin mRecycler;
   int mSelectionBottomPadding;
   int mSelectionLeftPadding;
   int mSelectionRightPadding;
   int mSelectionTopPadding;
   final Rect mSpinnerPadding;
   private Rect mTouchFrame;
   int mWidthMeasureSpec;

   AbsSpinnerICS(Context var1) {
      super(var1);
      this.mSelectionLeftPadding = 0;
      this.mSelectionTopPadding = 0;
      this.mSelectionRightPadding = 0;
      this.mSelectionBottomPadding = 0;
      this.mSpinnerPadding = new Rect();
      this.mRecycler = new AbsSpinnerICS.RecycleBin();
      this.initAbsSpinner();
   }

   AbsSpinnerICS(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   AbsSpinnerICS(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSelectionLeftPadding = 0;
      this.mSelectionTopPadding = 0;
      this.mSelectionRightPadding = 0;
      this.mSelectionBottomPadding = 0;
      this.mSpinnerPadding = new Rect();
      this.mRecycler = new AbsSpinnerICS.RecycleBin();
      this.initAbsSpinner();
   }

   private void initAbsSpinner() {
      this.setFocusable(true);
      this.setWillNotDraw(false);
   }

   protected LayoutParams generateDefaultLayoutParams() {
      return new LayoutParams(-1, -2);
   }

   public SpinnerAdapter getAdapter() {
      return this.mAdapter;
   }

   int getChildHeight(View var1) {
      return var1.getMeasuredHeight();
   }

   int getChildWidth(View var1) {
      return var1.getMeasuredWidth();
   }

   public int getCount() {
      return this.mItemCount;
   }

   public View getSelectedView() {
      return this.mItemCount > 0 && this.mSelectedPosition >= 0 ? this.getChildAt(this.mSelectedPosition - this.mFirstPosition) : null;
   }

   abstract void layout(int var1, boolean var2);

   protected void onMeasure(int var1, int var2) {
      int var9 = MeasureSpec.getMode(var1);
      int var3 = this.getPaddingLeft();
      int var6 = this.getPaddingTop();
      int var5 = this.getPaddingRight();
      int var4 = this.getPaddingBottom();
      Rect var11 = this.mSpinnerPadding;
      if (var3 <= this.mSelectionLeftPadding) {
         var3 = this.mSelectionLeftPadding;
      }

      var11.left = var3;
      var11 = this.mSpinnerPadding;
      if (var6 > this.mSelectionTopPadding) {
         var3 = var6;
      } else {
         var3 = this.mSelectionTopPadding;
      }

      var11.top = var3;
      var11 = this.mSpinnerPadding;
      if (var5 > this.mSelectionRightPadding) {
         var3 = var5;
      } else {
         var3 = this.mSelectionRightPadding;
      }

      var11.right = var3;
      var11 = this.mSpinnerPadding;
      if (var4 > this.mSelectionBottomPadding) {
         var3 = var4;
      } else {
         var3 = this.mSelectionBottomPadding;
      }

      var11.bottom = var3;
      if (this.mDataChanged) {
         this.handleDataChanged();
      }

      byte var14 = 0;
      byte var7 = 0;
      boolean var8 = true;
      int var10 = this.getSelectedItemPosition();
      boolean var13 = var8;
      var4 = var14;
      var3 = var7;
      if (var10 >= 0) {
         var13 = var8;
         var4 = var14;
         var3 = var7;
         if (this.mAdapter != null) {
            var13 = var8;
            var4 = var14;
            var3 = var7;
            if (var10 < this.mAdapter.getCount()) {
               View var12 = this.mRecycler.get(var10);
               View var15 = var12;
               if (var12 == null) {
                  var15 = this.mAdapter.getView(var10, (View)null, this);
               }

               if (var15 != null) {
                  this.mRecycler.put(var10, var15);
               }

               var13 = var8;
               var4 = var14;
               var3 = var7;
               if (var15 != null) {
                  if (var15.getLayoutParams() == null) {
                     this.mBlockLayoutRequests = true;
                     var15.setLayoutParams(this.generateDefaultLayoutParams());
                     this.mBlockLayoutRequests = false;
                  }

                  this.measureChild(var15, var1, var2);
                  var4 = this.getChildHeight(var15) + this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
                  var3 = this.getChildWidth(var15) + this.mSpinnerPadding.left + this.mSpinnerPadding.right;
                  var13 = false;
               }
            }
         }
      }

      var6 = var4;
      var4 = var3;
      if (var13) {
         var5 = this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
         var6 = var5;
         var4 = var3;
         if (var9 == 0) {
            var4 = this.mSpinnerPadding.left + this.mSpinnerPadding.right;
            var6 = var5;
         }
      }

      var3 = Math.max(var6, this.getSuggestedMinimumHeight());
      var4 = Math.max(var4, this.getSuggestedMinimumWidth());
      var3 = resolveSize(var3, var2);
      this.setMeasuredDimension(resolveSize(var4, var1), var3);
      this.mHeightMeasureSpec = var2;
      this.mWidthMeasureSpec = var1;
   }

   public void onRestoreInstanceState(Parcelable var1) {
      AbsSpinnerICS.SavedState var2 = (AbsSpinnerICS.SavedState)var1;
      super.onRestoreInstanceState(var2.getSuperState());
      if (var2.selectedId >= 0L) {
         this.mDataChanged = true;
         this.mNeedSync = true;
         this.mSyncRowId = var2.selectedId;
         this.mSyncPosition = var2.position;
         this.mSyncMode = 0;
         this.requestLayout();
      }

   }

   public Parcelable onSaveInstanceState() {
      AbsSpinnerICS.SavedState var1 = new AbsSpinnerICS.SavedState(super.onSaveInstanceState());
      var1.selectedId = this.getSelectedItemId();
      if (var1.selectedId >= 0L) {
         var1.position = this.getSelectedItemPosition();
         return var1;
      } else {
         var1.position = -1;
         return var1;
      }
   }

   public int pointToPosition(int var1, int var2) {
      Rect var5 = this.mTouchFrame;
      Rect var4 = var5;
      if (var5 == null) {
         this.mTouchFrame = new Rect();
         var4 = this.mTouchFrame;
      }

      for(int var3 = this.getChildCount() - 1; var3 >= 0; --var3) {
         View var6 = this.getChildAt(var3);
         if (var6.getVisibility() == 0) {
            var6.getHitRect(var4);
            if (var4.contains(var1, var2)) {
               return this.mFirstPosition + var3;
            }
         }
      }

      return -1;
   }

   void recycleAllViews() {
      int var2 = this.getChildCount();
      AbsSpinnerICS.RecycleBin var4 = this.mRecycler;
      int var3 = this.mFirstPosition;

      for(int var1 = 0; var1 < var2; ++var1) {
         var4.put(var3 + var1, this.getChildAt(var1));
      }

   }

   public void requestLayout() {
      if (!this.mBlockLayoutRequests) {
         super.requestLayout();
      }

   }

   void resetList() {
      this.mDataChanged = false;
      this.mNeedSync = false;
      this.removeAllViewsInLayout();
      this.mOldSelectedPosition = -1;
      this.mOldSelectedRowId = Long.MIN_VALUE;
      this.setSelectedPositionInt(-1);
      this.setNextSelectedPositionInt(-1);
      this.invalidate();
   }

   public void setAdapter(SpinnerAdapter var1) {
      byte var2 = -1;
      if (this.mAdapter != null) {
         this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
         this.resetList();
      }

      this.mAdapter = var1;
      this.mOldSelectedPosition = -1;
      this.mOldSelectedRowId = Long.MIN_VALUE;
      if (this.mAdapter != null) {
         this.mOldItemCount = this.mItemCount;
         this.mItemCount = this.mAdapter.getCount();
         this.checkFocus();
         this.mDataSetObserver = new AdapterViewICS.AdapterDataSetObserver();
         this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
         if (this.mItemCount > 0) {
            var2 = 0;
         }

         this.setSelectedPositionInt(var2);
         this.setNextSelectedPositionInt(var2);
         if (this.mItemCount == 0) {
            this.checkSelectionChanged();
         }
      } else {
         this.checkFocus();
         this.resetList();
         this.checkSelectionChanged();
      }

      this.requestLayout();
   }

   public void setSelection(int var1) {
      this.setNextSelectedPositionInt(var1);
      this.requestLayout();
      this.invalidate();
   }

   public void setSelection(int var1, boolean var2) {
      if (var2 && this.mFirstPosition <= var1 && var1 <= this.mFirstPosition + this.getChildCount() - 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setSelectionInt(var1, var2);
   }

   void setSelectionInt(int var1, boolean var2) {
      if (var1 != this.mOldSelectedPosition) {
         this.mBlockLayoutRequests = true;
         int var3 = this.mSelectedPosition;
         this.setNextSelectedPositionInt(var1);
         this.layout(var1 - var3, var2);
         this.mBlockLayoutRequests = false;
      }

   }

   class RecycleBin {
      private final SparseArray mScrapHeap = new SparseArray();

      void clear() {
         SparseArray var3 = this.mScrapHeap;
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            View var4 = (View)var3.valueAt(var1);
            if (var4 != null) {
               AbsSpinnerICS.this.removeDetachedView(var4, true);
            }
         }

         var3.clear();
      }

      View get(int var1) {
         View var2 = (View)this.mScrapHeap.get(var1);
         if (var2 != null) {
            this.mScrapHeap.delete(var1);
         }

         return var2;
      }

      public void put(int var1, View var2) {
         this.mScrapHeap.put(var1, var2);
      }
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public AbsSpinnerICS.SavedState createFromParcel(Parcel var1) {
            return new AbsSpinnerICS.SavedState(var1);
         }

         public AbsSpinnerICS.SavedState[] newArray(int var1) {
            return new AbsSpinnerICS.SavedState[var1];
         }
      };
      int position;
      long selectedId;

      private SavedState(Parcel var1) {
         super(var1);
         this.selectedId = var1.readLong();
         this.position = var1.readInt();
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "AbsSpinner.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " selectedId=" + this.selectedId + " position=" + this.position + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeLong(this.selectedId);
         var1.writeInt(this.position);
      }
   }
}
