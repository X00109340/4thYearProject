package com.example.shaju.letzchat.main_page.view;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.google.firebase.storage.StorageReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.users_page.model.User;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Shajun on 16/03/2017.
 */

public class MainPageView extends CoordinatorLayout implements MainPageDisplayer {

    //Navigation drawer
    private DrawerLayout drawer;

    //Different views - profile
    private CircleImageView profileImageView;
    private NavigationView navigationView;
    private NavigationView logoutView;

    //Toolbar
    private Toolbar toolbar;

    //TextView
    private TextView emailTextView;
    private TextView nameTextView;

    //Search view
    private MaterialSearchView materialSearchView;

    private DrawerClickActionListeners drawerClickActionListeners;
    private NavigationActionListener navigationActionListener;
    private SearchUserActionListener searchUserActionListener;

    //constructor
    public MainPageView(Context contextIN, AttributeSet attributeSetIN)
    {
        super(contextIN, attributeSetIN);
    }

    //on xml load
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set the view
        View.inflate(getContext(), R.layout.merge_main_view, this);

        //Set the main_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //main_toolbar icon to open
        toolbar.setNavigationIcon(R.drawable.toolbar_ham_sign);

        //drawer layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        logoutView = (NavigationView) navigationView.findViewById(R.id.logout_view);
        View headerLayout = navigationView.getHeaderView(0);
        profileImageView = (CircleImageView) headerLayout.findViewById(R.id.profileImageView);

        //Textviews
        nameTextView = (TextView) headerLayout.findViewById(R.id.nameTextView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.emailTextView);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
    }

    //attach drawer listner, navigation listener
    @Override
    public void attach(final DrawerClickActionListeners drawerClickActionListenersIN, NavigationActionListener navigationActionListenerIN, SearchUserActionListener searchUserActionListenerIN) {
        navigationActionListener = navigationActionListenerIN;
        drawerClickActionListeners = drawerClickActionListenersIN;
        searchUserActionListener = searchUserActionListenerIN;

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //Toolbar on click listener
        toolbar.setNavigationOnClickListener(navigationClickListener);

        //Top header on click listeners
        profileImageView.setOnClickListener(headerClickListener);
        nameTextView.setOnClickListener(headerClickListener);
        emailTextView.setOnClickListener(headerClickListener);

        //navigation view on click listener
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        logoutView.setNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    //Remove all these listeners when the drawer is closed
    @Override
    public void detach(DrawerClickActionListeners drawerClickActionListenersIN, NavigationActionListener navigationActionListenerIN, SearchUserActionListener searchUserActionListenerIN) {
        toolbar.setNavigationOnClickListener(null);

        profileImageView.setOnClickListener(null);
        nameTextView.setOnClickListener(null);
        emailTextView.setOnClickListener(null);

        navigationView.setNavigationItemSelectedListener(null);
        logoutView.setNavigationItemSelectedListener(null);

    }



    //SET USER DETAILS IN THE PROFILE HEADER VIEW ON DRAWER LAYOUT
    @Override
    public void setUserDetails(User userIN) {
        //Set profile picture
        loadImageElseWhite(userIN.getImage(),profileImageView,getContext());
        //set name(ID)
        nameTextView.setText(userIN.getName());
        //set Email
        emailTextView.setText(userIN.getEmail());
    }

    @Override
    public void inflateMenu() {
        if (!toolbar.getMenu().hasVisibleItems())
        {
            toolbar.inflateMenu(R.menu.fragment_users_itemlist);
            materialSearchView.setOnQueryTextListener(queryTextListener);

            MenuItem item = toolbar.getMenu().findItem(R.id.search_user);
            materialSearchView.setMenuItem(item);
        }
    }

    //Set main_toolbar title - NOT USED**
    @Override
    public void setTitle(String titleIN)
    {
        toolbar.setTitle(titleIN);
    }


    @Override
    public boolean onBackPressed() {
        if (materialSearchView.isSearchOpen())
        {
            materialSearchView.closeSearch();
            return true;
        }
        return false;
    }

    //Open drawer
    @Override
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    //Close drawer
    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void clearMenu() {
        toolbar.getMenu().clear();
    }


    private MaterialSearchView.OnQueryTextListener queryTextListener = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String userName)
        {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String queryIN) {
            searchUserActionListener.displayFilteredUserList(queryIN);
            return false;
        }
    };


    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItemIN) {
            drawerClickActionListeners.navigationMenuItemSelected(menuItemIN);
            return true;
        }
    };

    //Open drawer
    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            navigationActionListener.onHamburgerPressed();
        }
    };

    //Open profile activity
    private final OnClickListener headerClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            drawerClickActionListeners.headerProfileSelected();
        }
    };


    public static void loadImageElseWhite(String imageIN, CircleImageView circleImageViewIN, Context contextIN) {

        try {
            if (imageIN != null && imageIN.length() > 0) {
                StorageReference ref = ChatDependencies.INSTANCE.getStorageService().getProfileImageReferenceFromStorage(imageIN);
                Glide.with(contextIN)
                        .using(new FirebaseImageLoaderService())
                        .load(ref)
                        .into(circleImageViewIN);
            } else
            {
                Glide.with(contextIN)
                        .load("")
                        .placeholder(R.drawable.person_circlewhite)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(circleImageViewIN);
            }
        } catch (IllegalArgumentException e) {

        }

    }



}

