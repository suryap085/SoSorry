package comsorry.sosorry.youtubedataactivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import comsorry.sosorry.ConnectionDetector;
import comsorry.sosorry.R;
import comsorry.sosorry.youtubedataactivities.SearchVideos.SearchActivity;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class YouTubeActivity extends AppCompatActivity {
    private static final String[] YOUTUBE_PLAYLISTS = {
            "PL_yIBWagYVjztnssruzd0PHc92RY8giOA",
            "PLK_opvkxlemWiF-LEw1nsaksN5GXPIrAK"


    };
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (Config.DEVELOPER_KEY.startsWith("YOUR_API_KEY")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                        .setTitle("Missing API Key")
                        .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else if (savedInstanceState == null) {
                mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                        .setApplicationName(getResources().getString(R.string.app_name))
                        .build();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS))
                        .commit();
            }
        } else {
            showAlertDialog(this,
                    "No Internet Connection",
                    "You don't have internet connection.", false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        boolean returnValue = true;

        switch (itemId) {

            case R.id.menu_rate_us:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id="
                                        + "comsorry.sosorry")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + "comsorry.sosorry")));
                    }
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;
            case R.id.share:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    Intent sharingIntent = new Intent(
                            android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "So Sorry");

                    sharingIntent
                            .putExtra(
                                    android.content.Intent.EXTRA_TEXT,
                                    "https://play.google.com/store/apps/details?id=comsorry.sosorry&hl=en");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;

            case R.id.more:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://search?q=Surya+Prakash+Maurya")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + "comsorry.sosorry")));
                    }
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;
            case R.id.search_menu:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    try {
                        Intent intent=new Intent(this,SearchActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;

           default:
                returnValue = super.onOptionsItemSelected(item);
                break;
        }
        return returnValue;
    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);

        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
