package com.example.resort;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.resort.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.resort.Adapters.HotelsAdapter;
import com.example.resort.Decoration.GridSpacingItemDecoration;
import com.example.resort.Util.Bookings;
import com.example.resort.Util.CurrentUser;
import com.example.resort.Util.Hotel;
import com.example.resort.Util.HotelView;
import com.example.resort.Util.Reader;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelsAdapter adapter;
    private List<HotelView> hotelList;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        username = CurrentUser.username;
        initCollapsingToolbar();

        recyclerView =  findViewById(R.id.recycler_view);

        hotelList = new ArrayList<>();
        adapter = new HotelsAdapter(this, hotelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareHotels();

        try {
            Glide.with(this).load(R.drawable.bg).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;

                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private Bookings getBooked(){
        ArrayList<Bookings> bookings = Reader.getBookingsList(getApplicationContext());
        if(bookings == null)
            return null;
        for(Bookings book : bookings){
            String name = book.getName();
            if(name == null) continue;
            if(name.equals(username))
            {

                return book;

            }
        }

        return null;
    }
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void prepareHotels() {
//        int[] cover = {R.drawable.back,
//
//            R.drawable.hicon5,R.drawable.hicon6,R.drawable.hicon7,R.drawable.hicon8,
//                R.drawable.hicon9,R.drawable.hicon10,R.drawable.hicon11,R.drawable.hicon12,R.drawable.hicon13,R.drawable.hicon14};
//        Random random = new Random();
       Bookings booking = getBooked();
        HotelView hotelview ;
        List<Hotel> hotels = Reader.getRestaurantList(getApplicationContext());
//        Toast.makeText(getApplicationContext(),"abc: "+getResId("hicon10", R.drawable.class),Toast.LENGTH_LONG).show();



        if(booking==null)
        {
        for(Hotel h : hotels) {
//            int idx = random.nextInt(12);
//            idx = idx%11;
                hotelview = new HotelView(h.getName(),h.getLocation(),getResId(h.getImg(), R.drawable.class),h.getRating(),h.getFeats(),h.getUrl());
                hotelList.add(hotelview);
            }
        } else {
            List<Integer> ids = booking.getId();
            for(Hotel h : hotels) {
//                int idx = random.nextInt(12);
//                idx = idx%4;

                if(!ids.contains(h.getId())) {
                    hotelview = new HotelView(h.getName(), h.getLocation(),getResId(h.getImg(), R.drawable.class), h.getRating(), h.getFeats(),h.getUrl());
                    hotelList.add(hotelview);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public void onBackPressed() {
        finishAffinity();

        System.exit(0);
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
                    Toast.makeText(getApplicationContext(),"No Saved Resorts for you",Toast.LENGTH_LONG).show();
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
            case R.id.clear:

                Reader.clearpref(getApplicationContext());


                Toast.makeText(getApplicationContext(),"cleared",Toast.LENGTH_LONG).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

