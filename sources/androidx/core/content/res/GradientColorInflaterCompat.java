package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Xml;
import androidx.core.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(List<Integer> list, List<Float> list2) {
            int size = list.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = ((Integer) list.get(i)).intValue();
                this.mOffsets[i] = ((Float) list2.get(i)).floatValue();
            }
        }

        ColorStops(int i, int i2) {
            this.mColors = new int[]{i, i2};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(int i, int i2, int i3) {
            this.mColors = new int[]{i, i2, i3};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }
    }

    private GradientColorInflaterCompat() {
    }

    static Shader createFromXml(Resources resources, XmlPullParser xmlPullParser, Theme theme) throws XmlPullParserException, IOException {
        int next;
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlPullParser);
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return createFromXmlInner(resources, xmlPullParser, asAttributeSet, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    static Shader createFromXmlInner(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        String name = xmlPullParser.getName();
        if (name.equals("gradient")) {
            Theme theme2 = theme;
            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme2, attributeSet, R.styleable.GradientColor);
            float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "startX", R.styleable.GradientColor_android_startX, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "startY", R.styleable.GradientColor_android_startY, 0.0f);
            float namedFloat3 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "endX", R.styleable.GradientColor_android_endX, 0.0f);
            float namedFloat4 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "endY", R.styleable.GradientColor_android_endY, 0.0f);
            float namedFloat5 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "centerX", R.styleable.GradientColor_android_centerX, 0.0f);
            float namedFloat6 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "centerY", R.styleable.GradientColor_android_centerY, 0.0f);
            int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, PasswordLoginParams.IDENTIFIER_KEY_TYPE, R.styleable.GradientColor_android_type, 0);
            int namedColor = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, "startColor", R.styleable.GradientColor_android_startColor, 0);
            String str = "centerColor";
            boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser2, str);
            int namedColor2 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, str, R.styleable.GradientColor_android_centerColor, 0);
            int namedColor3 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, "endColor", R.styleable.GradientColor_android_endColor, 0);
            int namedInt2 = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, "tileMode", R.styleable.GradientColor_android_tileMode, 0);
            float f = namedFloat5;
            float namedFloat7 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "gradientRadius", R.styleable.GradientColor_android_gradientRadius, 0.0f);
            obtainAttributes.recycle();
            ColorStops checkColors = checkColors(inflateChildElements(resources, xmlPullParser, attributeSet, theme), namedColor, namedColor3, hasAttribute, namedColor2);
            if (namedInt == 1) {
                float f2 = f;
                if (namedFloat7 > 0.0f) {
                    int[] iArr = checkColors.mColors;
                    RadialGradient radialGradient = new RadialGradient(f2, namedFloat6, namedFloat7, iArr, checkColors.mOffsets, parseTileMode(namedInt2));
                    return radialGradient;
                }
                throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
            } else if (namedInt != 2) {
                LinearGradient linearGradient = new LinearGradient(namedFloat, namedFloat2, namedFloat3, namedFloat4, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
                return linearGradient;
            } else {
                return new SweepGradient(f, namedFloat6, checkColors.mColors, checkColors.mOffsets);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(xmlPullParser.getPositionDescription());
            sb.append(": invalid gradient color tag ");
            sb.append(name);
            throw new XmlPullParserException(sb.toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0089, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r10.toString());
     */
    private static ColorStops inflateChildElements(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) throws XmlPullParserException, IOException {
        int depth = xmlPullParser.getDepth() + 1;
        ArrayList arrayList = new ArrayList(20);
        ArrayList arrayList2 = new ArrayList(20);
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            int depth2 = xmlPullParser.getDepth();
            if (depth2 < depth && next == 3) {
                break;
            } else if (next == 2 && depth2 <= depth && xmlPullParser.getName().equals("item")) {
                TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.GradientColorItem);
                boolean hasValue = obtainAttributes.hasValue(R.styleable.GradientColorItem_android_color);
                boolean hasValue2 = obtainAttributes.hasValue(R.styleable.GradientColorItem_android_offset);
                if (!hasValue || !hasValue2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(xmlPullParser.getPositionDescription());
                    sb.append(": <item> tag requires a 'color' attribute and a 'offset' ");
                    sb.append("attribute!");
                } else {
                    int color = obtainAttributes.getColor(R.styleable.GradientColorItem_android_color, 0);
                    float f = obtainAttributes.getFloat(R.styleable.GradientColorItem_android_offset, 0.0f);
                    obtainAttributes.recycle();
                    arrayList2.add(Integer.valueOf(color));
                    arrayList.add(Float.valueOf(f));
                }
            }
        }
        if (arrayList2.size() > 0) {
            return new ColorStops((List<Integer>) arrayList2, (List<Float>) arrayList);
        }
        return null;
    }

    private static ColorStops checkColors(ColorStops colorStops, int i, int i2, boolean z, int i3) {
        if (colorStops != null) {
            return colorStops;
        }
        if (z) {
            return new ColorStops(i, i3, i2);
        }
        return new ColorStops(i, i2);
    }

    private static TileMode parseTileMode(int i) {
        if (i == 1) {
            return TileMode.REPEAT;
        }
        if (i != 2) {
            return TileMode.CLAMP;
        }
        return TileMode.MIRROR;
    }
}
