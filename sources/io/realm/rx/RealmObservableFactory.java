package io.realm.rx;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposables;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.ObjectChangeSet;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.IdentityHashMap;
import java.util.Map;

public class RealmObservableFactory implements RxObservableFactory {
    private static final BackpressureStrategy BACK_PRESSURE_STRATEGY = BackpressureStrategy.LATEST;
    /* access modifiers changed from: private */
    public ThreadLocal<StrongReferenceCounter<RealmList>> listRefs = new ThreadLocal<StrongReferenceCounter<RealmList>>() {
        /* access modifiers changed from: protected */
        public StrongReferenceCounter<RealmList> initialValue() {
            return new StrongReferenceCounter<>();
        }
    };
    /* access modifiers changed from: private */
    public ThreadLocal<StrongReferenceCounter<RealmModel>> objectRefs = new ThreadLocal<StrongReferenceCounter<RealmModel>>() {
        /* access modifiers changed from: protected */
        public StrongReferenceCounter<RealmModel> initialValue() {
            return new StrongReferenceCounter<>();
        }
    };
    /* access modifiers changed from: private */
    public ThreadLocal<StrongReferenceCounter<RealmResults>> resultsRefs = new ThreadLocal<StrongReferenceCounter<RealmResults>>() {
        /* access modifiers changed from: protected */
        public StrongReferenceCounter<RealmResults> initialValue() {
            return new StrongReferenceCounter<>();
        }
    };

    private static class StrongReferenceCounter<K> {
        private final Map<K, Integer> references;

        private StrongReferenceCounter() {
            this.references = new IdentityHashMap();
        }

        public void acquireReference(K k) {
            Integer num = (Integer) this.references.get(k);
            if (num == null) {
                this.references.put(k, Integer.valueOf(1));
            } else {
                this.references.put(k, Integer.valueOf(num.intValue() + 1));
            }
        }

        public void releaseReference(K k) {
            Integer num = (Integer) this.references.get(k);
            if (num == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Object does not have any references: ");
                sb.append(k);
                throw new IllegalStateException(sb.toString());
            } else if (num.intValue() > 1) {
                this.references.put(k, Integer.valueOf(num.intValue() - 1));
            } else if (num.intValue() == 1) {
                this.references.remove(k);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid reference count: ");
                sb2.append(num);
                throw new IllegalStateException(sb2.toString());
            }
        }
    }

    public int hashCode() {
        return 37;
    }

    public Flowable<Realm> from(Realm realm) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<Realm>() {
            public void subscribe(final FlowableEmitter<Realm> flowableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                final AnonymousClass1 r1 = new RealmChangeListener<Realm>() {
                    public void onChange(Realm realm) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(realm);
                        }
                    }
                };
                instance.addChangeListener(r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        instance.removeChangeListener(r1);
                        instance.close();
                    }
                }));
                flowableEmitter.onNext(instance);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public Flowable<DynamicRealm> from(DynamicRealm dynamicRealm) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<DynamicRealm>() {
            public void subscribe(final FlowableEmitter<DynamicRealm> flowableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                final AnonymousClass1 r1 = new RealmChangeListener<DynamicRealm>() {
                    public void onChange(DynamicRealm dynamicRealm) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(dynamicRealm);
                        }
                    }
                };
                instance.addChangeListener(r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        instance.removeChangeListener(r1);
                        instance.close();
                    }
                }));
                flowableEmitter.onNext(instance);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E> Flowable<RealmResults<E>> from(Realm realm, final RealmResults<E> realmResults) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<RealmResults<E>>() {
            public void subscribe(final FlowableEmitter<RealmResults<E>> flowableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).acquireReference(realmResults);
                final AnonymousClass1 r1 = new RealmChangeListener<RealmResults<E>>() {
                    public void onChange(RealmResults<E> realmResults) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(realmResults);
                        }
                    }
                };
                realmResults.addChangeListener((RealmChangeListener<RealmResults<E>>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmResults.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).releaseReference(realmResults);
                    }
                }));
                flowableEmitter.onNext(realmResults);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E> Observable<CollectionChange<RealmResults<E>>> changesetsFrom(Realm realm, final RealmResults<E> realmResults) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<CollectionChange<RealmResults<E>>>() {
            public void subscribe(final ObservableEmitter<CollectionChange<RealmResults<E>>> observableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).acquireReference(realmResults);
                final AnonymousClass1 r1 = new OrderedRealmCollectionChangeListener<RealmResults<E>>() {
                    public void onChange(RealmResults<E> realmResults, OrderedCollectionChangeSet orderedCollectionChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new CollectionChange(realmResults, orderedCollectionChangeSet));
                        }
                    }
                };
                realmResults.addChangeListener((OrderedRealmCollectionChangeListener<RealmResults<E>>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmResults.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).releaseReference(realmResults);
                    }
                }));
                observableEmitter.onNext(new CollectionChange(realmResults, null));
            }
        });
    }

    public <E> Flowable<RealmResults<E>> from(DynamicRealm dynamicRealm, final RealmResults<E> realmResults) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<RealmResults<E>>() {
            public void subscribe(final FlowableEmitter<RealmResults<E>> flowableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).acquireReference(realmResults);
                final AnonymousClass1 r1 = new RealmChangeListener<RealmResults<E>>() {
                    public void onChange(RealmResults<E> realmResults) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(realmResults);
                        }
                    }
                };
                realmResults.addChangeListener((RealmChangeListener<RealmResults<E>>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmResults.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).releaseReference(realmResults);
                    }
                }));
                flowableEmitter.onNext(realmResults);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E> Observable<CollectionChange<RealmResults<E>>> changesetsFrom(DynamicRealm dynamicRealm, final RealmResults<E> realmResults) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<CollectionChange<RealmResults<E>>>() {
            public void subscribe(final ObservableEmitter<CollectionChange<RealmResults<E>>> observableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).acquireReference(realmResults);
                final AnonymousClass1 r1 = new OrderedRealmCollectionChangeListener<RealmResults<E>>() {
                    public void onChange(RealmResults<E> realmResults, OrderedCollectionChangeSet orderedCollectionChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new CollectionChange(realmResults, orderedCollectionChangeSet));
                        }
                    }
                };
                realmResults.addChangeListener((OrderedRealmCollectionChangeListener<RealmResults<E>>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmResults.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.resultsRefs.get()).releaseReference(realmResults);
                    }
                }));
                observableEmitter.onNext(new CollectionChange(realmResults, null));
            }
        });
    }

    public <E> Flowable<RealmList<E>> from(Realm realm, final RealmList<E> realmList) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<RealmList<E>>() {
            public void subscribe(final FlowableEmitter<RealmList<E>> flowableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).acquireReference(realmList);
                final AnonymousClass1 r1 = new RealmChangeListener<RealmList<E>>() {
                    public void onChange(RealmList<E> realmList) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(realmList);
                        }
                    }
                };
                realmList.addChangeListener((RealmChangeListener<RealmList<E>>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmList.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).releaseReference(realmList);
                    }
                }));
                flowableEmitter.onNext(realmList);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E> Observable<CollectionChange<RealmList<E>>> changesetsFrom(Realm realm, final RealmList<E> realmList) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<CollectionChange<RealmList<E>>>() {
            public void subscribe(final ObservableEmitter<CollectionChange<RealmList<E>>> observableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).acquireReference(realmList);
                final AnonymousClass1 r1 = new OrderedRealmCollectionChangeListener<RealmList<E>>() {
                    public void onChange(RealmList<E> realmList, OrderedCollectionChangeSet orderedCollectionChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new CollectionChange(realmList, orderedCollectionChangeSet));
                        }
                    }
                };
                realmList.addChangeListener((OrderedRealmCollectionChangeListener<RealmList<E>>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmList.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).releaseReference(realmList);
                    }
                }));
                observableEmitter.onNext(new CollectionChange(realmList, null));
            }
        });
    }

    public <E> Flowable<RealmList<E>> from(DynamicRealm dynamicRealm, final RealmList<E> realmList) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<RealmList<E>>() {
            public void subscribe(final FlowableEmitter<RealmList<E>> flowableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).acquireReference(realmList);
                final AnonymousClass1 r1 = new RealmChangeListener<RealmList<E>>() {
                    public void onChange(RealmList<E> realmList) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(realmList);
                        }
                    }
                };
                realmList.addChangeListener((RealmChangeListener<RealmList<E>>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmList.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).releaseReference(realmList);
                    }
                }));
                flowableEmitter.onNext(realmList);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E> Observable<CollectionChange<RealmList<E>>> changesetsFrom(DynamicRealm dynamicRealm, final RealmList<E> realmList) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<CollectionChange<RealmList<E>>>() {
            public void subscribe(final ObservableEmitter<CollectionChange<RealmList<E>>> observableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).acquireReference(realmList);
                final AnonymousClass1 r1 = new OrderedRealmCollectionChangeListener<RealmList<E>>() {
                    public void onChange(RealmList<E> realmList, OrderedCollectionChangeSet orderedCollectionChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new CollectionChange(realmList, orderedCollectionChangeSet));
                        }
                    }
                };
                realmList.addChangeListener((OrderedRealmCollectionChangeListener<RealmList<E>>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        realmList.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.listRefs.get()).releaseReference(realmList);
                    }
                }));
                observableEmitter.onNext(new CollectionChange(realmList, null));
            }
        });
    }

    public <E extends RealmModel> Flowable<E> from(Realm realm, final E e) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<E>() {
            public void subscribe(final FlowableEmitter<E> flowableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).acquireReference(e);
                final AnonymousClass1 r1 = new RealmChangeListener<E>() {
                    public void onChange(E e) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(e);
                        }
                    }
                };
                RealmObject.addChangeListener(e, (RealmChangeListener<E>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        RealmObject.removeChangeListener(e, r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).releaseReference(e);
                    }
                }));
                flowableEmitter.onNext(e);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public <E extends RealmModel> Observable<ObjectChange<E>> changesetsFrom(Realm realm, final E e) {
        final RealmConfiguration configuration = realm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<ObjectChange<E>>() {
            public void subscribe(final ObservableEmitter<ObjectChange<E>> observableEmitter) throws Exception {
                final Realm instance = Realm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).acquireReference(e);
                final AnonymousClass1 r1 = new RealmObjectChangeListener<E>() {
                    public void onChange(E e, ObjectChangeSet objectChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new ObjectChange(e, objectChangeSet));
                        }
                    }
                };
                RealmObject.addChangeListener(e, (RealmObjectChangeListener<E>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        RealmObject.removeChangeListener(e, r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).releaseReference(e);
                    }
                }));
                observableEmitter.onNext(new ObjectChange(e, null));
            }
        });
    }

    public Flowable<DynamicRealmObject> from(DynamicRealm dynamicRealm, final DynamicRealmObject dynamicRealmObject) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Flowable.create(new FlowableOnSubscribe<DynamicRealmObject>() {
            public void subscribe(final FlowableEmitter<DynamicRealmObject> flowableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).acquireReference(dynamicRealmObject);
                final AnonymousClass1 r1 = new RealmChangeListener<DynamicRealmObject>() {
                    public void onChange(DynamicRealmObject dynamicRealmObject) {
                        if (!flowableEmitter.isCancelled()) {
                            flowableEmitter.onNext(dynamicRealmObject);
                        }
                    }
                };
                RealmObject.addChangeListener(dynamicRealmObject, (RealmChangeListener<E>) r1);
                flowableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        RealmObject.removeChangeListener(dynamicRealmObject, r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).releaseReference(dynamicRealmObject);
                    }
                }));
                flowableEmitter.onNext(dynamicRealmObject);
            }
        }, BACK_PRESSURE_STRATEGY);
    }

    public Observable<ObjectChange<DynamicRealmObject>> changesetsFrom(DynamicRealm dynamicRealm, final DynamicRealmObject dynamicRealmObject) {
        final RealmConfiguration configuration = dynamicRealm.getConfiguration();
        return Observable.create(new ObservableOnSubscribe<ObjectChange<DynamicRealmObject>>() {
            public void subscribe(final ObservableEmitter<ObjectChange<DynamicRealmObject>> observableEmitter) throws Exception {
                final DynamicRealm instance = DynamicRealm.getInstance(configuration);
                ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).acquireReference(dynamicRealmObject);
                final AnonymousClass1 r1 = new RealmObjectChangeListener<DynamicRealmObject>() {
                    public void onChange(DynamicRealmObject dynamicRealmObject, ObjectChangeSet objectChangeSet) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(new ObjectChange(dynamicRealmObject, objectChangeSet));
                        }
                    }
                };
                dynamicRealmObject.addChangeListener((RealmObjectChangeListener<E>) r1);
                observableEmitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        dynamicRealmObject.removeChangeListener(r1);
                        instance.close();
                        ((StrongReferenceCounter) RealmObservableFactory.this.objectRefs.get()).releaseReference(dynamicRealmObject);
                    }
                }));
                observableEmitter.onNext(new ObjectChange(dynamicRealmObject, null));
            }
        });
    }

    public <E> Single<RealmQuery<E>> from(Realm realm, RealmQuery<E> realmQuery) {
        throw new RuntimeException("RealmQuery not supported yet.");
    }

    public <E> Single<RealmQuery<E>> from(DynamicRealm dynamicRealm, RealmQuery<E> realmQuery) {
        throw new RuntimeException("RealmQuery not supported yet.");
    }

    public boolean equals(Object obj) {
        return obj instanceof RealmObservableFactory;
    }
}
