package org.matrix.androidsdk.interfaces;

import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;

public interface HtmlToolbox {
    String convert(String str);

    ImageGetter getImageGetter();

    TagHandler getTagHandler(String str);
}
