package im.vector.util;

import com.facebook.stetho.server.http.HttpHeaders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kotlin.text.Typography;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public class BugReporterMultipartBody extends RequestBody {
    private static final byte[] COLONSPACE = {58, 32};
    private static final byte[] CRLF = {13, 10};
    private static final byte[] DASHDASH = {Framer.STDIN_FRAME_PREFIX, Framer.STDIN_FRAME_PREFIX};
    private static final MediaType FORM = MediaType.parse("multipart/form-data");
    private final ByteString mBoundary;
    private long mContentLength;
    private List<Long> mContentLengthSize;
    private final MediaType mContentType;
    private final List<Part> mParts;
    private WriteListener mWriteListener;

    public static final class Builder {
        private final ByteString boundary;
        private final List<Part> parts;

        public Builder() {
            this(UUID.randomUUID().toString());
        }

        public Builder(String str) {
            this.parts = new ArrayList();
            this.boundary = ByteString.encodeUtf8(str);
        }

        public Builder addFormDataPart(String str, String str2) {
            return addPart(Part.createFormData(str, str2));
        }

        public Builder addFormDataPart(String str, String str2, RequestBody requestBody) {
            return addPart(Part.createFormData(str, str2, requestBody));
        }

        public Builder addPart(Part part) {
            if (part != null) {
                this.parts.add(part);
                return this;
            }
            throw new NullPointerException("part == null");
        }

        public BugReporterMultipartBody build() {
            if (!this.parts.isEmpty()) {
                return new BugReporterMultipartBody(this.boundary, this.parts);
            }
            throw new IllegalStateException("Multipart body must have at least one part.");
        }
    }

    public static final class Part {
        final RequestBody body;
        final Headers headers;

        public static Part create(Headers headers2, RequestBody requestBody) {
            if (requestBody == null) {
                throw new NullPointerException("body == null");
            } else if (headers2 != null && headers2.get(HttpHeaders.CONTENT_TYPE) != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Type");
            } else if (headers2 == null || headers2.get(HttpHeaders.CONTENT_LENGTH) == null) {
                return new Part(headers2, requestBody);
            } else {
                throw new IllegalArgumentException("Unexpected header: Content-Length");
            }
        }

        public static Part createFormData(String str, String str2) {
            return createFormData(str, null, RequestBody.create((MediaType) null, str2));
        }

        public static Part createFormData(String str, String str2, RequestBody requestBody) {
            if (str != null) {
                StringBuilder sb = new StringBuilder("form-data; name=");
                BugReporterMultipartBody.appendQuotedString(sb, str);
                if (str2 != null) {
                    sb.append("; filename=");
                    BugReporterMultipartBody.appendQuotedString(sb, str2);
                }
                return create(Headers.of("Content-Disposition", sb.toString()), requestBody);
            }
            throw new NullPointerException("name == null");
        }

        private Part(Headers headers2, RequestBody requestBody) {
            this.headers = headers2;
            this.body = requestBody;
        }
    }

    public interface WriteListener {
        void onWrite(long j, long j2);
    }

    private BugReporterMultipartBody(ByteString byteString, List<Part> list) {
        this.mContentLength = -1;
        this.mContentLengthSize = null;
        this.mBoundary = byteString;
        StringBuilder sb = new StringBuilder();
        sb.append(FORM);
        sb.append("; boundary=");
        sb.append(byteString.utf8());
        this.mContentType = MediaType.parse(sb.toString());
        this.mParts = Util.immutableList(list);
    }

    public MediaType contentType() {
        return this.mContentType;
    }

    public long contentLength() throws IOException {
        long j = this.mContentLength;
        if (j != -1) {
            return j;
        }
        long writeOrCountBytes = writeOrCountBytes(null, true);
        this.mContentLength = writeOrCountBytes;
        return writeOrCountBytes;
    }

    public void writeTo(BufferedSink bufferedSink) throws IOException {
        writeOrCountBytes(bufferedSink, false);
    }

    public void setWriteListener(WriteListener writeListener) {
        this.mWriteListener = writeListener;
    }

    private void onWrite(long j) {
        WriteListener writeListener = this.mWriteListener;
        if (writeListener != null) {
            long j2 = this.mContentLength;
            if (j2 > 0) {
                writeListener.onWrite(j, j2);
            }
        }
    }

    /* JADX WARNING: type inference failed for: r13v1, types: [okio.BufferedSink] */
    /* JADX WARNING: type inference failed for: r0v1 */
    /* JADX WARNING: type inference failed for: r13v4, types: [okio.Buffer] */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 3 */
    private long writeOrCountBytes(BufferedSink bufferedSink, boolean z) throws IOException {
        Buffer buffer;
        if (z) {
            r13 = new Buffer();
            this.mContentLengthSize = new ArrayList();
            buffer = r13;
            r13 = r13;
        } else {
            r13 = bufferedSink;
            buffer = 0;
        }
        int size = this.mParts.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            Part part = (Part) this.mParts.get(i);
            Headers headers = part.headers;
            RequestBody requestBody = part.body;
            r13.write(DASHDASH);
            r13.write(this.mBoundary);
            r13.write(CRLF);
            if (headers != null) {
                int size2 = headers.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    r13.writeUtf8(headers.name(i2)).write(COLONSPACE).writeUtf8(headers.value(i2)).write(CRLF);
                }
            }
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                r13.writeUtf8("Content-Type: ").writeUtf8(contentType.toString()).write(CRLF);
            }
            int contentLength = (int) requestBody.contentLength();
            if (contentLength != -1) {
                BufferedSink writeUtf8 = r13.writeUtf8("Content-Length: ");
                StringBuilder sb = new StringBuilder();
                sb.append(contentLength);
                sb.append("");
                writeUtf8.writeUtf8(sb.toString()).write(CRLF);
            } else if (z) {
                buffer.clear();
                return -1;
            }
            r13.write(CRLF);
            if (z) {
                j += (long) contentLength;
                this.mContentLengthSize.add(Long.valueOf(j));
            } else {
                requestBody.writeTo(r13);
                List<Long> list = this.mContentLengthSize;
                if (list != null && i < list.size()) {
                    onWrite(((Long) this.mContentLengthSize.get(i)).longValue());
                }
            }
            r13.write(CRLF);
        }
        r13.write(DASHDASH);
        r13.write(this.mBoundary);
        r13.write(DASHDASH);
        r13.write(CRLF);
        if (z) {
            j += buffer.size();
            buffer.clear();
        }
        return j;
    }

    /* access modifiers changed from: private */
    public static void appendQuotedString(StringBuilder sb, String str) {
        sb.append(Typography.quote);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == 10) {
                sb.append("%0A");
            } else if (charAt == 13) {
                sb.append("%0D");
            } else if (charAt != '\"') {
                sb.append(charAt);
            } else {
                sb.append("%22");
            }
        }
        sb.append(Typography.quote);
    }
}
