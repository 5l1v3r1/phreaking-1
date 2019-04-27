package tofu.mishazawa.phreaking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
  private static final int PERMISSION_RECORD_AUDIO = 0;

  Socket connection;
  Button call;
  Button ptt;

  boolean recording = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    call = findViewById(R.id.call_button);
    ptt = findViewById(R.id.push_to_talk);
  }

  void setConnectionSocket(Socket conn) {
    this.connection = conn;
  }

  public void callToServer(View view) {
    new CallToServerTask(this).execute();
  }

  public void talk (View view) {
    if (connection == null) return;

    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.RECORD_AUDIO }, PERMISSION_RECORD_AUDIO);
      return;
    }

    recording = !recording;
    record(recording);

    ptt.setText(recording ? "Stop talk" : "Start talk");
  }

  void record (boolean flag) {
    if (!flag) return;
    new RecordMicTask(this).execute();
  }
}
