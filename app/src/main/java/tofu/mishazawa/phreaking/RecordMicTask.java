package tofu.mishazawa.phreaking;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class RecordMicTask extends AsyncTask<Void, Void, Void> {
  private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
  private static final int SAMPLE_RATE = 44100; // Hz
  private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
  private static final int CHANNEL_MASK = AudioFormat.CHANNEL_IN_MONO;
  private static final int BUFFER_SIZE = 2 * AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_MASK, ENCODING);

  private WeakReference<MainActivity> mainActivityWeakReference;

  RecordMicTask(MainActivity activity) {
    this.mainActivityWeakReference = new WeakReference<>(activity);
  }

  @Override
  protected Void doInBackground(Void... voids) {
    try {
      AudioRecord rec = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_MASK, ENCODING, BUFFER_SIZE);
      DataOutputStream outputStream = new DataOutputStream(this.mainActivityWeakReference.get().connection.getOutputStream());
      byte[] buffer = new byte[BUFFER_SIZE];
      NoiseSuppressor.create(rec.getAudioSessionId());
      rec.startRecording();

      while (isRunning()) {
        int bytesRead = rec.read(buffer, 0, buffer.length);
        outputStream.write(buffer, 0, bytesRead);
      }

      rec.stop();
      rec.release();
      outputStream.flush();
    } catch (IOException err) {
      err.printStackTrace();
    }
    return null;
  }

  private boolean isRunning() {
    return mainActivityWeakReference.get().recording;
  }
}
