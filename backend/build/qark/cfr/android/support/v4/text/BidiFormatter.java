/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Locale
 */
package android.support.v4.text;

import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.text.TextUtilsCompat;
import java.util.Locale;

public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    private static final BidiFormatter DEFAULT_LTR_INSTANCE;
    private static final BidiFormatter DEFAULT_RTL_INSTANCE;
    private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '\u202a';
    private static final char LRM = '\u200e';
    private static final String LRM_STRING;
    private static final char PDF = '\u202c';
    private static final char RLE = '\u202b';
    private static final char RLM = '\u200f';
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    static {
        DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString((char)'\u200e');
        RLM_STRING = Character.toString((char)'\u200f');
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }

    private BidiFormatter(boolean bl, int n, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        this.mIsRtlContext = bl;
        this.mFlags = n;
        this.mDefaultTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
    }

    private static int getEntryDir(String string2) {
        return new DirectionalityEstimator(string2, false).getEntryDir();
    }

    private static int getExitDir(String string2) {
        return new DirectionalityEstimator(string2, false).getExitDir();
    }

    public static BidiFormatter getInstance() {
        return new Builder().build();
    }

    public static BidiFormatter getInstance(Locale locale) {
        return new Builder(locale).build();
    }

    public static BidiFormatter getInstance(boolean bl) {
        return new Builder(bl).build();
    }

    private static boolean isRtlLocale(Locale locale) {
        if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1) {
            return true;
        }
        return false;
    }

    private String markAfter(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean bl = textDirectionHeuristicCompat.isRtl(string2, 0, string2.length());
        if (!this.mIsRtlContext && (bl || BidiFormatter.getExitDir(string2) == 1)) {
            return LRM_STRING;
        }
        if (this.mIsRtlContext && (!bl || BidiFormatter.getExitDir(string2) == -1)) {
            return RLM_STRING;
        }
        return "";
    }

    private String markBefore(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        boolean bl = textDirectionHeuristicCompat.isRtl(string2, 0, string2.length());
        if (!this.mIsRtlContext && (bl || BidiFormatter.getEntryDir(string2) == 1)) {
            return LRM_STRING;
        }
        if (this.mIsRtlContext && (!bl || BidiFormatter.getEntryDir(string2) == -1)) {
            return RLM_STRING;
        }
        return "";
    }

    public boolean getStereoReset() {
        if ((this.mFlags & 2) != 0) {
            return true;
        }
        return false;
    }

    public boolean isRtl(String string2) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(string2, 0, string2.length());
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public String unicodeWrap(String string2) {
        return this.unicodeWrap(string2, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(string2, textDirectionHeuristicCompat, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String unicodeWrap(String string2, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean bl) {
        boolean bl2 = textDirectionHeuristicCompat.isRtl(string2, 0, string2.length());
        StringBuilder stringBuilder = new StringBuilder();
        if (this.getStereoReset() && bl) {
            textDirectionHeuristicCompat = bl2 ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR;
            stringBuilder.append(this.markBefore(string2, textDirectionHeuristicCompat));
        }
        if (bl2 != this.mIsRtlContext) {
            char c = bl2 ? '\u202b' : '\u202a';
            stringBuilder.append(c);
            stringBuilder.append(string2);
            stringBuilder.append('\u202c');
        } else {
            stringBuilder.append(string2);
        }
        if (bl) {
            textDirectionHeuristicCompat = bl2 ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR;
            stringBuilder.append(this.markAfter(string2, textDirectionHeuristicCompat));
        }
        return stringBuilder.toString();
    }

    public String unicodeWrap(String string2, boolean bl) {
        return this.unicodeWrap(string2, this.mDefaultTextDirectionHeuristicCompat, bl);
    }

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(Locale locale) {
            this.initialize(BidiFormatter.isRtlLocale(locale));
        }

        public Builder(boolean bl) {
            this.initialize(bl);
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean bl) {
            if (bl) {
                return DEFAULT_RTL_INSTANCE;
            }
            return DEFAULT_LTR_INSTANCE;
        }

        private void initialize(boolean bl) {
            this.mIsRtlContext = bl;
            this.mTextDirectionHeuristicCompat = DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return Builder.getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
            this.mTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
            return this;
        }

        public Builder stereoReset(boolean bl) {
            if (bl) {
                this.mFlags |= 2;
                return this;
            }
            this.mFlags &= -3;
            return this;
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[1792];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final String text;

        static {
            for (int i = 0; i < 1792; ++i) {
                DirectionalityEstimator.DIR_TYPE_CACHE[i] = Character.getDirectionality((int)i);
            }
        }

        DirectionalityEstimator(String string2, boolean bl) {
            this.text = string2;
            this.isHtml = bl;
            this.length = string2.length();
        }

        private static byte getCachedDirectionality(char c) {
            if (c < '\u0700') {
                return DIR_TYPE_CACHE[c];
            }
            return Character.getDirectionality((char)c);
        }

        private byte skipEntityBackward() {
            int n = this.charIndex;
            while (this.charIndex > 0) {
                int n2;
                String string2 = this.text;
                this.charIndex = n2 = this.charIndex - 1;
                this.lastChar = string2.charAt(n2);
                if (this.lastChar == '&') {
                    return 12;
                }
                if (this.lastChar != ';') continue;
            }
            this.charIndex = n;
            this.lastChar = (char)59;
            return 13;
        }

        private byte skipEntityForward() {
            while (this.charIndex < this.length) {
                char c;
                String string2 = this.text;
                int n = this.charIndex;
                this.charIndex = n + 1;
                this.lastChar = c = string2.charAt(n);
                if (c != ';') continue;
            }
            return 12;
        }

        /*
         * Enabled aggressive block sorting
         */
        private byte skipTagBackward() {
            int n = this.charIndex;
            block0 : while (this.charIndex > 0) {
                int n2;
                String string2 = this.text;
                this.charIndex = n2 = this.charIndex - 1;
                this.lastChar = string2.charAt(n2);
                if (this.lastChar == '<') {
                    return 12;
                }
                if (this.lastChar == '>') break;
                if (this.lastChar != '\"' && this.lastChar != '\'') continue;
                n2 = this.lastChar;
                while (this.charIndex > 0) {
                    char c;
                    int n3;
                    string2 = this.text;
                    this.charIndex = n3 = this.charIndex - 1;
                    this.lastChar = c = string2.charAt(n3);
                    if (c == n2) continue block0;
                }
            }
            this.charIndex = n;
            this.lastChar = (char)62;
            return 13;
        }

        private byte skipTagForward() {
            int n = this.charIndex;
            block0 : while (this.charIndex < this.length) {
                String string2 = this.text;
                int n2 = this.charIndex;
                this.charIndex = n2 + 1;
                this.lastChar = string2.charAt(n2);
                if (this.lastChar == '>') {
                    return 12;
                }
                if (this.lastChar != '\"' && this.lastChar != '\'') continue;
                n2 = this.lastChar;
                while (this.charIndex < this.length) {
                    char c;
                    string2 = this.text;
                    int n3 = this.charIndex;
                    this.charIndex = n3 + 1;
                    this.lastChar = c = string2.charAt(n3);
                    if (c == n2) continue block0;
                }
            }
            this.charIndex = n;
            this.lastChar = (char)60;
            return 13;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        byte dirTypeBackward() {
            byte by;
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate((char)this.lastChar)) {
                int n = Character.codePointBefore((CharSequence)this.text, (int)this.charIndex);
                this.charIndex -= Character.charCount((int)n);
                return Character.getDirectionality((int)n);
            }
            --this.charIndex;
            byte by2 = by = DirectionalityEstimator.getCachedDirectionality(this.lastChar);
            if (!this.isHtml) return by2;
            if (this.lastChar == '>') {
                return this.skipTagBackward();
            }
            by2 = by;
            if (this.lastChar != ';') return by2;
            return this.skipEntityBackward();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        byte dirTypeForward() {
            byte by;
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate((char)this.lastChar)) {
                int n = Character.codePointAt((CharSequence)this.text, (int)this.charIndex);
                this.charIndex += Character.charCount((int)n);
                return Character.getDirectionality((int)n);
            }
            ++this.charIndex;
            byte by2 = by = DirectionalityEstimator.getCachedDirectionality(this.lastChar);
            if (!this.isHtml) return by2;
            if (this.lastChar == '<') {
                return this.skipTagForward();
            }
            by2 = by;
            if (this.lastChar != '&') return by2;
            return this.skipEntityForward();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        int getEntryDir() {
            int n;
            int n2;
            int n3;
            block21 : {
                this.charIndex = 0;
                n = 0;
                n2 = 0;
                n3 = 0;
                block13 : while (this.charIndex < this.length) {
                    if (n3 == 0) {
                        switch (this.dirTypeForward()) {
                            case 9: {
                                continue block13;
                            }
                            default: {
                                n3 = n;
                                continue block13;
                            }
                            case 14: 
                            case 15: {
                                ++n;
                                n2 = -1;
                                continue block13;
                            }
                            case 16: 
                            case 17: {
                                ++n;
                                n2 = 1;
                                continue block13;
                            }
                            case 18: {
                                --n;
                                n2 = 0;
                                continue block13;
                            }
                            case 0: {
                                if (n == 0) {
                                    return -1;
                                }
                                n3 = n;
                                continue block13;
                            }
                            case 1: 
                            case 2: 
                        }
                        if (n == 0) {
                            return 1;
                        }
                        n3 = n;
                        continue;
                    }
                    break block21;
                }
                return 0;
            }
            if (n3 == 0) {
                return 0;
            }
            int n4 = n2;
            if (n2 != 0) return n4;
            block14 : while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    default: {
                        continue block14;
                    }
                    case 14: 
                    case 15: {
                        if (n3 == n) {
                            return -1;
                        }
                        --n;
                        continue block14;
                    }
                    case 16: 
                    case 17: {
                        if (n3 == n) {
                            return 1;
                        }
                        --n;
                        continue block14;
                    }
                    case 18: 
                }
                ++n;
            }
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         */
        int getExitDir() {
            this.charIndex = this.length;
            int n = 0;
            int n2 = 0;
            block8 : while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    case 9: {
                        continue block8;
                    }
                    default: {
                        if (n2 != 0) continue block8;
                        n2 = n;
                        continue block8;
                    }
                    case 0: {
                        if (n == 0) return -1;
                        {
                            if (n2 != 0) continue block8;
                            n2 = n;
                            continue block8;
                        }
                    }
                    case 14: 
                    case 15: {
                        if (n2 == n) {
                            return -1;
                        }
                        --n;
                        continue block8;
                    }
                    case 1: 
                    case 2: {
                        if (n == 0) {
                            return 1;
                        }
                        if (n2 != 0) continue block8;
                        n2 = n;
                        continue block8;
                    }
                    case 16: 
                    case 17: {
                        if (n2 == n) {
                            return 1;
                        }
                        --n;
                        continue block8;
                    }
                    case 18: 
                }
                ++n;
            }
            return 0;
        }
    }

}

