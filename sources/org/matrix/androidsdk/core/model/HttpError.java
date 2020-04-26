package org.matrix.androidsdk.core.model;

public final class HttpError {
    private final String errorBody;
    private final int httpCode;

    public HttpError(String str, int i) {
        this.errorBody = str;
        this.httpCode = i;
    }

    public String getErrorBody() {
        return this.errorBody;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HttpError httpError = (HttpError) obj;
        if (this.httpCode != httpError.httpCode) {
            return false;
        }
        String str = this.errorBody;
        if (str != null) {
            z = str.equals(httpError.errorBody);
        } else if (httpError.errorBody != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.errorBody;
        return ((str != null ? str.hashCode() : 0) * 31) + this.httpCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HttpError{errorBody='");
        sb.append(this.errorBody);
        sb.append('\'');
        sb.append(", httpCode=");
        sb.append(this.httpCode);
        sb.append('}');
        return sb.toString();
    }
}
