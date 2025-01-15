package com.example.resort;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resort.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.resort.Adapters.Rec_HotelsAdapter;
import com.example.resort.Adapters.SliderAdapter;
import com.example.resort.Decoration.GridSpacingItemDecoration;
import com.example.resort.Util.Bookings;
import com.example.resort.Util.CurrentUser;
import com.example.resort.Util.Drafts;
import com.example.resort.Util.Hotel;
import com.example.resort.Util.HotelView;
import com.example.resort.Util.Reader;
import com.example.resort.Util.Writer;

import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;

public class HotelViewer extends AppCompatActivity {
    public String hotelName ;
    public List<Hotel> hotels;
    public Hotel hotel = null;
    SliderView sliderView;
    private List<HotelView> hotelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Rec_HotelsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_viewer);
        Intent intent = getIntent();
        hotelName = intent.getStringExtra("hotelname");
        sliderView = findViewById(R.id.imageSlider);


        //Initializing Image Slider
        final SliderAdapter adapter = new SliderAdapter(this);
        adapter.setCount(2);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.DRAG_FLAG_GLOBAL);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
        setValues(hotelName,this);

        //Initializing Recommendation RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rec_recycler_view);
        mAdapter = new Rec_HotelsAdapter(getApplicationContext(),hotelList);
//        RecyclerView.LayoutManager mLayoutManager =
//                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareHotelData();
    }


    private Bookings getBooked(){
        /* returns booked hotel for the user*/
        ArrayList<Bookings> bookings = Reader.getBookingsList(getApplicationContext());
        if(bookings == null)
            return null;
        for(Bookings book : bookings){
            String name = book.getName();
            if(name == null) continue;

            if(name.equals(CurrentUser.username))
            {

                return book;

            }
        }

        return null;
    }
    public void prepareHotelData(){
        /*PREPARE HOTEL LIST FOR RECOMMENDING TO USER BASED ON ITS PREVIOUS BOOKINGS*/
        int[] cover = {R.drawable.hicon6,
                R.drawable.hicon7,
                R.drawable.hicon8,
                R.drawable.hicon9};
        TextView title = findViewById(R.id.View_title);
        TextView feats = findViewById(R.id.View_features);
        String[] feat_text = feats.getText().toString().split(": ");
        String[] curr_feats = feat_text[1].split(" , ");

        TextView locs = findViewById(R.id.View_location);
        String[] locs_text = locs.getText().toString().split(": ");
        Random random = new Random();
        Bookings booking = getBooked();
        HotelView hotelview ;
        List<Hotel> hotels = Reader.getRestaurantList(getApplicationContext());

        if(booking==null)
        {
            for(Hotel h : hotels) {
                int idx = random.nextInt(12);
                idx = idx%4;
                if(h.getLocation().equalsIgnoreCase(locs_text[1]) && !h.getName().equalsIgnoreCase(title.getText().toString())) {
                    hotelview = new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats(),h.getUrl());
                    hotelList.add(hotelview);
                }
            }
        } else {
            List<Integer> ids = booking.getId();
            for(Hotel h : hotels) {
                int idx = random.nextInt(12);
                idx = idx%4;

                if(!ids.contains(h.getId())) {
                    if((h.getFeatures().contains(curr_feats[0]) || h.getFeatures().contains(curr_feats[1]))
                            && (!h.getName().equalsIgnoreCase(title.getText().toString()))) {
                        hotelview = new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats(),h.getUrl());
                        hotelList.add(hotelview);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setValues(String hotelName, Context context){
        /*SETTING VALUES TO THE LAYOUT COMPONENTS*/
        TextView viewtitle , viewlocation , viewrating , viewfeature , viewcontact,viewurl;
        final Button viewbook,viewsave,viewgmap;
        viewtitle = findViewById(R.id.View_title);
        viewlocation = findViewById(R.id.View_location);
        viewrating = findViewById(R.id.View_rating);
        viewfeature = findViewById(R.id.View_features);
        viewcontact = findViewById(R.id.View_contact);
        viewbook = findViewById(R.id.View_book);
        viewsave = findViewById(R.id.View_save);
        viewurl = findViewById(R.id.url);
        viewgmap=findViewById(R.id.gmap);
//        viewurl = findViewById(R.id.View_url);
        hotels = Reader.getRestaurantList(context);

        for(Hotel hot : hotels) {
            if(hot.getName().equalsIgnoreCase(hotelName)) {
                hotel = hot;
                break;
            }
        }
        viewtitle.setText(hotel.getName());
        viewlocation.setText("Locations: "+ hotel.getLocation());
        viewfeature.setText("Features: "+hotel.getFeats());
        viewrating.setText("User Rating: "+hotel.getRating());
        viewcontact.setText("Contact: "+hotel.getContact());
//        Toast.makeText(getApplicationContext(),"url="+hotel.getUrl(),Toast.LENGTH_LONG).show();

        viewurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = hotel.getUrl();

                Toast.makeText(getApplicationContext()," "+ str,Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                startActivity(browserIntent);
            }
        });
        viewgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = hotel.getgmap();

                Toast.makeText(getApplicationContext(),"Gmap"+ str,Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                startActivity(browserIntent);
            }
        });


        // ON CLICK LISTNER FOR Saving BUTTON
        viewbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stat = viewbook.getText().toString();
                if(stat.equalsIgnoreCase("Save"))
                {
                    viewbook.setText("Saved");
                    int index = -1;
                    ArrayList<Bookings> bookings = Reader.getBookingsList(getApplicationContext());
                    Bookings firstBooking = null;
                    if(bookings == null) //CHECK IF NO BOOKING HAS BEEN DONE BY ANY USER
                    {
                        firstBooking = new Bookings();
                        firstBooking.setName(CurrentUser.username);
                        List<Integer> ids = new ArrayList<>();
                        ids.add(hotel.getId());
                        firstBooking.setId(ids);
                        bookings = new ArrayList<>();
                        bookings.add(firstBooking);
                        Writer.writeBookings(getApplicationContext(), bookings);
                    }
                    else {
                        boolean status = false;
                        for (Bookings book : bookings) {
                            //FINDING ALL THE BOOKINGS FOR THE CURRENTUSER
                            if(book.getName() == null) continue;
                            if (book.getName().equalsIgnoreCase(CurrentUser.username)) {
                                status = true;
                                firstBooking = book;
                                index = bookings.indexOf(book);

                                break;
                            }
                        }
                        if (status) {
                            //USER ALREADY HAS SOME BOOKING
                            List<Integer> ids = firstBooking.getId();
                            if(ids.contains(hotel.getId()))
                                return;
                            ids.add(hotel.getId());
                            bookings.remove(index);
                            firstBooking.setId(ids);
                            bookings.add(firstBooking);

                        } else {
                            //USER BOOKING FOR FIRST TIME
                            firstBooking = new Bookings();
                            firstBooking.setName(CurrentUser.username);
                            List<Integer> ids = new ArrayList<>();
                            ids.add(hotel.getId());
                            firstBooking.setId(ids);
                            bookings.add(firstBooking);
                        }
                        Writer.writeBookings(getApplicationContext(), bookings);
                    }

                }
                else
                        Toast.makeText(getApplicationContext(),"Already Saved!",Toast.LENGTH_LONG).show();
            }
        });
        viewsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = hotel.getContact();
                callPhoneNumber(str);
//
                    Toast.makeText(getApplicationContext(),"Calling Owner "+ str,Toast.LENGTH_LONG).show();
            }
        });
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//    {
//        if(requestCode == 101)
//        {
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                callPhoneNumber();
//            }
//        }
//    }

    public void callPhoneNumber(String number)
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HotelViewer.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hotel, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_bookings:
                Bookings booking = getBooked();
                List<Hotel> hotels = Reader.getRestaurantList(getApplicationContext());
                if(booking==null)
                    Toast.makeText(getApplicationContext(),"No Bookings for you",Toast.LENGTH_LONG).show();
                else {
                    List<Integer> ids = booking.getId();
                    String res = "";
                    for(Hotel h : hotels) {

                        if(ids.contains(h.getId())) {
                            res = res + h.getName() +"\n";
                        }
                    }
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}