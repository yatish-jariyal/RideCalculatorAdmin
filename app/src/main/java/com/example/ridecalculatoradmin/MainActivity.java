package com.example.ridecalculatoradmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String city;
    private double price;
    private String fuel_type;
    private Bitmap bitmap;
    private String title;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void fetchPetrolPrice()
    {
        ContentPetrol1 contentPetrol1 = new ContentPetrol1();
        contentPetrol1.execute();
    }

    public void fetchDieselPrice()
    {
        ContentDiesel content = new ContentDiesel();
        content.execute();
    }

    public void fetchCNGPrice()
    {
        ContentCNG contentCNG = new ContentCNG();
        contentCNG.execute();

    }

    public static void checkElement(String name, Element elem) {
        if (elem == null) {
            throw new RuntimeException("Unable to find " + name);
        }
    }

    private class ContentPetrol1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                String url = "https://www.mypetrolprice.com/petrol-price-in-india.aspx";
                Document document = Jsoup.connect(url).get();


                ArrayList<String> downServers = new ArrayList<>();
                Elements statesList = document.getElementsByClass("sixteen columns row");
                Log.d("rowssize",statesList.size()+"");
                Elements states = statesList.get(3).getAllElements();

                //System.out.println("states"+states.toString());
                String stateName = "";
                String cityName = "";
                String price = "";
                for(int i = 0;i<states.size();i++)
                {
                    Element e = states.get(i);
                    if(e.is("h2"))
                    {
                        String s = e.toString();
                        stateName = s.substring(s.indexOf("\">")+2,s.indexOf("</a>"));
                        System.out.println("state = "+stateName);
                    }
                    else
                    {

                        if(e.hasClass("CH"))
                        {

                            String s = e.getAllElements().get(1).toString();
                            cityName = s.substring(s.indexOf("title=")+7);
                            s = cityName.substring(0, cityName.indexOf("Petrol")-1);
                            System.out.println("city = "+s);
                        }
                        else if(e.hasClass("txtC"))
                        {
                            String s = e.toString();
                            price = s.substring(s.indexOf("<b>₹")+5, s.indexOf("</b>"));
                            System.out.println("price = "+price);

                        }
                        ApiInterface apiservice = ApiClient.getRetrofitInstance().create(ApiInterface.class);
                        Call <ResponseBody> call = apiservice.updateFuelPrices(stateName, cityName, Double.parseDouble(price), "Petrol");
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
        }
    }



    private class ContentCNG extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                String url = "https://www.mypetrolprice.com/cng-price-in-india.aspx";
                Document document = Jsoup.connect(url).get();


                ArrayList<String> downServers = new ArrayList<>();
                Elements statesList = document.getElementsByClass("sixteen columns row");
                Log.d("rowssize",statesList.size()+"");
                Elements states = statesList.get(3).getAllElements();

                //System.out.println("states"+states.toString());

                String stateName = "";
                String cityName = "";
                String price = "";
                for(int i = 0;i<states.size();i++)
                {
                    Element e = states.get(i);
                    if(e.is("h2"))
                    {
                        String s = e.toString();
                        stateName = s.substring(s.indexOf("\">")+2,s.indexOf("</a>"));
                        System.out.println("state = "+stateName);
                    }
                    else
                    {

                        if(e.hasClass("CH"))
                        {

                            String s = e.getAllElements().get(1).toString();
                            cityName = s.substring(s.indexOf("title=")+7);
                            s = cityName.substring(0, cityName.indexOf("Petrol")-1);
                            System.out.println("city = "+s);
                        }
                        else if(e.hasClass("txtC"))
                        {
                            String s = e.toString();
                            price = s.substring(s.indexOf("<b>₹")+5, s.indexOf("</b>"));
                            System.out.println("price = "+price);

                        }
                        ApiInterface apiservice = ApiClient.getRetrofitInstance().create(ApiInterface.class);
                        Call <ResponseBody> call = apiservice.updateFuelPrices(stateName, cityName, Double.parseDouble(price), "CNG");
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
        }
    }

    private class ContentDiesel extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                String url = "https://www.mypetrolprice.com/diesel-price-in-india.aspx";
                Document document = Jsoup.connect(url).get();


                ArrayList<String> downServers = new ArrayList<>();
                Elements statesList = document.getElementsByClass("sixteen columns row");
                Log.d("rowssize",statesList.size()+"");
                Elements states = statesList.get(3).getAllElements();

                //System.out.println("states"+states.toString());

                String stateName = "";
                String price = "";
                String cityName = "";
                for(int i = 0;i<states.size();i++)
                {
                    Element e = states.get(i);
                    if(e.is("h2"))
                    {
                        String s = e.toString();
                        stateName = s.substring(s.indexOf("\">")+2,s.indexOf("</a>"));
                        System.out.println("state = "+stateName);
                    }
                    else
                    {

                        if(e.hasClass("CH"))
                        {

                            String s = e.getAllElements().get(1).toString();
                            cityName = s.substring(s.indexOf("title=")+7);
                            s = cityName.substring(0, cityName.indexOf("Petrol")-1);
                            System.out.println("city = "+s);
                        }
                        else if(e.hasClass("txtC"))
                        {
                            String s = e.toString();
                            price = s.substring(s.indexOf("<b>₹")+5, s.indexOf("</b>"));
                            System.out.println("price = "+price);

                        }
                        ApiInterface apiservice = ApiClient.getRetrofitInstance().create(ApiInterface.class);
                        Call <ResponseBody> call = apiservice.updateFuelPrices(stateName, cityName, Double.parseDouble(price), "Diesel");
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
        }
    }


    public void onClick(View v)
    {
        if(v.getId() == R.id.button)
        {
            //fetch fuel prices
           // fetchPetrolPrice();
            //fetchDieselPrice();
            fetchCNGPrice();
        }
    }

}
