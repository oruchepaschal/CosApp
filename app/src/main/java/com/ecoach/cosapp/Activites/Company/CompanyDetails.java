package com.ecoach.cosapp.Activites.Company;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.RequestQueue;
import com.ecoach.cosapp.Activites.CompanyDetailsTabbedActivities.Details;
import com.ecoach.cosapp.Activites.CompanyDetailsTabbedActivities.Map;
import com.ecoach.cosapp.Activites.CompanyDetailsTabbedActivities.Profile;
import com.ecoach.cosapp.Application.Application;
import com.ecoach.cosapp.DataBase.VerifiedCompanies;
import com.ecoach.cosapp.Http.VolleySingleton;
import com.ecoach.cosapp.R;
import com.ecoach.cosapp.Utilities.ViewUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CompanyDetails extends AppCompatActivity  {
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
   // private AVLoadingIndicatorView avi;
    private TextView companyName,companycategory;
    private MaterialRatingBar ratingBar;
    private RelativeLayout imageView2;

    CircleImageView companyAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(Application.getSelectedCompanyName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        getCompanyDetails(savedInstanceState);

    }


    private void setUpCompanyDetails(Bundle savedInstanceState){
        companyAvatar=(CircleImageView)findViewById(R.id.companyAvatar);
        TextDrawable drawable = TextDrawable.builder()
                .buildRoundRect(Application.getSelectedCompanyObbject().getCompanyName().substring(0, 1), CompanyDetails.this.getResources().getColor(R.color.colorPrimary), 10);

        Drawable d = new BitmapDrawable(ViewUtils.drawableToBitmap(drawable));


        String avatorPath= Application.getSelectedCompanyObbject().getPath()+Application.getSelectedCompanyObbject().getCompanyStorageName()+"/"+Application.getSelectedCompanyObbject().getAvatarLocation();
        String CoverPath= Application.getSelectedCompanyObbject().getPath()+Application.getSelectedCompanyObbject().getCompanyStorageName()+"/"+Application.getSelectedCompanyObbject().getCoverLocation();

        Picasso.with(CompanyDetails.this)
                .load(avatorPath)
                .placeholder(d)
                .into(companyAvatar);

        imageView2=(RelativeLayout)findViewById(R.id.imageView2);

        Picasso.with(this)
                .load(CoverPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        BitmapDrawable background = new BitmapDrawable(bitmap);
                        imageView2.setBackgroundDrawable(background);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        Drawable drawable = CompanyDetails.this.getResources().getDrawable(R.drawable.ic_no_image);
                       imageView2.setBackground(drawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Drawable drawable = CompanyDetails.this.getResources().getDrawable(R.drawable.ic_no_image);
                        imageView2.setBackground(drawable);
                    }
                });

        companyName=(TextView)findViewById(R.id.companyName);
        companyName.setText(Application.getSelectedCompanyObbject().getCompanyName());

        companycategory=(TextView)findViewById(R.id.companycategory);
        companycategory.setText(Application.getSelectedCategoryName());


        ratingBar=(MaterialRatingBar)findViewById(R.id.companyRating);
        ratingBar.setRating(Float.parseFloat(Application.getSelectedCompanyObbject().getCompanyRating()));


        setTabHost(savedInstanceState);

    }

    private void setTabHost(Bundle savedInstanceState){

    // create the TabHost that will contain the Tabs
    TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);
    // tabHost.setup();

    TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
    TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
    TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

    tab1.setIndicator("Details");
    tab1.setContent(new Intent(this,Details.class));

    tab2.setIndicator("Profile");
    tab2.setContent(new Intent(this,Profile.class));

    tab3.setIndicator("Map");
    tab3.setContent(new Intent(this,Map.class));

    /** Add the tabs  to the TabHost to display. */
    tabHost.addTab(tab1);
    tabHost.addTab(tab2);
    tabHost.addTab(tab3);


}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    private void getCompanyDetails(final Bundle savedInstanceState){

        VerifiedCompanies verifiedCompanies = VerifiedCompanies.getCompanyByID(Application.getSelectedCompanyID());
        Application.setSelectedCompanyObbject(verifiedCompanies);
        setUpCompanyDetails(savedInstanceState);

       /* avi.show();



        final HashMap<String, String> params = new HashMap<String, String>();



        params.put("fetch_public_info",""+ "1");
        params.put("scope","company_profile");
        params.put("company_id", Application.getSelectedCompanyID());


       // Log

        Log.d("Params",params+"");

        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                APIRequest.BASE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();

                        try {



                            Log.d("logs",response.toString());

                            formatJSON(response,savedInstanceState);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)   {
                avi.hide();


                //  dialogs.SimpleWarningAlertDialog("Transmission Error", "Connection Failed").show();
                Log.d("volley.Response", error.toString());



                avi.hide();

                if (error instanceof TimeoutError) {
                    // dialogs.SimpleWarningAlertDialog("Network Slacking", "Time Out Error").show();
                    Log.d("volley", "NoConnectionError.....TimeoutError..");


                    //     dialogs.SimpleWarningAlertDialog("Network Slacking", "Time Out Error");



                } else if(error instanceof NoConnectionError){

                    // dialogs.SimpleWarningAlertDialog("No Internet Connections Detected", "No Internet Connection").show();

                }


                else if (error instanceof AuthFailureError) {
                    //  Log.d("volley", "AuthFailureError..");
                    // dialogs.SimpleWarningAlertDialog("Authentication Failure","AuthFailureError").show();


                } else if (error instanceof ServerError) {
                    // dialogs.SimpleWarningAlertDialog("Server Malfunction", "Server Error").show();

                } else if (error instanceof NetworkError) {
                    // dialogs.SimpleWarningAlertDialog("Network Error", "Network Error").show();

                } else if (error instanceof ParseError) {
                    // dialogs.SimpleWarningAlertDialog("Parse Error","Parse Error").show();
                }

            }
        }) {

        };
        int socketTimeout = 480000000;//8 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
        Log.d("oxinbo","Server Logs"+params.toString());*/
    }

}