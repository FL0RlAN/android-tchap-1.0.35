package okhttp3.logging;

import com.facebook.stetho.server.http.HttpHeaders;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private volatile Level level;
    private final Logger logger;

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    public interface Logger {
        public static final Logger DEFAULT = new Logger() {
            public void log(String str) {
                Platform.get().log(4, str, null);
            }
        };

        void log(String str);
    }

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(Logger logger2) {
        this.level = Level.NONE;
        this.logger = logger2;
    }

    public HttpLoggingInterceptor setLevel(Level level2) {
        if (level2 != null) {
            this.level = level2;
            return this;
        }
        throw new NullPointerException("level == null. Use Level.NONE instead.");
    }

    public Level getLevel() {
        return this.level;
    }

    /* JADX WARNING: Removed duplicated region for block: B:95:0x0334  */
    public Response intercept(Chain chain) throws IOException {
        String str;
        boolean z;
        String str2;
        long j;
        String str3;
        char c;
        String str4;
        Object obj;
        boolean z2;
        Chain chain2 = chain;
        Level level2 = this.level;
        Request request = chain.request();
        if (level2 == Level.NONE) {
            return chain2.proceed(request);
        }
        boolean z3 = true;
        boolean z4 = level2 == Level.BODY;
        boolean z5 = z4 || level2 == Level.HEADERS;
        RequestBody body = request.body();
        if (body == null) {
            z3 = false;
        }
        Connection connection = chain.connection();
        StringBuilder sb = new StringBuilder();
        sb.append("--> ");
        sb.append(request.method());
        sb.append(' ');
        sb.append(request.url());
        String str5 = "";
        if (connection != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" ");
            sb2.append(connection.protocol());
            str = sb2.toString();
        } else {
            str = str5;
        }
        sb.append(str);
        String sb3 = sb.toString();
        String str6 = "-byte body)";
        String str7 = " (";
        if (!z5 && z3) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb3);
            sb4.append(str7);
            sb4.append(body.contentLength());
            sb4.append(str6);
            sb3 = sb4.toString();
        }
        this.logger.log(sb3);
        String str8 = "-byte body omitted)";
        String str9 = ": ";
        if (z5) {
            if (z3) {
                if (body.contentType() != null) {
                    Logger logger2 = this.logger;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Content-Type: ");
                    sb5.append(body.contentType());
                    logger2.log(sb5.toString());
                }
                if (body.contentLength() != -1) {
                    Logger logger3 = this.logger;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Content-Length: ");
                    sb6.append(body.contentLength());
                    logger3.log(sb6.toString());
                }
            }
            Headers headers = request.headers();
            int size = headers.size();
            int i = 0;
            while (i < size) {
                String name = headers.name(i);
                int i2 = size;
                if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name) || HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
                    z2 = z5;
                } else {
                    Logger logger4 = this.logger;
                    z2 = z5;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(name);
                    sb7.append(str9);
                    sb7.append(headers.value(i));
                    logger4.log(sb7.toString());
                }
                i++;
                size = i2;
                z5 = z2;
            }
            z = z5;
            String str10 = "--> END ";
            if (!z4 || !z3) {
                Logger logger5 = this.logger;
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str10);
                sb8.append(request.method());
                logger5.log(sb8.toString());
            } else if (bodyHasUnknownEncoding(request.headers())) {
                Logger logger6 = this.logger;
                StringBuilder sb9 = new StringBuilder();
                sb9.append(str10);
                sb9.append(request.method());
                sb9.append(" (encoded body omitted)");
                logger6.log(sb9.toString());
            } else {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                this.logger.log(str5);
                if (isPlaintext(buffer)) {
                    this.logger.log(buffer.readString(charset));
                    Logger logger7 = this.logger;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str10);
                    sb10.append(request.method());
                    sb10.append(str7);
                    sb10.append(body.contentLength());
                    sb10.append(str6);
                    logger7.log(sb10.toString());
                } else {
                    Logger logger8 = this.logger;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(str10);
                    sb11.append(request.method());
                    sb11.append(" (binary ");
                    sb11.append(body.contentLength());
                    sb11.append(str8);
                    logger8.log(sb11.toString());
                }
            }
        } else {
            z = z5;
        }
        long nanoTime = System.nanoTime();
        try {
            Response proceed = chain2.proceed(request);
            long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoTime);
            ResponseBody body2 = proceed.body();
            long contentLength = body2.contentLength();
            if (contentLength != -1) {
                StringBuilder sb12 = new StringBuilder();
                sb12.append(contentLength);
                sb12.append("-byte");
                str2 = sb12.toString();
            } else {
                str2 = "unknown-length";
            }
            Logger logger9 = this.logger;
            StringBuilder sb13 = new StringBuilder();
            String str11 = str6;
            sb13.append("<-- ");
            sb13.append(proceed.code());
            if (proceed.message().isEmpty()) {
                j = contentLength;
                str3 = str5;
                c = ' ';
            } else {
                StringBuilder sb14 = new StringBuilder();
                j = contentLength;
                c = ' ';
                sb14.append(' ');
                sb14.append(proceed.message());
                str3 = sb14.toString();
            }
            sb13.append(str3);
            sb13.append(c);
            sb13.append(proceed.request().url());
            sb13.append(str7);
            sb13.append(millis);
            sb13.append("ms");
            if (!z) {
                StringBuilder sb15 = new StringBuilder();
                sb15.append(", ");
                sb15.append(str2);
                sb15.append(" body");
                str4 = sb15.toString();
            } else {
                str4 = str5;
            }
            sb13.append(str4);
            sb13.append(')');
            logger9.log(sb13.toString());
            if (z) {
                Headers headers2 = proceed.headers();
                int size2 = headers2.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    Logger logger10 = this.logger;
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append(headers2.name(i3));
                    sb16.append(str9);
                    sb16.append(headers2.value(i3));
                    logger10.log(sb16.toString());
                }
                if (!z4 || !okhttp3.internal.http.HttpHeaders.hasBody(proceed)) {
                    this.logger.log("<-- END HTTP");
                } else if (bodyHasUnknownEncoding(proceed.headers())) {
                    this.logger.log("<-- END HTTP (encoded body omitted)");
                } else {
                    BufferedSource source = body2.source();
                    source.request(LongCompanionObject.MAX_VALUE);
                    Buffer buffer2 = source.buffer();
                    GzipSource gzipSource = null;
                    if ("gzip".equalsIgnoreCase(headers2.get("Content-Encoding"))) {
                        obj = Long.valueOf(buffer2.size());
                        try {
                            GzipSource gzipSource2 = new GzipSource(buffer2.clone());
                            try {
                                buffer2 = new Buffer();
                                buffer2.writeAll(gzipSource2);
                                gzipSource2.close();
                            } catch (Throwable th) {
                                th = th;
                                gzipSource = gzipSource2;
                                if (gzipSource != null) {
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (gzipSource != null) {
                                gzipSource.close();
                            }
                            throw th;
                        }
                    } else {
                        obj = null;
                    }
                    Charset charset2 = UTF8;
                    MediaType contentType2 = body2.contentType();
                    if (contentType2 != null) {
                        charset2 = contentType2.charset(UTF8);
                    }
                    if (!isPlaintext(buffer2)) {
                        this.logger.log(str5);
                        Logger logger11 = this.logger;
                        StringBuilder sb17 = new StringBuilder();
                        sb17.append("<-- END HTTP (binary ");
                        sb17.append(buffer2.size());
                        sb17.append(str8);
                        logger11.log(sb17.toString());
                        return proceed;
                    }
                    if (j != 0) {
                        this.logger.log(str5);
                        this.logger.log(buffer2.clone().readString(charset2));
                    }
                    String str12 = "<-- END HTTP (";
                    if (obj != null) {
                        Logger logger12 = this.logger;
                        StringBuilder sb18 = new StringBuilder();
                        sb18.append(str12);
                        sb18.append(buffer2.size());
                        sb18.append("-byte, ");
                        sb18.append(obj);
                        sb18.append("-gzipped-byte body)");
                        logger12.log(sb18.toString());
                    } else {
                        Logger logger13 = this.logger;
                        StringBuilder sb19 = new StringBuilder();
                        sb19.append(str12);
                        sb19.append(buffer2.size());
                        sb19.append(str11);
                        logger13.log(sb19.toString());
                    }
                }
            }
            return proceed;
        } catch (Exception e) {
            Exception exc = e;
            Logger logger14 = this.logger;
            StringBuilder sb20 = new StringBuilder();
            sb20.append("<-- HTTP FAILED: ");
            sb20.append(exc);
            logger14.log(sb20.toString());
            throw exc;
        }
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer buffer2 = new Buffer();
            buffer.copyTo(buffer2, 0, buffer.size() < 64 ? buffer.size() : 64);
            int i = 0;
            while (true) {
                if (i >= 16) {
                    break;
                } else if (buffer2.exhausted()) {
                    break;
                } else {
                    int readUtf8CodePoint = buffer2.readUtf8CodePoint();
                    if (Character.isISOControl(readUtf8CodePoint) && !Character.isWhitespace(readUtf8CodePoint)) {
                        return false;
                    }
                    i++;
                }
            }
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String str = headers.get("Content-Encoding");
        return str != null && !str.equalsIgnoreCase("identity") && !str.equalsIgnoreCase("gzip");
    }
}
