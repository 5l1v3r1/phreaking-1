package tofu.mishazawa.phreaking;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;

public class CallToServerTask extends AsyncTask<Void, Void, Socket> {
  private static final String SERVER_IP = "192.168.0.104";
  private static final int SERVER_PORT = 8080;
  private WeakReference<MainActivity> mainActivityWeakReference;

  public CallToServerTask (MainActivity thread) {
    mainActivityWeakReference = new WeakReference<>(thread);
  }

  @Override
  protected Socket doInBackground(Void... voids) {
    try {
      InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
      return new Socket(serverAddr, SERVER_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  protected void onPostExecute(Socket socket) {
    mainActivityWeakReference.get().setConnectionSocket(socket);
  }
}
