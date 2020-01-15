package su.sorex.com.sorex_front_app;

import android.app.Application;
import android.os.SystemClock;

public class SorEx extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
