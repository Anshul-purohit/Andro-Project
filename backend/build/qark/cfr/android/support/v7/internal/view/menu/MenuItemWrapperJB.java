/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ActionProvider
 *  android.view.ActionProvider$VisibilityListener
 *  android.view.MenuItem
 *  android.view.View
 *  java.lang.Object
 */
package android.support.v7.internal.view.menu;

import android.support.v4.view.ActionProvider;
import android.support.v7.internal.view.menu.MenuItemWrapperICS;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;

class MenuItemWrapperJB
extends MenuItemWrapperICS {
    MenuItemWrapperJB(MenuItem menuItem) {
        super(menuItem, false);
    }

    @Override
    MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider actionProvider) {
        return new ActionProviderWrapperJB(actionProvider);
    }

    class ActionProviderWrapperJB
    extends MenuItemWrapperICS.ActionProviderWrapper
    implements ActionProvider.VisibilityListener {
        ActionProvider.VisibilityListener mListener;

        public ActionProviderWrapperJB(ActionProvider actionProvider) {
            super(actionProvider);
        }

        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        @Override
        public void onActionProviderVisibilityChanged(boolean bl) {
            if (this.mListener != null) {
                this.mListener.onActionProviderVisibilityChanged(bl);
            }
        }

        public View onCreateActionView(MenuItem menuItem) {
            return this.mInner.onCreateActionView(menuItem);
        }

        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setVisibilityListener(ActionProvider.VisibilityListener object) {
            this.mListener = object;
            ActionProvider actionProvider = this.mInner;
            object = object != null ? this : null;
            actionProvider.setVisibilityListener((ActionProvider.VisibilityListener)object);
        }
    }

}

