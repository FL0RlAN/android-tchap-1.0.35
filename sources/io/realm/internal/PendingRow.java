package io.realm.internal;

import io.realm.RealmChangeListener;
import io.realm.RealmFieldType;
import io.realm.internal.core.DescriptorOrdering;
import java.lang.ref.WeakReference;
import java.util.Date;

public class PendingRow implements Row {
    private static final String PROXY_NOT_SET_MESSAGE = "The 'frontEnd' has not been set.";
    private static final String QUERY_EXECUTED_MESSAGE = "The query has been executed. This 'PendingRow' is not valid anymore.";
    private static final String QUERY_NOT_RETURNED_MESSAGE = "The pending query has not been executed.";
    private WeakReference<FrontEnd> frontEndRef;
    private RealmChangeListener<PendingRow> listener = new RealmChangeListener<PendingRow>() {
        public void onChange(PendingRow pendingRow) {
            PendingRow.this.notifyFrontEnd();
        }
    };
    private OsResults pendingOsResults;
    private boolean returnCheckedRow;
    private OsSharedRealm sharedRealm;

    public interface FrontEnd {
        void onQueryFinished(Row row);
    }

    public boolean isAttached() {
        return false;
    }

    public PendingRow(OsSharedRealm osSharedRealm, TableQuery tableQuery, DescriptorOrdering descriptorOrdering, boolean z) {
        this.sharedRealm = osSharedRealm;
        this.pendingOsResults = OsResults.createFromQuery(osSharedRealm, tableQuery, descriptorOrdering);
        this.pendingOsResults.addListener(this, this.listener);
        this.returnCheckedRow = z;
        osSharedRealm.addPendingRow(this);
    }

    public void setFrontEnd(FrontEnd frontEnd) {
        this.frontEndRef = new WeakReference<>(frontEnd);
    }

    public long getColumnCount() {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public String getColumnName(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public long getColumnIndex(String str) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public RealmFieldType getColumnType(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public Table getTable() {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public long getIndex() {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public long getLong(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public boolean getBoolean(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public float getFloat(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public double getDouble(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public Date getDate(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public String getString(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public byte[] getBinaryByteArray(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public long getLink(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public boolean isNullLink(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public OsList getModelList(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public OsList getValueList(long j, RealmFieldType realmFieldType) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setLong(long j, long j2) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setBoolean(long j, boolean z) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setFloat(long j, float f) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setDouble(long j, double d) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setDate(long j, Date date) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setString(long j, String str) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setBinaryByteArray(long j, byte[] bArr) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setLink(long j, long j2) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void nullifyLink(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public boolean isNull(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void setNull(long j) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public void checkIfAttached() {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    public boolean hasColumn(String str) {
        throw new IllegalStateException(QUERY_NOT_RETURNED_MESSAGE);
    }

    private void clearPendingCollection() {
        this.pendingOsResults.removeListener(this, this.listener);
        this.pendingOsResults = null;
        this.listener = null;
        this.sharedRealm.removePendingRow(this);
    }

    /* access modifiers changed from: private */
    public void notifyFrontEnd() {
        WeakReference<FrontEnd> weakReference = this.frontEndRef;
        if (weakReference != null) {
            FrontEnd frontEnd = (FrontEnd) weakReference.get();
            if (frontEnd == null) {
                clearPendingCollection();
                return;
            }
            if (this.pendingOsResults.isValid()) {
                UncheckedRow firstUncheckedRow = this.pendingOsResults.firstUncheckedRow();
                clearPendingCollection();
                if (firstUncheckedRow != null) {
                    if (this.returnCheckedRow) {
                        firstUncheckedRow = CheckedRow.getFromRow(firstUncheckedRow);
                    }
                    frontEnd.onQueryFinished(firstUncheckedRow);
                } else {
                    frontEnd.onQueryFinished(InvalidRow.INSTANCE);
                }
            } else {
                clearPendingCollection();
            }
            return;
        }
        throw new IllegalStateException(PROXY_NOT_SET_MESSAGE);
    }

    public void executeQuery() {
        if (this.pendingOsResults != null) {
            notifyFrontEnd();
            return;
        }
        throw new IllegalStateException(QUERY_EXECUTED_MESSAGE);
    }
}
