package android.support.v7.internal.view.menu;

class BaseWrapper {
   final Object mWrappedObject;

   BaseWrapper(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Wrapped Object can not be null.");
      } else {
         this.mWrappedObject = var1;
      }
   }

   public Object getWrappedObject() {
      return this.mWrappedObject;
   }
}
