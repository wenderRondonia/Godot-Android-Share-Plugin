package shinnil.godot.plugin.android.godotshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import java.io.File;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.UsedByGodot;

public class GodotShare extends GodotPlugin {
    private static final String TAG = "godot";

    private Activity activity;

    public GodotShare(Godot godot) {
        super(godot);
        this.activity = (Activity)godot.getActivity();
    }

    @UsedByGodot
    public void shareText(String title, String subject, String text) {
        Log.d("godot", "shareText called");
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra("android.intent.extra.SUBJECT", subject);
        sharingIntent.putExtra("android.intent.extra.TEXT", text);
        this.activity.startActivity(Intent.createChooser(sharingIntent, title));
    }

    @UsedByGodot
    public void shareTextByApp(String title, String subject,String text,String app) {
        Log.d("godot", "shareText called");
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra("android.intent.extra.SUBJECT", subject);
        sharingIntent.putExtra("android.intent.extra.TEXT", text);
        sharingIntent.setPackage(app);
        try {
            PackageManager pm = this.activity.getPackageManager();
            PackageInfo info = pm.getPackageInfo(app, PackageManager.GET_META_DATA);

        } catch (Exception e) {
            Log.d("godot", "shareText failed no package found app="+app);
            //Toast.makeText(activity, "App not Installed", Toast.LENGTH_SHORT).show();

        }

        this.activity.startActivity(Intent.createChooser(sharingIntent, title));
    }

    @UsedByGodot
    public void sharePic(String path, String title, String subject, String text) {
        Uri uri;
        Log.d("godot", "sharePic called");
        File f = new File(path);
        try {
            uri = FileProvider.getUriForFile((Context)this.activity, this.activity.getPackageName(), f);
        } catch (IllegalArgumentException e) {
            Log.e("godot", "The selected file can't be shared: " + path);
            return;
        }
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        shareIntent.putExtra("android.intent.extra.SUBJECT", subject);
        shareIntent.putExtra("android.intent.extra.TEXT", text);
        shareIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.activity.startActivity(Intent.createChooser(shareIntent, title));
    }

    @NonNull
    public String getPluginName() {
        return "GodotShare";
    }
}
