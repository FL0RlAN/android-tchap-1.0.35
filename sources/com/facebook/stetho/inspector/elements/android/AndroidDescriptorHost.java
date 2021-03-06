package com.facebook.stetho.inspector.elements.android;

import com.facebook.stetho.inspector.elements.Descriptor.Host;
import javax.annotation.Nullable;

interface AndroidDescriptorHost extends Host {
    @Nullable
    HighlightableDescriptor getHighlightableDescriptor(@Nullable Object obj);
}
