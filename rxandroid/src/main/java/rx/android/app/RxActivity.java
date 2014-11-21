package rx.android.app;

import android.app.Activity;
import android.os.Bundle;
import rx.Observable;

/**
 * An Activity with reactive extensions to make it easier to handle binding
 * Observables to the lifecycle.
 */
public class RxActivity extends Activity {

    private final LifecycleManager lifecycleManager = new LifecycleManager();

    public <T> Observable<T> bindLifecycle(Observable<T> source) {
        LifecycleEvent event = lifecycleManager.getCorrespondingTeardownEvent();
        if (event == null) {
            throw new IllegalStateException("Cannot automatically bind to lifecycle when completely outside of it.");
        }

        return bindUntilLifecycleEvent(source, event);
    }

    public <T> Observable<T> bindUntilLifecycleEvent(Observable<T> source, LifecycleEvent event) {
        if (source == null) {
            throw new IllegalArgumentException("Observable must be given");
        }

        return source.takeUntil(Observable.create(new OperatorLifecycleEvent(lifecycleManager, event)));
    }

    // Lifecycle manager notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleManager.onLifecycleEvent(LifecycleEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleManager.onLifecycleEvent(LifecycleEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleManager.onLifecycleEvent(LifecycleEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleManager.onLifecycleEvent(LifecycleEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleManager.onLifecycleEvent(LifecycleEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleManager.onLifecycleEvent(LifecycleEvent.DESTROY);
    }
}
