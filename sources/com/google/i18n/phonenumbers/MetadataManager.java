package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadataCollection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

final class MetadataManager {
    private static final String ALTERNATE_FORMATS_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberAlternateFormatsProto";
    static final MetadataLoader DEFAULT_METADATA_LOADER = new MetadataLoader() {
        public InputStream loadMetadata(String str) {
            return MetadataManager.class.getResourceAsStream(str);
        }
    };
    static final String MULTI_FILE_PHONE_NUMBER_METADATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberMetadataProto";
    private static final String SHORT_NUMBER_METADATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/ShortNumberMetadataProto";
    static final String SINGLE_FILE_PHONE_NUMBER_METADATA_FILE_NAME = "/com/google/i18n/phonenumbers/data/SingleFilePhoneNumberMetadataProto";
    private static final Set<Integer> alternateFormatsCountryCodes = AlternateFormatsCountryCodeSet.getCountryCodeSet();
    private static final ConcurrentHashMap<Integer, PhoneMetadata> alternateFormatsMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(MetadataManager.class.getName());
    private static final ConcurrentHashMap<String, PhoneMetadata> shortNumberMetadataMap = new ConcurrentHashMap<>();
    private static final Set<String> shortNumberMetadataRegionCodes = ShortNumbersRegionCodeSet.getRegionCodeSet();

    static class SingleFileMetadataMaps {
        private final Map<Integer, PhoneMetadata> countryCallingCodeToMetadata;
        private final Map<String, PhoneMetadata> regionCodeToMetadata;

        static SingleFileMetadataMaps load(String str, MetadataLoader metadataLoader) {
            List<PhoneMetadata> access$000 = MetadataManager.getMetadataFromSingleFileName(str, metadataLoader);
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            for (PhoneMetadata phoneMetadata : access$000) {
                String id = phoneMetadata.getId();
                if (PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY.equals(id)) {
                    hashMap2.put(Integer.valueOf(phoneMetadata.getCountryCode()), phoneMetadata);
                } else {
                    hashMap.put(id, phoneMetadata);
                }
            }
            return new SingleFileMetadataMaps(hashMap, hashMap2);
        }

        private SingleFileMetadataMaps(Map<String, PhoneMetadata> map, Map<Integer, PhoneMetadata> map2) {
            this.regionCodeToMetadata = Collections.unmodifiableMap(map);
            this.countryCallingCodeToMetadata = Collections.unmodifiableMap(map2);
        }

        /* access modifiers changed from: 0000 */
        public PhoneMetadata get(String str) {
            return (PhoneMetadata) this.regionCodeToMetadata.get(str);
        }

        /* access modifiers changed from: 0000 */
        public PhoneMetadata get(int i) {
            return (PhoneMetadata) this.countryCallingCodeToMetadata.get(Integer.valueOf(i));
        }
    }

    private MetadataManager() {
    }

    static PhoneMetadata getAlternateFormatsForCountry(int i) {
        if (!alternateFormatsCountryCodes.contains(Integer.valueOf(i))) {
            return null;
        }
        return getMetadataFromMultiFilePrefix(Integer.valueOf(i), alternateFormatsMap, ALTERNATE_FORMATS_FILE_PREFIX, DEFAULT_METADATA_LOADER);
    }

    static PhoneMetadata getShortNumberMetadataForRegion(String str) {
        if (!shortNumberMetadataRegionCodes.contains(str)) {
            return null;
        }
        return getMetadataFromMultiFilePrefix(str, shortNumberMetadataMap, SHORT_NUMBER_METADATA_FILE_PREFIX, DEFAULT_METADATA_LOADER);
    }

    static Set<String> getSupportedShortNumberRegions() {
        return Collections.unmodifiableSet(shortNumberMetadataRegionCodes);
    }

    static <T> PhoneMetadata getMetadataFromMultiFilePrefix(T t, ConcurrentHashMap<T, PhoneMetadata> concurrentHashMap, String str, MetadataLoader metadataLoader) {
        PhoneMetadata phoneMetadata = (PhoneMetadata) concurrentHashMap.get(t);
        if (phoneMetadata != null) {
            return phoneMetadata;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_");
        sb.append(t);
        String sb2 = sb.toString();
        List metadataFromSingleFileName = getMetadataFromSingleFileName(sb2, metadataLoader);
        if (metadataFromSingleFileName.size() > 1) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("more than one metadata in file ");
            sb3.append(sb2);
            logger2.log(level, sb3.toString());
        }
        PhoneMetadata phoneMetadata2 = (PhoneMetadata) metadataFromSingleFileName.get(0);
        PhoneMetadata phoneMetadata3 = (PhoneMetadata) concurrentHashMap.putIfAbsent(t, phoneMetadata2);
        if (phoneMetadata3 == null) {
            phoneMetadata3 = phoneMetadata2;
        }
        return phoneMetadata3;
    }

    static SingleFileMetadataMaps getSingleFileMetadataMaps(AtomicReference<SingleFileMetadataMaps> atomicReference, String str, MetadataLoader metadataLoader) {
        SingleFileMetadataMaps singleFileMetadataMaps = (SingleFileMetadataMaps) atomicReference.get();
        if (singleFileMetadataMaps != null) {
            return singleFileMetadataMaps;
        }
        atomicReference.compareAndSet(null, SingleFileMetadataMaps.load(str, metadataLoader));
        return (SingleFileMetadataMaps) atomicReference.get();
    }

    /* access modifiers changed from: private */
    public static List<PhoneMetadata> getMetadataFromSingleFileName(String str, MetadataLoader metadataLoader) {
        InputStream loadMetadata = metadataLoader.loadMetadata(str);
        if (loadMetadata != null) {
            List<PhoneMetadata> metadataList = loadMetadataAndCloseInput(loadMetadata).getMetadataList();
            if (metadataList.size() != 0) {
                return metadataList;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("empty metadata: ");
            sb.append(str);
            throw new IllegalStateException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("missing metadata: ");
        sb2.append(str);
        throw new IllegalStateException(sb2.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0034 A[SYNTHETIC, Splitter:B:24:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0038 A[Catch:{ IOException -> 0x003c }] */
    private static PhoneMetadataCollection loadMetadataAndCloseInput(InputStream inputStream) {
        ObjectInputStream objectInputStream;
        String str = "error closing input stream (ignored)";
        String str2 = "cannot load/parse metadata";
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            try {
                PhoneMetadataCollection phoneMetadataCollection = new PhoneMetadataCollection();
                phoneMetadataCollection.readExternal(objectInputStream);
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, str, e);
                }
                return phoneMetadataCollection;
            } catch (IOException e2) {
                throw new RuntimeException(str2, e2);
            } catch (Throwable th) {
                th = th;
                if (objectInputStream == null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e3) {
                        logger.log(Level.WARNING, str, e3);
                    }
                } else {
                    inputStream.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            throw new RuntimeException(str2, e4);
        } catch (Throwable th2) {
            th = th2;
            objectInputStream = null;
            if (objectInputStream == null) {
            }
            throw th;
        }
    }
}
