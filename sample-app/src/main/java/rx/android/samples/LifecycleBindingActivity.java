package rx.android.samples;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import rx.Subscription;
import rx.android.app.RxActivity;
import rx.android.events.OnClickEvent;
import rx.android.observables.ViewObservable;
import rx.functions.Action1;

/**
 * Simple example of creating a Subscription that is bound to the lifecycle
 * (and thus automatically unsubscribed when the Activity is destroyed).
 */
public class LifecycleBindingActivity extends RxActivity {

    private static final String TAG = LifecycleBindingActivity.class.getSimpleName();

    private Button button;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button = new Button(this);
        button.setText("Click Me!");
        setContentView(button);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscription =
                bindLifecycle(ViewObservable.clicks(button))
                        .subscribe(new Action1<OnClickEvent>() {
                            @Override
                            public void call(OnClickEvent onClickEvent) {
                                Toast.makeText(LifecycleBindingActivity.this,
                                        "Clicked button!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Should output "false"
        Log.i(TAG, "onPause(), isUnsubscribed=" + subscription.isUnsubscribed());
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Should output "true"
        Log.i(TAG, "onStop(), isUnsubscribed=" + subscription.isUnsubscribed());
    }
}
