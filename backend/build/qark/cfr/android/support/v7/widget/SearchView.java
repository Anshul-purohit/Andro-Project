/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.app.SearchableInfo
 *  android.content.ActivityNotFoundException
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.database.Cursor
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.os.ResultReceiver
 *  android.text.Editable
 *  android.text.SpannableStringBuilder
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.text.style.ImageSpan
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.KeyEvent
 *  android.view.KeyEvent$DispatcherState
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.View$OnFocusChangeListener
 *  android.view.View$OnKeyListener
 *  android.view.View$OnLayoutChangeListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.AutoCompleteTextView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.ListAdapter
 *  android.widget.TextView
 *  android.widget.TextView$OnEditorActionListener
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 *  java.util.WeakHashMap
 */
package android.support.v7.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.SuggestionsAdapter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView
extends LinearLayout
implements CollapsibleActionView {
    private static final boolean DBG = false;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    private static final String LOG_TAG = "SearchView";
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    private ImageView mCloseButton;
    private int mCollapsedImeOptions;
    private View mDropDownAnchor;
    private boolean mExpandedInActionView;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View.OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView.OnEditorActionListener mOnEditorActionListener;
    private final AdapterView.OnItemClickListener mOnItemClickListener;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    private View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View.OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private SearchAutoComplete mQueryTextView;
    private Runnable mReleaseCursorRunnable;
    private View mSearchButton;
    private View mSearchEditFrame;
    private ImageView mSearchHintIcon;
    private View mSearchPlate;
    private SearchableInfo mSearchable;
    private Runnable mShowImeRunnable;
    private View mSubmitArea;
    private View mSubmitButton;
    private boolean mSubmitButtonEnabled;
    private CursorAdapter mSuggestionsAdapter;
    View.OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    private View mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;

    public SearchView(Context context) {
        this(context, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public SearchView(Context context, AttributeSet attributeSet) {
        CharSequence charSequence;
        super(context, attributeSet);
        this.mShowImeRunnable = new Runnable(){

            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
                if (inputMethodManager != null) {
                    SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(inputMethodManager, (View)SearchView.this, 0);
                }
            }
        };
        this.mUpdateDrawableStateRunnable = new Runnable(){

            public void run() {
                SearchView.this.updateFocusedState();
            }
        };
        this.mReleaseCursorRunnable = new Runnable(){

            public void run() {
                if (SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
                    SearchView.this.mSuggestionsAdapter.changeCursor(null);
                }
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap();
        this.mOnClickListener = new View.OnClickListener(){

            /*
             * Enabled aggressive block sorting
             */
            public void onClick(View view) {
                if (view == SearchView.this.mSearchButton) {
                    SearchView.this.onSearchClicked();
                    return;
                } else {
                    if (view == SearchView.this.mCloseButton) {
                        SearchView.this.onCloseClicked();
                        return;
                    }
                    if (view == SearchView.this.mSubmitButton) {
                        SearchView.this.onSubmitQuery();
                        return;
                    }
                    if (view == SearchView.this.mVoiceButton) {
                        SearchView.this.onVoiceClicked();
                        return;
                    }
                    if (view != SearchView.this.mQueryTextView) return;
                    {
                        SearchView.this.forceSuggestionQuery();
                        return;
                    }
                }
            }
        };
        this.mTextKeyListener = new View.OnKeyListener(){

            /*
             * Enabled aggressive block sorting
             */
            public boolean onKey(View view, int n, KeyEvent keyEvent) {
                block5 : {
                    block4 : {
                        if (SearchView.this.mSearchable == null) break block4;
                        if (SearchView.this.mQueryTextView.isPopupShowing() && SearchView.this.mQueryTextView.getListSelection() != -1) {
                            return SearchView.this.onSuggestionsKey(view, n, keyEvent);
                        }
                        if (!SearchView.this.mQueryTextView.isEmpty() && KeyEventCompat.hasNoModifiers(keyEvent) && keyEvent.getAction() == 1 && n == 66) break block5;
                    }
                    return false;
                }
                view.cancelLongPress();
                SearchView.this.launchQuerySearch(0, null, SearchView.this.mQueryTextView.getText().toString());
                return true;
            }
        };
        this.mOnEditorActionListener = new TextView.OnEditorActionListener(){

            public boolean onEditorAction(TextView textView, int n, KeyEvent keyEvent) {
                SearchView.this.onSubmitQuery();
                return true;
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                SearchView.this.onItemClicked(n, 0, null);
            }
        };
        this.mOnItemSelectedListener = new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {
                SearchView.this.onItemSelected(n);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        this.mTextWatcher = new TextWatcher(){

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
            }

            public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                SearchView.this.onTextChanged(charSequence);
            }
        };
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.abc_search_view, (ViewGroup)this, true);
        this.mSearchButton = this.findViewById(R.id.search_button);
        this.mQueryTextView = (SearchAutoComplete)this.findViewById(R.id.search_src_text);
        this.mQueryTextView.setSearchView(this);
        this.mSearchEditFrame = this.findViewById(R.id.search_edit_frame);
        this.mSearchPlate = this.findViewById(R.id.search_plate);
        this.mSubmitArea = this.findViewById(R.id.submit_area);
        this.mSubmitButton = this.findViewById(R.id.search_go_btn);
        this.mCloseButton = (ImageView)this.findViewById(R.id.search_close_btn);
        this.mVoiceButton = this.findViewById(R.id.search_voice_btn);
        this.mSearchHintIcon = (ImageView)this.findViewById(R.id.search_mag_icon);
        this.mSearchButton.setOnClickListener(this.mOnClickListener);
        this.mCloseButton.setOnClickListener(this.mOnClickListener);
        this.mSubmitButton.setOnClickListener(this.mOnClickListener);
        this.mVoiceButton.setOnClickListener(this.mOnClickListener);
        this.mQueryTextView.setOnClickListener(this.mOnClickListener);
        this.mQueryTextView.addTextChangedListener(this.mTextWatcher);
        this.mQueryTextView.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mQueryTextView.setOnItemClickListener(this.mOnItemClickListener);
        this.mQueryTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
        this.mQueryTextView.setOnKeyListener(this.mTextKeyListener);
        this.mQueryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            public void onFocusChange(View view, boolean bl) {
                if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
                    SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange((View)SearchView.this, bl);
                }
            }
        });
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SearchView, 0, 0);
        this.setIconifiedByDefault(typedArray.getBoolean(3, true));
        int n = typedArray.getDimensionPixelSize(0, -1);
        if (n != -1) {
            this.setMaxWidth(n);
        }
        if (!TextUtils.isEmpty((CharSequence)(charSequence = typedArray.getText(4)))) {
            this.setQueryHint(charSequence);
        }
        if ((n = typedArray.getInt(2, -1)) != -1) {
            this.setImeOptions(n);
        }
        if ((n = typedArray.getInt(1, -1)) != -1) {
            this.setInputType(n);
        }
        typedArray.recycle();
        context = context.obtainStyledAttributes(attributeSet, R.styleable.View, 0, 0);
        boolean bl = context.getBoolean(0, true);
        context.recycle();
        this.setFocusable(bl);
        this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
        this.mVoiceWebSearchIntent.addFlags(268435456);
        this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mVoiceAppSearchIntent.addFlags(268435456);
        this.mDropDownAnchor = this.findViewById(this.mQueryTextView.getDropDownAnchor());
        if (this.mDropDownAnchor != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                this.addOnLayoutChangeListenerToDropDownAnchorSDK11();
            } else {
                this.addOnLayoutChangeListenerToDropDownAnchorBase();
            }
        }
        this.updateViewsVisibility(this.mIconifiedByDefault);
        this.updateQueryHint();
    }

    private void addOnLayoutChangeListenerToDropDownAnchorBase() {
        this.mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            public void onGlobalLayout() {
                SearchView.this.adjustDropDownSizeAndPosition();
            }
        });
    }

    private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
        this.mDropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){

            public void onLayoutChange(View view, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
                SearchView.this.adjustDropDownSizeAndPosition();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     */
    private void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            Resources resources = this.getContext().getResources();
            int n = this.mSearchPlate.getPaddingLeft();
            Rect rect = new Rect();
            int n2 = this.mIconifiedByDefault ? resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left) : 0;
            this.mQueryTextView.getDropDownBackground().getPadding(rect);
            int n3 = rect.left;
            this.mQueryTextView.setDropDownHorizontalOffset(n - (n3 + n2));
            n3 = this.mDropDownAnchor.getWidth();
            int n4 = rect.left;
            int n5 = rect.right;
            this.mQueryTextView.setDropDownWidth(n3 + n4 + n5 + n2 - n);
        }
    }

    private Intent createIntent(String string2, Uri uri, String string3, String string4, int n, String string5) {
        string2 = new Intent(string2);
        string2.addFlags(268435456);
        if (uri != null) {
            string2.setData(uri);
        }
        string2.putExtra("user_query", this.mUserQuery);
        if (string4 != null) {
            string2.putExtra("query", string4);
        }
        if (string3 != null) {
            string2.putExtra("intent_extra_data_key", string3);
        }
        if (this.mAppSearchData != null) {
            string2.putExtra("app_data", this.mAppSearchData);
        }
        if (n != 0) {
            string2.putExtra("action_key", n);
            string2.putExtra("action_msg", string5);
        }
        string2.setComponent(this.mSearchable.getSearchActivity());
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Intent createIntentFromSuggestion(Cursor cursor, int n, String string2) {
        String string3;
        String string4;
        String string5;
        try {
            string5 = string4 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_action");
            if (string4 == null) {
                string5 = this.mSearchable.getSuggestIntentAction();
            }
        }
        catch (RuntimeException runtimeException) {
            try {
                n = cursor.getPosition();
            }
            catch (RuntimeException runtimeException2) {
                n = -1;
            }
            Log.w((String)"SearchView", (String)("Search suggestions cursor at row " + n + " returned exception."), (Throwable)runtimeException);
            return null;
        }
        string4 = string5;
        if (string5 == null) {
            string4 = "android.intent.action.SEARCH";
        }
        string5 = string3 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data");
        if (string3 == null) {
            string5 = this.mSearchable.getSuggestIntentData();
        }
        string3 = string5;
        if (string5 != null) {
            String string6 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data_id");
            string3 = string5;
            if (string6 != null) {
                string3 = string5 + "/" + Uri.encode((String)string6);
            }
        }
        string5 = string3 == null ? null : Uri.parse((String)string3);
        string3 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_query");
        return this.createIntent(string4, (Uri)string5, SuggestionsAdapter.getColumnString(cursor, "suggest_intent_extra_data"), string3, n, string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private Intent createVoiceAppSearchIntent(Intent object, SearchableInfo searchableInfo) {
        ComponentName componentName = searchableInfo.getSearchActivity();
        Object object2 = new Intent("android.intent.action.SEARCH");
        object2.setComponent(componentName);
        PendingIntent pendingIntent = PendingIntent.getActivity((Context)this.getContext(), (int)0, (Intent)object2, (int)1073741824);
        Bundle bundle = new Bundle();
        if (this.mAppSearchData != null) {
            bundle.putParcelable("app_data", (Parcelable)this.mAppSearchData);
        }
        Intent intent = new Intent((Intent)object);
        object = "free_form";
        object2 = null;
        String string2 = null;
        int n = 1;
        Resources resources = this.getResources();
        if (searchableInfo.getVoiceLanguageModeId() != 0) {
            object = resources.getString(searchableInfo.getVoiceLanguageModeId());
        }
        if (searchableInfo.getVoicePromptTextId() != 0) {
            object2 = resources.getString(searchableInfo.getVoicePromptTextId());
        }
        if (searchableInfo.getVoiceLanguageId() != 0) {
            string2 = resources.getString(searchableInfo.getVoiceLanguageId());
        }
        if (searchableInfo.getVoiceMaxResults() != 0) {
            n = searchableInfo.getVoiceMaxResults();
        }
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", (String)object);
        intent.putExtra("android.speech.extra.PROMPT", (String)object2);
        intent.putExtra("android.speech.extra.LANGUAGE", string2);
        intent.putExtra("android.speech.extra.MAX_RESULTS", n);
        object = componentName == null ? null : componentName.flattenToShortString();
        intent.putExtra("calling_package", (String)object);
        intent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)pendingIntent);
        intent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Intent createVoiceWebSearchIntent(Intent object, SearchableInfo searchableInfo) {
        Intent intent = new Intent((Intent)object);
        object = searchableInfo.getSearchActivity();
        object = object == null ? null : object.flattenToShortString();
        intent.putExtra("calling_package", (String)object);
        return intent;
    }

    private void dismissSuggestions() {
        this.mQueryTextView.dismissDropDown();
    }

    private void forceSuggestionQuery() {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mQueryTextView);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mQueryTextView);
    }

    private CharSequence getDecoratedHint(CharSequence charSequence) {
        if (!this.mIconifiedByDefault) {
            return charSequence;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)"   ");
        spannableStringBuilder.append(charSequence);
        charSequence = this.getContext().getResources().getDrawable(this.getSearchIconId());
        int n = (int)((double)this.mQueryTextView.getTextSize() * 1.25);
        charSequence.setBounds(0, 0, n, n);
        spannableStringBuilder.setSpan((Object)new ImageSpan((Drawable)charSequence), 1, 2, 33);
        return spannableStringBuilder;
    }

    private int getPreferredWidth() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }

    private int getSearchIconId() {
        TypedValue typedValue = new TypedValue();
        this.getContext().getTheme().resolveAttribute(R.attr.searchViewSearchIcon, typedValue, true);
        return typedValue.resourceId;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean hasVoiceSearch() {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.mSearchable == null) return bl2;
        bl2 = bl;
        if (!this.mSearchable.getVoiceSearchEnabled()) return bl2;
        Intent intent = null;
        if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
            intent = this.mVoiceWebSearchIntent;
        } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
            intent = this.mVoiceAppSearchIntent;
        }
        bl2 = bl;
        if (intent == null) return bl2;
        bl2 = bl;
        if (this.getContext().getPackageManager().resolveActivity(intent, 65536) == null) return bl2;
        return true;
    }

    static boolean isLandscapeMode(Context context) {
        if (context.getResources().getConfiguration().orientation == 2) {
            return true;
        }
        return false;
    }

    private boolean isSubmitAreaEnabled() {
        if ((this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified()) {
            return true;
        }
        return false;
    }

    private void launchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            this.getContext().startActivity(intent);
            return;
        }
        catch (RuntimeException runtimeException) {
            Log.e((String)"SearchView", (String)("Failed launch activity: " + (Object)intent), (Throwable)runtimeException);
            return;
        }
    }

    private void launchQuerySearch(int n, String string2, String string3) {
        string2 = this.createIntent("android.intent.action.SEARCH", null, null, string3, n, string2);
        this.getContext().startActivity((Intent)string2);
    }

    private boolean launchSuggestion(int n, int n2, String string2) {
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor != null && cursor.moveToPosition(n)) {
            this.launchIntent(this.createIntentFromSuggestion(cursor, n2, string2));
            return true;
        }
        return false;
    }

    private void onCloseClicked() {
        if (TextUtils.isEmpty((CharSequence)this.mQueryTextView.getText())) {
            if (this.mIconifiedByDefault && (this.mOnCloseListener == null || !this.mOnCloseListener.onClose())) {
                this.clearFocus();
                this.updateViewsVisibility(true);
            }
            return;
        }
        this.mQueryTextView.setText((CharSequence)"");
        this.mQueryTextView.requestFocus();
        this.setImeVisibility(true);
    }

    private boolean onItemClicked(int n, int n2, String string2) {
        boolean bl = false;
        if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionClick(n)) {
            this.launchSuggestion(n, 0, null);
            this.setImeVisibility(false);
            this.dismissSuggestions();
            bl = true;
        }
        return bl;
    }

    private boolean onItemSelected(int n) {
        if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionSelect(n)) {
            this.rewriteQueryFromSuggestion(n);
            return true;
        }
        return false;
    }

    private void onSearchClicked() {
        this.updateViewsVisibility(false);
        this.mQueryTextView.requestFocus();
        this.setImeVisibility(true);
        if (this.mOnSearchClickListener != null) {
            this.mOnSearchClickListener.onClick((View)this);
        }
    }

    private void onSubmitQuery() {
        Editable editable = this.mQueryTextView.getText();
        if (!(editable == null || TextUtils.getTrimmedLength((CharSequence)editable) <= 0 || this.mOnQueryChangeListener != null && this.mOnQueryChangeListener.onQueryTextSubmit(editable.toString()))) {
            if (this.mSearchable != null) {
                this.launchQuerySearch(0, null, editable.toString());
                this.setImeVisibility(false);
            }
            this.dismissSuggestions();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean onSuggestionsKey(View view, int n, KeyEvent keyEvent) {
        block6 : {
            block5 : {
                if (this.mSearchable == null || this.mSuggestionsAdapter == null || keyEvent.getAction() != 0 || !KeyEventCompat.hasNoModifiers(keyEvent)) break block5;
                if (n == 66 || n == 84 || n == 61) {
                    return this.onItemClicked(this.mQueryTextView.getListSelection(), 0, null);
                }
                if (n == 21 || n == 22) {
                    n = n == 21 ? 0 : this.mQueryTextView.length();
                    this.mQueryTextView.setSelection(n);
                    this.mQueryTextView.setListSelection(0);
                    this.mQueryTextView.clearListSelection();
                    HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mQueryTextView, true);
                    return true;
                }
                if (n == 19 && this.mQueryTextView.getListSelection() == 0) break block6;
            }
            return false;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onTextChanged(CharSequence charSequence) {
        boolean bl = true;
        Editable editable = this.mQueryTextView.getText();
        this.mUserQuery = editable;
        boolean bl2 = !TextUtils.isEmpty((CharSequence)editable);
        this.updateSubmitButton(bl2);
        bl2 = !bl2 ? bl : false;
        this.updateVoiceButton(bl2);
        this.updateCloseButton();
        this.updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals((CharSequence)charSequence, (CharSequence)this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(charSequence.toString());
        }
        this.mOldQueryText = charSequence.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void onVoiceClicked() {
        SearchableInfo searchableInfo;
        block6 : {
            block5 : {
                if (this.mSearchable != null) {
                    searchableInfo = this.mSearchable;
                    try {
                        if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                            searchableInfo = this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo);
                            this.getContext().startActivity((Intent)searchableInfo);
                            return;
                        }
                        if (!searchableInfo.getVoiceSearchLaunchRecognizer()) break block5;
                        break block6;
                    }
                    catch (ActivityNotFoundException activityNotFoundException) {
                        Log.w((String)"SearchView", (String)"Could not find voice search activity");
                        return;
                    }
                }
            }
            return;
        }
        searchableInfo = this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, searchableInfo);
        this.getContext().startActivity((Intent)searchableInfo);
    }

    private void postUpdateFocusedState() {
        this.post(this.mUpdateDrawableStateRunnable);
    }

    private void rewriteQueryFromSuggestion(int n) {
        Editable editable = this.mQueryTextView.getText();
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor == null) {
            return;
        }
        if (cursor.moveToPosition(n)) {
            if ((cursor = this.mSuggestionsAdapter.convertToString(cursor)) != null) {
                this.setQuery((CharSequence)cursor);
                return;
            }
            this.setQuery((CharSequence)editable);
            return;
        }
        this.setQuery((CharSequence)editable);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setImeVisibility(boolean bl) {
        if (bl) {
            this.post(this.mShowImeRunnable);
            return;
        } else {
            this.removeCallbacks(this.mShowImeRunnable);
            InputMethodManager inputMethodManager = (InputMethodManager)this.getContext().getSystemService("input_method");
            if (inputMethodManager == null) return;
            {
                inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setQuery(CharSequence charSequence) {
        this.mQueryTextView.setText(charSequence);
        SearchAutoComplete searchAutoComplete = this.mQueryTextView;
        int n = TextUtils.isEmpty((CharSequence)charSequence) ? 0 : charSequence.length();
        searchAutoComplete.setSelection(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateCloseButton() {
        int n = 1;
        int n2 = 0;
        boolean bl = !TextUtils.isEmpty((CharSequence)this.mQueryTextView.getText());
        int n3 = n;
        if (!bl) {
            n3 = this.mIconifiedByDefault && !this.mExpandedInActionView ? n : 0;
        }
        int[] arrn = this.mCloseButton;
        n3 = n3 != 0 ? n2 : 8;
        arrn.setVisibility(n3);
        Drawable drawable2 = this.mCloseButton.getDrawable();
        arrn = bl ? ENABLED_STATE_SET : EMPTY_STATE_SET;
        drawable2.setState(arrn);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateFocusedState() {
        boolean bl = this.mQueryTextView.hasFocus();
        Drawable drawable2 = this.mSearchPlate.getBackground();
        int[] arrn = bl ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        drawable2.setState(arrn);
        drawable2 = this.mSubmitArea.getBackground();
        arrn = bl ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        drawable2.setState(arrn);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateQueryHint() {
        if (this.mQueryHint != null) {
            this.mQueryTextView.setHint(this.getDecoratedHint(this.mQueryHint));
            return;
        } else {
            if (this.mSearchable == null) {
                this.mQueryTextView.setHint(this.getDecoratedHint(""));
                return;
            }
            String string2 = null;
            int n = this.mSearchable.getHintId();
            if (n == 0) return;
            string2 = this.getContext().getString(n);
            if (string2 == null) return;
            {
                this.mQueryTextView.setHint(this.getDecoratedHint(string2));
                return;
            }
        }
    }

    private void updateSearchAutoComplete() {
        int n;
        int n2 = 1;
        this.mQueryTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mQueryTextView.setImeOptions(this.mSearchable.getImeOptions());
        int n3 = n = this.mSearchable.getInputType();
        if ((n & 15) == 1) {
            n3 = n &= -65537;
            if (this.mSearchable.getSuggestAuthority() != null) {
                n3 = n | 65536 | 524288;
            }
        }
        this.mQueryTextView.setInputType(n3);
        if (this.mSuggestionsAdapter != null) {
            this.mSuggestionsAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            this.mSuggestionsAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mQueryTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
            SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            n3 = n2;
            if (this.mQueryRefinement) {
                n3 = 2;
            }
            suggestionsAdapter.setQueryRefinement(n3);
        }
    }

    private void updateSubmitArea() {
        int n;
        block2 : {
            block3 : {
                int n2;
                n = n2 = 8;
                if (!this.isSubmitAreaEnabled()) break block2;
                if (this.mSubmitButton.getVisibility() == 0) break block3;
                n = n2;
                if (this.mVoiceButton.getVisibility() != 0) break block2;
            }
            n = 0;
        }
        this.mSubmitArea.setVisibility(n);
    }

    private void updateSubmitButton(boolean bl) {
        int n;
        block2 : {
            block3 : {
                int n2;
                n = n2 = 8;
                if (!this.mSubmitButtonEnabled) break block2;
                n = n2;
                if (!this.isSubmitAreaEnabled()) break block2;
                n = n2;
                if (!this.hasFocus()) break block2;
                if (bl) break block3;
                n = n2;
                if (this.mVoiceButtonEnabled) break block2;
            }
            n = 0;
        }
        this.mSubmitButton.setVisibility(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateViewsVisibility(boolean bl) {
        boolean bl2 = true;
        int n = 8;
        this.mIconified = bl;
        int n2 = bl ? 0 : 8;
        boolean bl3 = !TextUtils.isEmpty((CharSequence)this.mQueryTextView.getText());
        this.mSearchButton.setVisibility(n2);
        this.updateSubmitButton(bl3);
        View view = this.mSearchEditFrame;
        n2 = bl ? 8 : 0;
        view.setVisibility(n2);
        view = this.mSearchHintIcon;
        n2 = this.mIconifiedByDefault ? n : 0;
        view.setVisibility(n2);
        this.updateCloseButton();
        bl = !bl3 ? bl2 : false;
        this.updateVoiceButton(bl);
        this.updateSubmitArea();
    }

    private void updateVoiceButton(boolean bl) {
        int n;
        int n2 = n = 8;
        if (this.mVoiceButtonEnabled) {
            n2 = n;
            if (!this.isIconified()) {
                n2 = n;
                if (bl) {
                    n2 = 0;
                    this.mSubmitButton.setVisibility(8);
                }
            }
        }
        this.mVoiceButton.setVisibility(n2);
    }

    public void clearFocus() {
        this.mClearingFocus = true;
        this.setImeVisibility(false);
        super.clearFocus();
        this.mQueryTextView.clearFocus();
        this.mClearingFocus = false;
    }

    public int getImeOptions() {
        return this.mQueryTextView.getImeOptions();
    }

    public int getInputType() {
        return this.mQueryTextView.getInputType();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public CharSequence getQuery() {
        return this.mQueryTextView.getText();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public CharSequence getQueryHint() {
        if (this.mQueryHint != null) {
            return this.mQueryHint;
        }
        if (this.mSearchable == null) return null;
        CharSequence charSequence = null;
        int n = this.mSearchable.getHintId();
        if (n == 0) return charSequence;
        return this.getContext().getString(n);
    }

    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }

    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }

    public boolean isIconified() {
        return this.mIconified;
    }

    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }

    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }

    @Override
    public void onActionViewCollapsed() {
        this.clearFocus();
        this.updateViewsVisibility(true);
        this.mQueryTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }

    @Override
    public void onActionViewExpanded() {
        if (this.mExpandedInActionView) {
            return;
        }
        this.mExpandedInActionView = true;
        this.mCollapsedImeOptions = this.mQueryTextView.getImeOptions();
        this.mQueryTextView.setImeOptions(this.mCollapsedImeOptions | 33554432);
        this.mQueryTextView.setText((CharSequence)"");
        this.setIconified(false);
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mUpdateDrawableStateRunnable);
        this.post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (this.mSearchable == null) {
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        if (this.isIconified()) {
            super.onMeasure(n, n2);
            return;
        }
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = View.MeasureSpec.getSize((int)n);
        switch (n3) {
            default: {
                n = n4;
                break;
            }
            case Integer.MIN_VALUE: {
                if (this.mMaxWidth > 0) {
                    n = Math.min((int)this.mMaxWidth, (int)n4);
                    break;
                }
                n = Math.min((int)this.getPreferredWidth(), (int)n4);
                break;
            }
            case 1073741824: {
                n = n4;
                if (this.mMaxWidth <= 0) break;
                n = Math.min((int)this.mMaxWidth, (int)n4);
                break;
            }
            case 0: {
                n = this.mMaxWidth > 0 ? this.mMaxWidth : this.getPreferredWidth();
            }
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824), n2);
    }

    void onQueryRefine(CharSequence charSequence) {
        this.setQuery(charSequence);
    }

    void onTextFocusChanged() {
        this.updateViewsVisibility(this.isIconified());
        this.postUpdateFocusedState();
        if (this.mQueryTextView.hasFocus()) {
            this.forceSuggestionQuery();
        }
    }

    public void onWindowFocusChanged(boolean bl) {
        super.onWindowFocusChanged(bl);
        this.postUpdateFocusedState();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean requestFocus(int n, Rect rect) {
        boolean bl;
        if (this.mClearingFocus) {
            return false;
        }
        if (!this.isFocusable()) {
            return false;
        }
        if (this.isIconified()) return super.requestFocus(n, rect);
        boolean bl2 = bl = this.mQueryTextView.requestFocus(n, rect);
        if (!bl) return bl2;
        this.updateViewsVisibility(false);
        return bl;
    }

    public void setAppSearchData(Bundle bundle) {
        this.mAppSearchData = bundle;
    }

    public void setIconified(boolean bl) {
        if (bl) {
            this.onCloseClicked();
            return;
        }
        this.onSearchClicked();
    }

    public void setIconifiedByDefault(boolean bl) {
        if (this.mIconifiedByDefault == bl) {
            return;
        }
        this.mIconifiedByDefault = bl;
        this.updateViewsVisibility(bl);
        this.updateQueryHint();
    }

    public void setImeOptions(int n) {
        this.mQueryTextView.setImeOptions(n);
    }

    public void setInputType(int n) {
        this.mQueryTextView.setInputType(n);
    }

    public void setMaxWidth(int n) {
        this.mMaxWidth = n;
        this.requestLayout();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mOnQueryTextFocusChangeListener = onFocusChangeListener;
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.mOnQueryChangeListener = onQueryTextListener;
    }

    public void setOnSearchClickListener(View.OnClickListener onClickListener) {
        this.mOnSearchClickListener = onClickListener;
    }

    public void setOnSuggestionListener(OnSuggestionListener onSuggestionListener) {
        this.mOnSuggestionListener = onSuggestionListener;
    }

    public void setQuery(CharSequence charSequence, boolean bl) {
        this.mQueryTextView.setText(charSequence);
        if (charSequence != null) {
            this.mQueryTextView.setSelection(this.mQueryTextView.length());
            this.mUserQuery = charSequence;
        }
        if (bl && !TextUtils.isEmpty((CharSequence)charSequence)) {
            this.onSubmitQuery();
        }
    }

    public void setQueryHint(CharSequence charSequence) {
        this.mQueryHint = charSequence;
        this.updateQueryHint();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setQueryRefinementEnabled(boolean bl) {
        this.mQueryRefinement = bl;
        if (this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
            SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            int n = bl ? 2 : 1;
            suggestionsAdapter.setQueryRefinement(n);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        this.mSearchable = searchableInfo;
        if (this.mSearchable != null) {
            this.updateSearchAutoComplete();
            this.updateQueryHint();
        }
        this.mVoiceButtonEnabled = this.hasVoiceSearch();
        if (this.mVoiceButtonEnabled) {
            this.mQueryTextView.setPrivateImeOptions("nm");
        }
        this.updateViewsVisibility(this.isIconified());
    }

    public void setSubmitButtonEnabled(boolean bl) {
        this.mSubmitButtonEnabled = bl;
        this.updateViewsVisibility(this.isIconified());
    }

    public void setSuggestionsAdapter(CursorAdapter cursorAdapter) {
        this.mSuggestionsAdapter = cursorAdapter;
        this.mQueryTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
    }

    private static class AutoCompleteTextViewReflector {
        private Method doAfterTextChanged;
        private Method doBeforeTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        AutoCompleteTextViewReflector() {
            try {
                this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.doBeforeTextChanged.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
            try {
                this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.doAfterTextChanged.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
            try {
                this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", new Class[]{Boolean.TYPE});
                this.ensureImeVisible.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
            try {
                this.showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", new Class[]{Integer.TYPE, ResultReceiver.class});
                this.showSoftInputUnchecked.setAccessible(true);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        void doAfterTextChanged(AutoCompleteTextView autoCompleteTextView) {
            if (this.doAfterTextChanged == null) return;
            try {
                this.doAfterTextChanged.invoke((Object)autoCompleteTextView, new Object[0]);
                return;
            }
            catch (Exception exception) {
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        void doBeforeTextChanged(AutoCompleteTextView autoCompleteTextView) {
            if (this.doBeforeTextChanged == null) return;
            try {
                this.doBeforeTextChanged.invoke((Object)autoCompleteTextView, new Object[0]);
                return;
            }
            catch (Exception exception) {
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        void ensureImeVisible(AutoCompleteTextView autoCompleteTextView, boolean bl) {
            if (this.ensureImeVisible == null) return;
            try {
                this.ensureImeVisible.invoke((Object)autoCompleteTextView, new Object[]{bl});
                return;
            }
            catch (Exception exception) {
                return;
            }
        }

        void showSoftInputUnchecked(InputMethodManager inputMethodManager, View view, int n) {
            if (this.showSoftInputUnchecked != null) {
                try {
                    this.showSoftInputUnchecked.invoke((Object)inputMethodManager, new Object[]{n, null});
                    return;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            inputMethodManager.showSoftInput(view, n);
        }
    }

    public static interface OnCloseListener {
        public boolean onClose();
    }

    public static interface OnQueryTextListener {
        public boolean onQueryTextChange(String var1);

        public boolean onQueryTextSubmit(String var1);
    }

    public static interface OnSuggestionListener {
        public boolean onSuggestionClick(int var1);

        public boolean onSuggestionSelect(int var1);
    }

    public static class SearchAutoComplete
    extends AutoCompleteTextView {
        private SearchView mSearchView;
        private int mThreshold;

        public SearchAutoComplete(Context context) {
            super(context);
            this.mThreshold = this.getThreshold();
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mThreshold = this.getThreshold();
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet, int n) {
            super(context, attributeSet, n);
            this.mThreshold = this.getThreshold();
        }

        private boolean isEmpty() {
            if (TextUtils.getTrimmedLength((CharSequence)this.getText()) == 0) {
                return true;
            }
            return false;
        }

        public boolean enoughToFilter() {
            if (this.mThreshold <= 0 || super.enoughToFilter()) {
                return true;
            }
            return false;
        }

        protected void onFocusChanged(boolean bl, int n, Rect rect) {
            super.onFocusChanged(bl, n, rect);
            this.mSearchView.onTextFocusChanged();
        }

        public boolean onKeyPreIme(int n, KeyEvent keyEvent) {
            if (n == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState dispatcherState = this.getKeyDispatcherState();
                    if (dispatcherState != null) {
                        dispatcherState.startTracking(keyEvent, (Object)this);
                    }
                    return true;
                }
                if (keyEvent.getAction() == 1) {
                    KeyEvent.DispatcherState dispatcherState = this.getKeyDispatcherState();
                    if (dispatcherState != null) {
                        dispatcherState.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.mSearchView.clearFocus();
                        this.mSearchView.setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(n, keyEvent);
        }

        public void onWindowFocusChanged(boolean bl) {
            super.onWindowFocusChanged(bl);
            if (bl && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
                ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
                if (SearchView.isLandscapeMode(this.getContext())) {
                    SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        public void performCompletion() {
        }

        protected void replaceText(CharSequence charSequence) {
        }

        void setSearchView(SearchView searchView) {
            this.mSearchView = searchView;
        }

        public void setThreshold(int n) {
            super.setThreshold(n);
            this.mThreshold = n;
        }
    }

}

