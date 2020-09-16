        package br.com.nobredelatorre.camesp32;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

        public class PhotoImageView extends ImageView {

    public static final int MODE_ORIGINAL = 0;
    public static final int MODE_FIT_WIDTH = 1;
    public static final int MODE_FIT_HEIGHT = 2;
    public static final int MODE_BEST_FIT = 3;
    public static final int MODE_STRETCH = 4;


    private static final int WAIT_AFTER_READ_IMAGE_ERROR_MSEC = 5000;
    private final String tag = getClass().getSimpleName();

    private Context context;
    private String url;
    private Bitmap lastBitmap;
    private PhotoDownloader downloader1;
    private final Object lockBitmap = new Object();

    private Paint paint;
    private Rect dst;

    private int mode = MODE_ORIGINAL;
    private int drawX,drawY, vWidth = -1, vHeight = -1;
    private int lastImgWidth, lastImgHeight;

    private boolean adjustWidth, adjustHeight;

    private final String CONTENT_LENGTH = "Content-Length";
    private final static int HEADER_MAX_LENGTH = 100;
    private final static int FRAME_MAX_LENGTH = 40000 + HEADER_MAX_LENGTH;
    private int mContentLength = -1;
    private DataInputStream in;
    private String ip;

    private int msecWaitAfterReadImageError = WAIT_AFTER_READ_IMAGE_ERROR_MSEC;

    private boolean isRecycleBitmap;
    private boolean isUserForceConfigRecycle;

    private OutputStream dataOutputStream = null;
    private InputStream dataInputStream = null;

    private static final int SERVER_PORT = 80;
    private static final String SERVER_IP = "192.168.4.1";
    private Socket socket;


    public PhotoImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PhotoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PhotoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dst = new Rect(0,0,0,0);
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isAdjustWidth() {
        return adjustWidth;
    }

    public void setAdjustWidth(boolean adjustWidth) {
        this.adjustWidth = adjustWidth;
    }

    public boolean isAdjustHeight() {
        return adjustHeight;
    }

    public void setAdjustHeight(boolean adjustHeight) {
        this.adjustHeight = adjustHeight;
    }


    public void startStream(){
        if(downloader1 != null && downloader1.isRunning()){
            Log.w(tag,"Already started, stop by calling stopStream() first.");
            return;
        }
        downloader1 = new PhotoDownloader();
        downloader1.start();
    }

    public void setBitmap(Bitmap bm){
        Log.v(tag,"Nova Foto - tamanho = "+bm.getByteCount());
        synchronized (lockBitmap) {
            if (lastBitmap != null && ((isUserForceConfigRecycle && isRecycleBitmap) || (!isUserForceConfigRecycle && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB))) {
                Log.v(tag, "Manually recycle bitmap");
                lastBitmap.recycle();
            }
            lastBitmap = bm;
        }

        if(context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                    requestLayout();
                    ((TakePhoto) context).progressHide();
                }
            });
        }
        else{
            Log.e(tag,"Can not request Canvas's redraw. Context is not an instance of Activity");
        }
    }
    public Bitmap getBitmap()
    {
        return lastBitmap;
    }

    public void stopStream(){
        try {
            downloader1.join(); // cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean shouldRecalculateSize;
        synchronized (lockBitmap) {
            shouldRecalculateSize = lastBitmap != null && (lastImgWidth != lastBitmap.getWidth() || lastImgHeight != lastBitmap.getHeight());
            if(shouldRecalculateSize) {
                lastImgWidth = lastBitmap.getWidth();
                lastImgHeight = lastBitmap.getHeight();
            }
        }

        if(shouldRecalculateSize) {
            Log.d(tag,"Recalculate view/image size");

            vWidth = MeasureSpec.getSize(widthMeasureSpec);
            vHeight = MeasureSpec.getSize(heightMeasureSpec);

            if(mode == MODE_ORIGINAL){
                drawX = (vWidth - lastImgWidth)/2;
                drawY = (vHeight - lastImgHeight)/2;

                if(adjustWidth){
                    vWidth = lastImgWidth;
                    drawX = 0;
                }

                if(adjustHeight){
                    vHeight = lastImgHeight;
                    drawY = 0;
                }
            }
            else if(mode == MODE_FIT_WIDTH){
                int newHeight = (int)(((float)lastImgHeight/(float)lastImgWidth)*vWidth);

                drawX = 0;

                if(adjustHeight){
                    vHeight = newHeight;
                    drawY = 0;
                }
                else{
                    drawY = (vHeight - newHeight)/2;
                }

                //no need to check adjustWidth because in this mode image's width is always equals view's width.

                dst.set(drawX,drawY,vWidth,drawY+newHeight);
            }
            else if(mode == MODE_FIT_HEIGHT){
                int newWidth = (int)(((float)lastImgWidth/(float)lastImgHeight)*vHeight);

                drawY = 0;

                if(adjustWidth){
                    vWidth = newWidth;
                    drawX = 0;
                }
                else{
                    drawX = (vWidth - newWidth)/2;
                }

                //no need to check adjustHeight because in this mode image's height is always equals view's height.

                dst.set(drawX,drawY,drawX+newWidth,vHeight);
            }
            else if(mode == MODE_BEST_FIT){
                if((float)lastImgWidth/(float)vWidth > (float)lastImgHeight/(float)vHeight){
                    //duplicated code
                    //fit width
                    int newHeight = (int)(((float)lastImgHeight/(float)lastImgWidth)*vWidth);

                    drawX = 0;

                    if(adjustHeight){
                        vHeight = newHeight;
                        drawY = 0;
                    }
                    else{
                        drawY = (vHeight - newHeight)/2;
                    }

                    //no need to check adjustWidth because in this mode image's width is always equals view's width.

                    dst.set(drawX,drawY,vWidth,drawY+newHeight);
                }
                else{
                    //duplicated code
                    //fit height
                    int newWidth = (int)(((float)lastImgWidth/(float)lastImgHeight)*vHeight);

                    drawY = 0;

                    if(adjustWidth){
                        vWidth = newWidth;
                        drawX = 0;
                    }
                    else{
                        drawX = (vWidth - newWidth)/2;
                    }

                    //no need to check adjustHeight because in this mode image's height is always equals view's height.

                    dst.set(drawX,drawY,drawX+newWidth,vHeight);
                }
            }
            else if(mode == MODE_STRETCH){
                dst.set(0,0,vWidth,vHeight);
                //no need to check neither adjustHeight nor adjustHeight because in this mode image's size is always equals view's size.
            }

            setMeasuredDimension(vWidth, vHeight);
        }
        else {
            if(vWidth == -1 || vHeight == -1){
                vWidth = MeasureSpec.getSize(widthMeasureSpec);
                vHeight = MeasureSpec.getSize(heightMeasureSpec);
            }

            setMeasuredDimension(vWidth, vHeight);
        }
    }


    @Override
    protected void onDraw(Canvas c) {
        synchronized (lockBitmap) {
            if (c != null && lastBitmap != null && !lastBitmap.isRecycled()) {
                if (isInEditMode()) {
                    // TODO: preview while edit xml
                } else if (mode != MODE_ORIGINAL) {
                    c.drawBitmap(lastBitmap, null, dst, paint);
                } else {
                    c.drawBitmap(lastBitmap, drawX, drawY, paint);
                }
            } else {
                Log.d(tag, "Skip drawing, canvas is null or bitmap is not ready yet");
            }
        }
    }



    public int getMsecWaitAfterReadImageError() {
        return msecWaitAfterReadImageError;
    }

    public void setMsecWaitAfterReadImageError(int msecWaitAfterReadImageError) {
        this.msecWaitAfterReadImageError = msecWaitAfterReadImageError;
    }

    public boolean isRecycleBitmap() {
        return isRecycleBitmap;
    }

    public void setRecycleBitmap(boolean recycleBitmap) {
        isUserForceConfigRecycle = true;
        isRecycleBitmap = recycleBitmap;
    }

    public void refresh(){
        if(context instanceof  Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                    requestLayout();
                }
            });
        }
        else{
            Log.e(tag,"Can not request Canvas's redraw. Context is not an instance of Activity");
        }
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
            dataInputStream.read(v);
            socket.close();
            System.out.println("-- mudou para "+size);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
    }



    class PhotoDownloader extends Thread{

        private boolean run = true;

        public void cancel(){
            run = false;
        }

        public boolean isRunning(){
            return run;
        }

        @Override
        public void run() {
            while(run) {
                if(msecWaitAfterReadImageError > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        if(e != null && e.getMessage() != null) {
                            Log.e(tag, e.getMessage());
                        }
                    }
                }

                HttpURLConnection connection = null;
                BufferedInputStream bis = null;
                URL serverUrl = null;

                try {
                    serverUrl = new URL(url);

                    connection = (HttpURLConnection) serverUrl.openConnection();
                    connection.connect();

                    try{
                        // Try to extract a boundary from HTTP header first.
                        // If the information is not presented, throw an exception and use default value instead.
                        String contentType = connection.getHeaderField("Content-Type");
                        if (contentType == null) {
                            throw new Exception("Unable to get content type");
                        }

                        String[] types = contentType.split(";");
                        if (types.length == 0) {
                            throw new Exception("Content type was empty");
                        }

                        String contentlength = connection.getHeaderField("Content-Length");
                        if (contentType == null) {
                            throw new Exception("Unable to get content type");
                        }
                        mContentLength = Integer.parseInt(contentlength); //props.getProperty(CONTENT_LENGTH));
                    }
                    catch(Exception e){
                        Log.w(tag,"Cannot extract a boundary string from HTTP header with message: " + e.getMessage() + ". Use a default value instead.");
                    }

                    in = new DataInputStream(connection.getInputStream());
                    in.available();
                    System.out.println("tamanho em bytes"+mContentLength +" Disponivel : "+ in.available());
                    Bitmap outputImg = BitmapFactory.decodeStream(in);;//BitmapFactory.decodeByteArray(image, 0, image.length);
                    System.out.println("Bitmap em bytes"+outputImg.getByteCount());
                    if (outputImg != null) {
                        if(run) {
                            newFrame(outputImg);
                            run = false;
                        }
                    } else {
                        Log.e(tag, "Read image error");
                    }

                } catch (Exception e) {
                    if(e != null && e.getMessage() != null) {
                        Log.e(tag, e.getMessage());
                    }
                }

                try {
                    in.close();
                    connection.disconnect();
                    Log.i(tag,"disconnected with " + url);
                } catch (Exception e) {
                    if(e != null && e.getMessage() != null) {
                        Log.e(tag, e.getMessage());
                    }
                }

                if(msecWaitAfterReadImageError > 0) {
                    try {
                        Thread.sleep(msecWaitAfterReadImageError);
                    } catch (InterruptedException e) {
                        if(e != null && e.getMessage() != null) {
                            Log.e(tag, e.getMessage());
                        }
                    }
                }

            }

        }


        private void newFrame(Bitmap bitmap)
        {
            setBitmap(bitmap);
        }

    }



}
