package com.example.ksoap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private static String NAMESPACE = "http://www.slmm.com.br/ws";
    private static String URL = "http://www.slmm.com.br/ws/ws1.php";
    private static String SOAP_ACTION = "http://www.slmm.com.br/ws/concatena";
    private static String METHOD_NAME = "concatena";

    private TextView txtResp;
    private EditText edt1;
    private EditText edt2;

    public String retorno = "";
    public String param1 = "";
    public String param2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = (EditText)findViewById(R.id.edt1);
        edt2 = (EditText)findViewById(R.id.edt2);
        txtResp = (TextView)findViewById(R.id.txtResp);

        findViewById(R.id.button).setOnClickListener(OnClickConcatena());

    }

    private View.OnClickListener OnClickConcatena(){
        return new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                param1 = edt1.getText().toString();
                param2 = edt2.getText().toString();

                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        };
    }

    private class AsyncCallWS extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            setDados(param1,param2);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            txtResp.setText(retorno);
        }
    }

    public void setDados(String str1, String str2){

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        PropertyInfo str1PI = new PropertyInfo();
        str1PI.setName("str1");
        str1PI.setValue(str1);
        str1PI.setType(String.class);
        request.addProperty(str1PI);

        PropertyInfo str2PI = new PropertyInfo();
        str2PI.setName("str2");
        str2PI.setValue(str2);
        str2PI.setType(String.class);
        request.addProperty(str2PI);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.bodyIn;

            retorno = response.getProperty("return").toString();

        } catch (Exception e){
            retorno = "erro "+ e.toString();
        }

    }

}
