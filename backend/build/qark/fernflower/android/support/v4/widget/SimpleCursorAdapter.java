package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
   private SimpleCursorAdapter.CursorToStringConverter mCursorToStringConverter;
   protected int[] mFrom;
   String[] mOriginalFrom;
   private int mStringConversionColumn = -1;
   protected int[] mTo;
   private SimpleCursorAdapter.ViewBinder mViewBinder;

   @Deprecated
   public SimpleCursorAdapter(Context var1, int var2, Cursor var3, String[] var4, int[] var5) {
      super(var1, var2, var3);
      this.mTo = var5;
      this.mOriginalFrom = var4;
      this.findColumns(var4);
   }

   public SimpleCursorAdapter(Context var1, int var2, Cursor var3, String[] var4, int[] var5, int var6) {
      super(var1, var2, var3, var6);
      this.mTo = var5;
      this.mOriginalFrom = var4;
      this.findColumns(var4);
   }

   private void findColumns(String[] var1) {
      if (this.mCursor != null) {
         int var3 = var1.length;
         if (this.mFrom == null || this.mFrom.length != var3) {
            this.mFrom = new int[var3];
         }

         for(int var2 = 0; var2 < var3; ++var2) {
            this.mFrom[var2] = this.mCursor.getColumnIndexOrThrow(var1[var2]);
         }
      } else {
         this.mFrom = null;
      }

   }

   public void bindView(View var1, Context var2, Cursor var3) {
      SimpleCursorAdapter.ViewBinder var8 = this.mViewBinder;
      int var5 = this.mTo.length;
      int[] var9 = this.mFrom;
      int[] var10 = this.mTo;

      for(int var4 = 0; var4 < var5; ++var4) {
         View var11 = var1.findViewById(var10[var4]);
         if (var11 != null) {
            boolean var6 = false;
            if (var8 != null) {
               var6 = var8.setViewValue(var11, var3, var9[var4]);
            }

            if (!var6) {
               String var7 = var3.getString(var9[var4]);
               String var12 = var7;
               if (var7 == null) {
                  var12 = "";
               }

               if (var11 instanceof TextView) {
                  this.setViewText((TextView)var11, var12);
               } else {
                  if (!(var11 instanceof ImageView)) {
                     throw new IllegalStateException(var11.getClass().getName() + " is not a " + " view that can be bounds by this SimpleCursorAdapter");
                  }

                  this.setViewImage((ImageView)var11, var12);
               }
            }
         }
      }

   }

   public void changeCursorAndColumns(Cursor var1, String[] var2, int[] var3) {
      this.mOriginalFrom = var2;
      this.mTo = var3;
      super.changeCursor(var1);
      this.findColumns(this.mOriginalFrom);
   }

   public CharSequence convertToString(Cursor var1) {
      if (this.mCursorToStringConverter != null) {
         return this.mCursorToStringConverter.convertToString(var1);
      } else {
         return (CharSequence)(this.mStringConversionColumn > -1 ? var1.getString(this.mStringConversionColumn) : super.convertToString(var1));
      }
   }

   public SimpleCursorAdapter.CursorToStringConverter getCursorToStringConverter() {
      return this.mCursorToStringConverter;
   }

   public int getStringConversionColumn() {
      return this.mStringConversionColumn;
   }

   public SimpleCursorAdapter.ViewBinder getViewBinder() {
      return this.mViewBinder;
   }

   public void setCursorToStringConverter(SimpleCursorAdapter.CursorToStringConverter var1) {
      this.mCursorToStringConverter = var1;
   }

   public void setStringConversionColumn(int var1) {
      this.mStringConversionColumn = var1;
   }

   public void setViewBinder(SimpleCursorAdapter.ViewBinder var1) {
      this.mViewBinder = var1;
   }

   public void setViewImage(ImageView var1, String var2) {
      try {
         var1.setImageResource(Integer.parseInt(var2));
      } catch (NumberFormatException var4) {
         var1.setImageURI(Uri.parse(var2));
      }
   }

   public void setViewText(TextView var1, String var2) {
      var1.setText(var2);
   }

   public Cursor swapCursor(Cursor var1) {
      var1 = super.swapCursor(var1);
      this.findColumns(this.mOriginalFrom);
      return var1;
   }

   public interface CursorToStringConverter {
      CharSequence convertToString(Cursor var1);
   }

   public interface ViewBinder {
      boolean setViewValue(View var1, Cursor var2, int var3);
   }
}
