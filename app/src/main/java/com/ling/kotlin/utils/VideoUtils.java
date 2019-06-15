package com.ling.kotlin.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import com.ling.kotlin.widget.CustomVideoView;

public class VideoUtils {

    private Context context;
    public static void startVideo(Context context ,int rawId,final CustomVideoView videoView){

        videoView.setVideoURI(Uri.parse("android.resource://"+ context.getPackageName()+"/"+ rawId));
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }

}
