import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * An abstract PreferenceActivity that extends the functionality of the base class to support
 * AppCompat features. This enables the use of ActionBar and other modern UI features in a
 * PreferenceActivity on older Android versions.
 */
public abstract class AppCompatPreferenceActivity extends PreferenceActivity {

    // Delegate for AppCompat support
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting up the delegate before creating the activity
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Informing the delegate about the completion of the activity creation
        getDelegate().onPostCreate(savedInstanceState);
    }

    // Returns the ActionBar provided by the AppCompat delegate
    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    // Sets the ActionBar for the activity using the AppCompat delegate
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        // Returns the MenuInflater used by the AppCompat delegate
        return getDelegate().getMenuInflater();
    }

    // Overridden setContentView methods to pass the layout to the delegate
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    // Add content view to the activity, delegated to AppCompat
    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Notifies the delegate that the activity has resumed
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        // Sync the activity's title with the delegate
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Delegate handling the configuration change
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Delegate handling the activity stopping
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Delegate handling the activity destruction
        getDelegate().onDestroy();
    }

    // Invalidate the options menu, causing it to be recreated
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    // Lazy initialization of the AppCompat delegate
    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}
