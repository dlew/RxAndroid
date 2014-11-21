package rx.android.app;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by dlew on 11/21/14.
 */
public class OperatorLifecycleEvent implements Observable.OnSubscribe<LifecycleEvent> {

    private final LifecycleManager lifecycle;
    private final LifecycleEvent lifecycleEvent;

    public OperatorLifecycleEvent(LifecycleManager lifecycle, LifecycleEvent lifecycleEvent) {
        this.lifecycle = lifecycle;
        this.lifecycleEvent = lifecycleEvent;
    }

    @Override
    public void call(Subscriber<? super LifecycleEvent> subscriber) {
        new LifecycleSubscription(subscriber, lifecycle, lifecycleEvent);
    }

    private static final class LifecycleSubscription implements Subscription, LifecycleListener {
        private Subscriber<? super LifecycleEvent> subscriber;
        private LifecycleManager lifecycle;
        private final LifecycleEvent lifecycleEvent;

        public LifecycleSubscription(Subscriber<? super LifecycleEvent> subscriber,
                                     LifecycleManager lifecycle,
                                     LifecycleEvent lifecycleEvent) {
            this.subscriber = subscriber;
            this.lifecycle = lifecycle;
            this.lifecycleEvent = lifecycleEvent;
            lifecycle.addLifecycleListener(this);
        }

        @Override
        public void onLifecycleEvent(LifecycleEvent event) {
            if (event != lifecycleEvent) {
                return;
            }

            subscriber.onNext(event);
        }

        @Override
        public void unsubscribe() {
            lifecycle.removeLifecycleListener(this);
            subscriber.unsubscribe();
        }

        @Override
        public boolean isUnsubscribed() {
            return subscriber.isUnsubscribed();
        }
    }
}
