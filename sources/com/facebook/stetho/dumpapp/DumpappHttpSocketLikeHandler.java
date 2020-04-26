package com.facebook.stetho.dumpapp;

import com.facebook.stetho.server.SocketLike;
import com.facebook.stetho.server.SocketLikeHandler;
import com.facebook.stetho.server.http.ExactPathMatcher;
import com.facebook.stetho.server.http.HandlerRegistry;
import com.facebook.stetho.server.http.HttpHandler;
import com.facebook.stetho.server.http.LightHttpBody;
import com.facebook.stetho.server.http.LightHttpRequest;
import com.facebook.stetho.server.http.LightHttpResponse;
import com.facebook.stetho.server.http.LightHttpServer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Deprecated
public class DumpappHttpSocketLikeHandler implements SocketLikeHandler {
    private final LightHttpServer mServer;

    private static class DumpappLegacyHttpHandler implements HttpHandler {
        private static final String CONTENT_TYPE = "application/octet-stream";
        private static final String QUERY_PARAM_ARGV = "argv";
        private static final String RESPONSE_HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        private final Dumper mDumper;

        public DumpappLegacyHttpHandler(Dumper dumper) {
            this.mDumper = dumper;
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x005d  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0060  */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x0081  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0093  */
        public boolean handleRequest(SocketLike socketLike, LightHttpRequest lightHttpRequest, LightHttpResponse lightHttpResponse) throws IOException {
            boolean z;
            boolean equals = "POST".equals(lightHttpRequest.method);
            if (!equals) {
                if ("GET".equals(lightHttpRequest.method)) {
                    z = true;
                    if (!z || equals) {
                        List queryParameters = lightHttpRequest.uri.getQueryParameters(QUERY_PARAM_ARGV);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        Framer framer = new Framer(new ByteArrayInputStream(new byte[0]), byteArrayOutputStream);
                        String str = !equals ? "ERROR" : "WARNING";
                        PrintStream stderr = framer.getStderr();
                        StringBuilder sb = new StringBuilder();
                        sb.append("*** ");
                        sb.append(str);
                        sb.append(": Using legacy HTTP protocol; update dumpapp script! ***");
                        stderr.println(sb.toString());
                        if (!z) {
                            DumpappSocketLikeHandler.dump(this.mDumper, framer, (String[]) queryParameters.toArray(new String[queryParameters.size()]));
                        } else {
                            framer.writeExitCode(1);
                        }
                        lightHttpResponse.code = 200;
                        lightHttpResponse.reasonPhrase = "OK";
                        lightHttpResponse.addHeader(RESPONSE_HEADER_ALLOW_ORIGIN, "*");
                        lightHttpResponse.body = LightHttpBody.create(byteArrayOutputStream.toByteArray(), CONTENT_TYPE);
                    } else {
                        lightHttpResponse.code = 501;
                        lightHttpResponse.reasonPhrase = "Not implemented";
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(lightHttpRequest.method);
                        sb2.append(" not implemented");
                        lightHttpResponse.body = LightHttpBody.create(sb2.toString(), "text/plain");
                    }
                    return true;
                }
            }
            z = false;
            if (!z) {
            }
            List queryParameters2 = lightHttpRequest.uri.getQueryParameters(QUERY_PARAM_ARGV);
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            Framer framer2 = new Framer(new ByteArrayInputStream(new byte[0]), byteArrayOutputStream2);
            if (!equals) {
            }
            PrintStream stderr2 = framer2.getStderr();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("*** ");
            sb3.append(str);
            sb3.append(": Using legacy HTTP protocol; update dumpapp script! ***");
            stderr2.println(sb3.toString());
            if (!z) {
            }
            lightHttpResponse.code = 200;
            lightHttpResponse.reasonPhrase = "OK";
            lightHttpResponse.addHeader(RESPONSE_HEADER_ALLOW_ORIGIN, "*");
            lightHttpResponse.body = LightHttpBody.create(byteArrayOutputStream2.toByteArray(), CONTENT_TYPE);
            return true;
        }
    }

    public DumpappHttpSocketLikeHandler(Dumper dumper) {
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        handlerRegistry.register(new ExactPathMatcher("/dumpapp"), new DumpappLegacyHttpHandler(dumper));
        this.mServer = new LightHttpServer(handlerRegistry);
    }

    public void onAccepted(SocketLike socketLike) throws IOException {
        this.mServer.serve(socketLike);
    }
}
