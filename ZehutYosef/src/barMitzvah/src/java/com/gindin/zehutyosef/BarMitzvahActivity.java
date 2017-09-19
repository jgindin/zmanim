package com.gindin.zehutyosef;

import com.gindin.zehutyosef.berachot.BirkatAmmazon;
import com.gindin.zehutyosef.berachot.MeenShalosh;
import com.gindin.zehutyosef.settings.Settings;
import com.gindin.zehutyosef.tefilla.Arvit;
import com.gindin.zehutyosef.tefilla.Mincha;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.HebrewDateFormatter;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BarMitzvahActivity
  extends ZehutYosefActivity {

    protected void addSections() {
        sections.add( new Mincha() );
        sections.add( new Arvit() );
        sections.add( new BirkatAmmazon() );
        sections.add( new MeenShalosh() );
    }

}
