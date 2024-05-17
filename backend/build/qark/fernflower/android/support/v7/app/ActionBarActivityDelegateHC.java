package android.support.v7.app;

class ActionBarActivityDelegateHC extends ActionBarActivityDelegateBase {
   ActionBarActivityDelegateHC(ActionBarActivity var1) {
      super(var1);
   }

   public ActionBar createSupportActionBar() {
      this.ensureSubDecor();
      return new ActionBarImplHC(this.mActivity, this.mActivity);
   }
}
