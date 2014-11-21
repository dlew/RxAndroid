package rx.android.app;

import java.util.HashSet;
import java.util.Set;

/**
 * Listens to lifecycle events and rebroadcasts them to other listeners.
 *
 * Also knows about the current state of the lifecycle so that it can handle changes.
 */
public class LifecycleManager {

    private LifecycleEvent lastEvent = null;

    private final Set<LifecycleListener> lifecycleListeners = new HashSet<LifecycleListener>();

    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    public void onLifecycleEvent(LifecycleEvent event) {
        lastEvent = event;

        for (LifecycleListener listener : lifecycleListeners) {
            listener.onLifecycleEvent(event);
        }
    }

    /**
     * A corresponding teardown event is the equivalent destructive event to the current creative event.
     * For example, CREATE corresponds to DESTROY.
     *
     * If the lifecycle is in PAUSE or beyond, the next teardown event is returned.
     * For example, PAUSE returns STOP.
     *
     * TODO: This could use a lot better terminology.
     *
     * @return the corresponding event from the last state seen, or null if we're entirely outside the lifecycle
     */
    public LifecycleEvent getCorrespondingTeardownEvent() {
        if (lastEvent == null) {
            return null;
        }

        switch(lastEvent) {
            case CREATE:
                return LifecycleEvent.DESTROY;
            case START:
                return LifecycleEvent.STOP;
            case RESUME:
                return LifecycleEvent.PAUSE;
            case PAUSE:
                return LifecycleEvent.STOP;
            case STOP:
                return LifecycleEvent.DESTROY;
            case DESTROY:
                return null;
            default:
                return null;
        }
    }

}
