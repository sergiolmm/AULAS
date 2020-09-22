package br.com.nobredelatorre.camesp32;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    private LinearLayout lyFundo;
    private ProgressBar pbCirculo;
    private PhotoImageView photo;
    private Bitmap bm;
    private Uri uri = null;

    private static OutputStream dataOutputStream = null;
    private static InputStream dataInputStream = null;

    private static final int    SERVER_PORT = 80;
    private static final String SERVER_IP   = "192.168.4.1";
    private static Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); //hide the title bar

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_take_photo);

        hideSystemUI();
        try {
            setFramesize(getLocalIpAddress(), "9");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", 0, 100);
        animation.setDuration(2000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();


        pbCirculo = (ProgressBar) findViewById(R.id.circularProgressbar);
        //    pbCirculo.setVisibility(View.GONE);

        lyFundo = (LinearLayout)findViewById(R.id.layoutPhoto);
        lyFundo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        Log.i("aa", "ACTION_UP----- ");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        if (x < 80) {
                            photo.stopStream();
                            exitPhoto();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.i("aa", "ACTION_DOWN-----");
                        break;
                }
                return true; //false
            }
        });

        Intent it = getIntent();
        if (it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                if (params.get(MediaStore.EXTRA_OUTPUT) != null)
                    uri = (Uri)params.get(MediaStore.EXTRA_OUTPUT);
            }
        }

        //

        photo = (PhotoImageView) findViewById(R.id.photoview);
        photo.setUrl("http://192.168.4.1:80/capture?_cb=20200216");
        photo.setMode(PhotoImageView.MODE_BEST_FIT);// MODE_STRETCH);
        photo.setAdjustHeight(true);
        photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        Log.i("aa", "ACTION_UP----- ");

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.i("aa", "ACTION_DOWN-----");
                        savePhoto();
                        break;
                }
                return true; //false
            }
        });

        photo.startStream();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    public void exitPhoto()
    {
        try {
            final ParcelFileDescriptor pdf = getContentResolver().openFileDescriptor(uri, "rw");
            if (pdf != null)
            {
                File file = new File(uri.getPath());
                file.delete();


            }else {
                try {
                    throw new FileNotFoundException("result of " + uri + " is null!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void savePhoto()
    {
        Uri photoURI = null;

        if (uri != null) {
            photoURI = uri;
        }
        else{
            // salvar a foto no arquivo passsado ou gerar um nome...
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {

                try {
                    photoURI = FileProvider.getUriForFile(TakePhoto.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (photoURI != null) {
            try {
                final ParcelFileDescriptor pdf = getContentResolver().openFileDescriptor(photoURI, "rw");
                if (pdf == null) try {
                    throw new FileNotFoundException("result of " + photoURI + " is null!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FileOutputStream outStream1 = new FileOutputStream(pdf.getFileDescriptor());//getContentResolver().openFileDescriptor(photoURI,"rw").getFileDescriptor());
                Bitmap bmp = photo.getBitmap();
                System.out.println("tamanho  " + bmp.getByteCount());
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream1); // 100

                outStream1.flush();
                outStream1.close();
                bmp.recycle();

            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                setFramesize(getLocalIpAddress(), "4");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //EXTRA_CAMERA_DATA
            setResult(RESULT_OK, intent);
        }
        else
        {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
        photo.stopStream();
        finish();
    }

    public void setFramesize( String _ip, String size ) {
        //http://192.168.4.1/control?var=framesize&val=6

        String message = "GET /control?var=framesize&val="+size+" HTTP/1.1\r\n" + //stream capture?_cb=20200201
                "Host: " + _ip + "\r\n" +
                "Connection: Keep - Alive\r\n\r\n";


        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            try {

                dataOutputStream = socket.getOutputStream();
                dataInputStream  = socket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            byte[] resp = message.getBytes();
            dataOutputStream.write( resp);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage().toString());
        }


        try {
            byte[] v = new byte[1];

            int b;
            try {
                dataInputStream.read(v);
                //               while (!((b = ) > 0))
                //                   System.out.print((char)b);
                System.out.println("--");
            }catch (Exception e)
            {
                ;
            }
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }



    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        // aqui tem verificar se o wifi esta ligoa se nao ligar...
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }

    String imageFilePath;

    private File createImageFile() throws IOException {


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CamEsp32_" + timeStamp + ".jpg";
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS ), imageFileName);

        if(!storageDir.exists()) storageDir.mkdirs();

        return storageDir;

        /*
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Calendar cal = Calendar.getInstance();
        String imageFileName = "CamEsp32_" ;//+ formatoData.format(cal.getTime());

        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES );
        try {
            // Make sure the Pictures directory exists.
            storageDir.mkdirs();
        }
        catch (Exception e){
            ;
        }

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //m_fotoRet = image.toString().substring(image.toString().lastIndexOf("/") + 1);
        imageFilePath = image.getAbsolutePath();
        return image;
        */
    }

    private String getFilePath(Uri uri){
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS ) ,"");
        } else {
            filesDir = new File(getExternalFilesDir(null),"external_files");
        }
        return filesDir.toString() + uri.toString().substring(uri.toString().lastIndexOf('/'));
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void progressHide()
    {
        pbCirculo.setVisibility(View.GONE);
    }

    public void saveImage(Bitmap _bm)
    {
        bm = _bm;
    }





}
