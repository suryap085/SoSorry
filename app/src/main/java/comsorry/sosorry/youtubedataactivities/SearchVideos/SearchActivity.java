package comsorry.sosorry.youtubedataactivities.SearchVideos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comsorry.sosorry.ConnectionDetector;
import comsorry.sosorry.R;
import comsorry.sosorry.youtubedataactivities.YouTubePlayer_Activity;

/**
 * Created by Surya on 5/19/2017.
 */
public class SearchActivity extends Activity {
    private EditText searchInput;
    private ListView videosFound;
    private Handler handler;
    private List<VideoItem> searchResults;
    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchInput = (EditText) findViewById(R.id.search_input);
        videosFound = (ListView) findViewById(R.id.videos_found);
        handler = new Handler();


        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), YouTubePlayer_Activity.class);
                intent.putExtra("VideoId", searchResults.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void btnSearchClick(View v) {

        pd = ProgressDialog.show(this, "",
                "searching file..!Please wait...", true);
        searchOnYoutube(searchInput.getText().toString());

    }


    private void searchOnYoutube(final String keywords) {
        new Thread() {
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable() {
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        if (searchResults != null) {
            ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                    }
                    ImageView thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                    TextView title = (TextView) convertView.findViewById(R.id.video_title);
                    // TextView description = (TextView)convertView.findViewById(R.id.video_description);

                    VideoItem searchResult = searchResults.get(position);

                    Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                    title.setText(searchResult.getTitle());
                    //description.setText(searchResult.getDescription());
                    return convertView;
                }
            };

            videosFound.setAdapter(adapter);
            pd.dismiss();
        } else {
            pd.dismiss();
            showAlertDialog(this, "Something wrong!",
                    "Please check network connection.", false);
        }
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
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
