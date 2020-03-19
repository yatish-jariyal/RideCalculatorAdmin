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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        ContentPetrol content = new ContentPetrol();
        content.execute();
    }

    public void fetchDieselPrice()
    {
        ContentDiesel content = new ContentDiesel();
        content.execute();
    }

    public void fetchCNGPrice()
    {

    }

    private class ContentPetrol extends AsyncTask<Void, Void, Void> {

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
                String url = "https://www.bankbazaar.com/fuel/petrol-price-india.html";
                Document document = Jsoup.connect(url).get();

                //Get the logo source of the website
                Element img = document.select("img").first();
                // Locate the src attribute
                String imgSrc = img.absUrl("src");
                // Download image from URL
                InputStream input = new java.net.URL(imgSrc).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);

                //Get the title of the website
                title = document.title();

                ArrayList<String> downServers = new ArrayList<>();
                Element table = document.select("table").get(1); //select the first table.
                Elements rows = table.select("tr");
                Log.d("rowssize",rows.size()+"");

                for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.

                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    String c = cols.get(0).select("td").toString();
                    String c1 = c.substring(c.indexOf(".html")+7,c.length());
                    city = c1.substring(0,c1.indexOf("<"));
                    String p = cols.get(1).select("td").toString();
                    String p1 = p.substring(6,p.length());
                    price = Double.parseDouble(p1.substring(0,p1.indexOf("<")));

                    Log.d("price = ",price+"");
                    Log.d("city = ",city);

                    //update Fuel prices
                    ApiInterface apiService = ApiClient.getRetrofitInstance().create(ApiInterface.class);
                    Call<ResponseBody> call = apiService.updateFuelPrices(city, price, "Petrol");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("response", response.toString());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("failure", t.toString());

                        }
                    });


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
                String url = "https://www.bankbazaar.com/fuel/diesel-price-india.html";
                Document document = Jsoup.connect(url).get();

                //Get the logo source of the website
                Element img = document.select("img").first();
                // Locate the src attribute
                String imgSrc = img.absUrl("src");
                // Download image from URL
                InputStream input = new java.net.URL(imgSrc).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);

                //Get the title of the website
                title = document.title();

                ArrayList<String> downServers = new ArrayList<>();
                Element table = document.select("table").get(0); //select the first table.
                Elements rows = table.select("tr");
                Log.d("rowssize",rows.size()+"");

                for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.

                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    String c = cols.get(0).select("td").toString();
                    String c1 = c.substring(c.indexOf(".html")+7,c.length());
                    city = c1.substring(0,c1.indexOf("<"));
                    String p = cols.get(1).select("td").toString();
                    String p1 = p.substring(6,p.length());
                    price = Double.parseDouble(p1.substring(0,p1.indexOf("<")));

                    Log.d("price = ",price+"");
                    Log.d("city = ",city);

                    //update Fuel prices

                    ApiInterface apiService = ApiClient.getRetrofitInstance().create(ApiInterface.class);

                    Call<ResponseBody> call = apiService.updateFuelPrices(city, price, "Diesel");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("response", response.toString());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("failure", t.toString());

                        }
                    });



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
            fetchPetrolPrice();
            fetchDieselPrice();
            fetchCNGPrice();
        }
    }

}
