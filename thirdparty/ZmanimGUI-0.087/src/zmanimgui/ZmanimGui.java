/*
 *  This is part of the Zmanim GUI package.
 *  See ZmanimMain.java for details.
 */


package zmanimgui;

import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*;


//TODO: complex zmanim (unless I give up)

public class ZmanimGui extends javax.swing.JFrame {

    public static String newline = System.getProperty("line.separator");
    private  ComplexZmanimCalendar czc;
    private  String lang;
    public boolean save_confs = false;

        /** Creates new form ZmanimGui */
    public ZmanimGui(String language)
    {
        initComponents();

        //Creates an instance of ComplexZmanimCalendar with a defualt location.
            //(The location will be changed later)
        czc = new ComplexZmanimCalendar(new GeoLocation());

        //Set the global language to the one given by the user as an argument
        lang = language;

        //Sets the icon of the form to "icon.png" in the same folder
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        setIconImage(icon);

        //Set Defualt location to Jerusalem:
        if (lang.compareTo("English") == 0)
            SetLocation("Jerusalem, Israel",31.77805, 35.235149,"Asia/Jerusalem", 800);
        else
            SetLocation("ירושלים, ישראל",31.77805, 35.235149,"Asia/Jerusalem", 800);

        //Override defualt location with the location stored in the conf file, if one exists
        String ConfFileContens = ReadConfFile();
        if (ConfFileContens != null)
        {
            try
            {
                apply_confs(ConfFileContens);
            }
            catch (Exception e)
            {
                System.out.println("Conf file corrupted, using default location");
            }
        }
        else
            System.out.println("No conf file found, using default location and language");

        //Rebuild the form in the language currently being used
        changelang();

        //Set the labels to show the Zmanim
        ShowZmanim();
    }


    //Set the number of minutes before Shkiah candle lighting is
        //Called by the ChangeLocation form
    public void SetCandleLightingOffset(double offset)
    {
        czc.setCandleLightingOffset(offset);
    }

    //Set the location times are taken from
    public void SetLocation(String locationName, double latitude, double longitude, String timezone, double elevation)
    {
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);

        czc.setGeoLocation(location);

        Calendar c = czc.getCalendar();
        c.setTimeZone(timeZone);
        //Reset the date to "Today" when location is changed
        SetDate(c);
              
        //Show the location's name in it's label
        jLabel30.setText(locationName);           
    }
    
    //Set the date times are taken from (to the date of the given calendar)
    private void SetDate(Calendar c)
    {
        czc.setCalendar(c);
        
        //Display the dates:
        
        //Display the hebrew date at the top
        String hebdate;
        hebdate = Taarich.GetHebrewDate( c, lang);
        jLabel85.setText(hebdate);
        
        //In english mode:
        if(lang.compareTo("English") == 0)
        {
            //Create a string representing the gregorian date
            String date = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "  " +
            getMonthName(c.get(Calendar.MONTH)) + " , " +
            Integer.toString(c.get(Calendar.YEAR));
            //Show the date in its label
            jLabel28.setText(date);
        }
        //In hebrew mode, show the hebrew date at the bottem too
        else
            jLabel28.setText(hebdate);
        
        //Show the day of the week in its label
        int day = c.get(Calendar.DAY_OF_WEEK);
        jLabel34.setText(Weekday(day)); 
    }

    //Processes the conf file's contents, and set the date and other conf's from it.
    public void apply_confs(String filecontents)
    {
        Boolean ok = true;
        String LocationName = "";
        double latitude = 0, longitude = 0, elevation = 0, offset = 0;
        String timezone = "";

        String[] lines = filecontents.split(newline);

        if (lines.length == 1)
        {
            set_lang(lines[0]);
            System.out.println("No location defined in conf file, using default location");
        }
        else if (lines.length > 6)
        {
            //Get location
            LocationName = lines[0];

            //Get latitude
            try {
                latitude = Double.parseDouble(lines[1]);
            } catch (NumberFormatException nfe) {
                ok = false;
            }
            //Get longitude
            try {
                longitude = Double.parseDouble(lines[2]);
            } catch (NumberFormatException nfe) {
                ok = false;
            }

            //Get timezone
            timezone = lines[3];

            //Get elevation
            try {
                elevation = Double.parseDouble(lines[4]);
            } catch (NumberFormatException nfe) {
                ok = false;
            }

            //Get candle lighting offset
            try {
                offset = Double.parseDouble(lines[5]);
            } catch (NumberFormatException nfe) {
                ok = false;
            }

            //If all variables are ok
            if (ok == true)
            {
                SetLocation(LocationName, latitude, longitude, timezone, elevation);
                czc.setCandleLightingOffset(offset);
            }
            else
                System.out.println("Conf file corrupted, using default location");

            if (lines.length == 7)
                set_lang(lines[6]);
        }

    }

    //Read the given file, covert from UTF to system's encoding,
    //   and return the contents as a String
    static String ReadConfFile ()
    {

        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream("zmanim.conf");
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            return null;
        }
    }


    //Change the date by the given offset in days (a minus value moves back)
        //And show the new Zmanim
    private void DayOffset(int offset)
    {
        Calendar c;

        c = czc.getCalendar();
        c.add(Calendar.DAY_OF_MONTH, offset);
        
        SetDate(c);
        
        ShowZmanim();
    }
     
    //Change the date by the given offset in months (a minus value moves back)
        //And show the new Zmanim
    private void MonthOffset(int offset)
    {
        Calendar c;

        c = czc.getCalendar();
        c.add(Calendar.MONTH, offset);
        SetDate(c);
        
        ShowZmanim();
    }
    
    //Sets all labels to show their Zmanim
    public void ShowZmanim() {      

         //Alos (Alos 16.1)
         jLabel15.setText(TimeOfDay(czc.getAlos16Point1Degrees()));
         
         //SunRise
         jLabel16.setText(TimeOfDay(czc.getSunrise()));
         
         //Sof Zman Shma MG"A
         jLabel17.setText(TimeOfDay(czc.getSofZmanShmaMGA()));
     
         //Sof Zman Shma GR"A
         jLabel18.setText(TimeOfDay(czc.getSofZmanShmaGRA()));
         
         //Sof Zman Tefila MG"A
         jLabel19.setText(TimeOfDay(czc.getSofZmanTfilaMGA()));
     
         //Sof Zman Tefila GR"A
         jLabel20.setText(TimeOfDay(czc.getSofZmanTfilaGRA()));
         
         //Chatzot
         jLabel21.setText(TimeOfDay(czc.getChatzos()));
          
         //Mincha Gedola
         jLabel22.setText(TimeOfDay(czc.getMinchaGedola()));
          
         //Mincha Ktana
         jLabel23.setText(TimeOfDay(czc.getMinchaKetana()));
          
         //Plag Hamincha
         jLabel24.setText(TimeOfDay(czc.getPlagHamincha()));

         //Shkiah
         jLabel36.setText(TimeOfDay(czc.getSunset()));
          
         //Tzet Hakochavim
         jLabel25.setText(TimeOfDay(czc.getTzais()));
          
         //Tzet Hakochavim (72)
         jLabel26.setText(TimeOfDay(czc.getTzais72()));

         //Show Candle Lighting only on Friday:
         Calendar c = czc.getCalendar();
         if (c.get(Calendar.DAY_OF_WEEK) != 6)
         {
            jLabel37.setVisible(false);
            jLabel38.setVisible(false);
         }
         else
         {
            jLabel37.setVisible(true);
            jLabel38.setVisible(true);
            jLabel38.setText(TimeOfDay(czc.getCandelLighting()));
         }
    }

    //Returns the name of the month (0 - 11)
    String getMonthName(int m)
    {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 )
        {
            month = months[m];
        }
        return month;
    }



    //Returns the name of the weekday
    private String Weekday(int day)
    {
        String Name = "";

        if(lang.compareTo("Hebrew") == 0)
        {
            if (day==1) Name="ראשון";
            if (day==2) Name="שני";
            if (day==3) Name="שלישי";
            if (day==4) Name="רביעי";
            if (day==5) Name="חמישי";
            if (day==6) Name="שישי";
            if (day==7) Name="שבת";
        }
        else
        {
            if (day==1) Name="Sunday";
            if (day==2) Name="Monday";
            if (day==3) Name="Tuesday";
            if (day==4) Name="Wednesday";
            if (day==5) Name="Thursday";
            if (day==6) Name="Friday";
            if (day==7) Name="Saturday";
        }
        return Name;
    }
    
    //Returns a string representing the given date
    private String TimeOfDay(java.util.Date date)
    {
        if(date != null)
        {
            GregorianCalendar gc = new GregorianCalendar();
            String Time="";
            Boolean IsPM;
            int hour, minute,second;
            IsPM = false;
            gc.setTime(date);
            hour = gc.get(GregorianCalendar.HOUR_OF_DAY);

            //Take 12 off numbers greater than 12, and mark as PM
            if (hour > 12)
            {
                IsPM = true;
                hour = hour - 12;
            }
            minute =  gc.get(GregorianCalendar.MINUTE);
            second = gc.get(GregorianCalendar.SECOND);

            Time =Integer.toString(hour) + ":" +  StringifyTwoDigits(minute) + ":" +  StringifyTwoDigits(second);
            if (IsPM == true)
                Time = Time + " PM";
            else
                 Time = Time + " AM";

            return Time;
        }
        //In some places some time are not available every day, so "N/A" is shown.
            //(I.E. , in Alaska there isn't sunrise and sunset every day.)
        else
            return "N/A";
    }
     
    //Returns a string representing the given number, with at least 2 digits (a "0" is added if neded)
    private String StringifyTwoDigits(int num){
        String str = "";
        str = Integer.toString(num);
        if (str.length() == 1)
            str = "0" + Integer.toString(num);
        return str;
       
    }

    //Change all contents of the form to the language in the public variable 'lang'
    private void changelang()
    {
        if (lang.compareTo("English") == 0)
        {
            setTitle("Zmanim GUI");

            jLabel28.setText("English Date");

            jLabel3.setText("Alos");
            jLabel4.setText("Sof Zman Shema GR\"A");
            jLabel5.setText("Sunrise");
            jLabel6.setText("Sof Zman Tefila MG\"A");
            jLabel7.setText("Sof Zman Shema MG\"A");
            jLabel1.setText("Zman");
            jLabel2.setText("Time");
            jLabel8.setText("Sof Zman Tefila GR\"A");
            jLabel9.setText("Chatzot");
            jLabel10.setText("Mincha Gedola");
            jLabel11.setText("Mincha Ketana");
            jLabel12.setText("Plag Hamincha");
            jLabel13.setText("Tzait");
            jLabel14.setText("Tzait 72");
            jLabel35.setText("Shkiah");
            jLabel37.setText("Candle Lighting");
            jLabel34.setText("Day of week");

            jLabel85.setText("Hebrew Date");
            jLabel29.setText("Zmanim Gui");

            jMenu1.setText("Preferences");
            jMenuItem2.setText("Set Location");
            jMenuItem3.setText("Hebrew Mode");
            jMenu2.setText("Help");
            jMenuItem1.setText("About");

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.white, null), "Zmanim", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nachlieli CLM", 1, 12)));

            jMenuBar1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jMenu1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jMenu2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jMenuItem1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jMenuItem2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jMenuItem3.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

            jPanel1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jPanel3.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);


        }
        else
        {
            setTitle("זמני היום");

            jLabel29.setText("זמני היום");

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.white, null), "זמנים", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nachlieli CLM", 1, 12)));


            jLabel1.setText("זמן");
            jLabel2.setText("שעה");

            jLabel3.setText("עלות השחר");
            jLabel5.setText("זריחה");
            String  dblquote  = "\"";
            jLabel7.setText("סוף זמן ק" + dblquote + "ש מג" + dblquote + "א");
            jLabel4.setText("סוף זמן ק" + dblquote + "ש גר" + dblquote + "א");
            jLabel6.setText("סוף זמן תפילה" + " מג" + dblquote + "א");
            jLabel8.setText("סוף זמן תפילה" + " גר" + dblquote + "א");

            jLabel9.setText("חצות");
            jLabel10.setText("מנחה גדולה");
            jLabel11.setText("מנחה קטנה");
            jLabel12.setText("פלג המנחה");
            jLabel35.setText("שקיעה");
            jLabel13.setText("צאת הכוכבים");
            jLabel14.setText("צאת הכוכבים ר" + dblquote + "ת");
            jLabel37.setText("הדלקת נרות");

            
            jMenu1.setText("הגדרות");
            jMenuItem2.setText("שנה מיקום");
            jMenuItem3.setText("ממשק אנגלי");
            jMenu2.setText("עזרה");
            jMenuItem1.setText("אודות");

            jMenuBar1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMenuItem1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMenuItem2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMenuItem3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMenu1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMenu2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            jPanel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jPanel3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        //Just to show the date (in the new language):
        Calendar c;
        c = czc.getCalendar();
        SetDate(c);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.gray);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel31.setText(">"); // NOI18N
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        jLabel32.setText(">>"); // NOI18N
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel32MouseClicked(evt);
            }
        });

        jLabel27.setText("<"); // NOI18N
        jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel27MouseClicked(evt);
            }
        });

        jLabel33.setText("<<"); // NOI18N
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel28.setText("English Date"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addGap(12, 12, 12)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel27)
                    .addComponent(jLabel31)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.white, null), "Zmanim", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nachlieli CLM", 1, 12))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));

        jLabel3.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel3.setText("Alos"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel4.setText("Sof Zman Shema GR\"A"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel5.setText("Sunrise"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel6.setText("Sof Zman Tefila MG\"A"); // NOI18N

        jLabel7.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel7.setText("Sof Zman Shema MG\"A"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Nachlieli CLM", 1, 22));
        jLabel1.setText("Zman"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Nachlieli CLM", 1, 22));
        jLabel2.setText("Time"); // NOI18N

        jLabel8.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel8.setText("Sof Zman Tefila GR\"A"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel9.setText("Chatzot"); // NOI18N

        jLabel10.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel10.setText("Mincha Gedola"); // NOI18N

        jLabel11.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel11.setText("Mincha Ketana"); // NOI18N

        jLabel12.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel12.setText("Plag Hamincha"); // NOI18N

        jLabel13.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel13.setText("Tzait"); // NOI18N

        jLabel14.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel14.setText("Tzait 72"); // NOI18N

        jLabel15.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("jLabel15"); // NOI18N

        jLabel16.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("jLabel16"); // NOI18N

        jLabel17.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("jLabel17"); // NOI18N

        jLabel18.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("jLabel18"); // NOI18N

        jLabel19.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("jLabel19"); // NOI18N

        jLabel20.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("jLabel20"); // NOI18N

        jLabel21.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("jLabel21"); // NOI18N

        jLabel22.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("jLabel22"); // NOI18N

        jLabel23.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("jLabel23"); // NOI18N

        jLabel24.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("jLabel24"); // NOI18N

        jLabel25.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("jLabel25"); // NOI18N

        jLabel26.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("jLabel26"); // NOI18N

        jLabel35.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel35.setText("Shkiah"); // NOI18N

        jLabel36.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("jLabel36"); // NOI18N

        jLabel37.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel37.setText("Candle Lighting"); // NOI18N

        jLabel38.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("jLabel38"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel37)
                    .addComponent(jLabel35)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(jLabel25)
                    .addComponent(jLabel17)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel16)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel2)
                    .addComponent(jLabel26)
                    .addComponent(jLabel36))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addContainerGap())
        );

        jLabel34.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel34.setText("Day of week");

        jLabel30.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel30.setText("Location"); // NOI18N

        jLabel85.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jLabel85.setText("Hebrew Date"); // NOI18N

        jLabel29.setFont(new java.awt.Font("David CLM", 1, 24));
        jLabel29.setText("Zmanim Gui"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel29)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30))
                .addContainerGap())
        );

        jMenu1.setText("Preferences");
        jMenu1.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));

        jMenuItem2.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jMenuItem2.setText("Set Location");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jMenuItem3.setText("Hebrew Mode");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");
        jMenu2.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));

        jMenuItem1.setFont(new java.awt.Font("Nachlieli CLM", 0, 12));
        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Window1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    }//GEN-LAST:event_formWindowActivated

    //Forward arrow
    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // Advance date by one day in english mode:
        if (lang.compareTo("English") == 0)
            DayOffset(1);
        // Go back one day in hebrew mode:
        else
            DayOffset(-1);
    }//GEN-LAST:event_jLabel31MouseClicked

    //Backwards arrow
    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        // Go back one day in english mode:
        if (lang.compareTo("English") == 0)
            DayOffset(-1);
        // Advance date by one day in hebrew mode:
        else
            DayOffset(1);
    }//GEN-LAST:event_jLabel27MouseClicked

     //Double forward arrow
    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        // Advance date by one month in english mode:
        if (lang.compareTo("English") == 0)
            MonthOffset(1);
        // Go back one month in hebrew mode:
        else
            MonthOffset(-1);
    }//GEN-LAST:event_jLabel32MouseClicked

     //Double Backwards arrow
    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // Go back one month in english mode:
        if (lang.compareTo("English") == 0)
            MonthOffset(-1);
        // Advance date by month day in hebrew mode:
        else
            MonthOffset(1);
    }//GEN-LAST:event_jLabel33MouseClicked

    //Location change dialog:
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        GeoLocation g = czc.getGeoLocation();
        ChangeLocation c = new ChangeLocation(this, g.getLocationName(), g.getLatitude(), g.getLongitude(),g.getTimeZone().getID(),g.getElevation(),czc.getCandleLightingOffset(), lang);
        c.setModal(true);
        c.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    //Language switch option:
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (lang.compareTo("Hebrew") == 0)
            lang = "English";
        else
            lang = "Hebrew";
        changelang();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    //About form:
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        About about = new About(lang);
        about.setVisible(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Save the language and the conf file
        save_conf_and_lang();
    }//GEN-LAST:event_formWindowClosing

    // Add the language to the conf file
    private void save_conf_and_lang()
    {
        //Read the file
        String [] lines = {""};

        String conffilecontents = ReadConfFile();
        if (conffilecontents != null)
        lines = conffilecontents.split(newline);
        String filetext;

        //If there is no file, or the file has only the lang in it, ignore it.
            //(And the new file will erase the old one)
        if (lines.length < 6)
        {
            filetext = "";
        }
        else //Else, keep all data besides the language
        {
            filetext = lines[0] + newline + lines[1] + newline + lines[2] + newline + lines[3] + newline + lines[4] + newline + lines[5] + newline;
        }
        filetext += lang;

        //Save the file:
        try
        {
            FileOutputStream fos = new FileOutputStream("zmanim.conf");
            //Save in unicode
            Writer out = new OutputStreamWriter(fos, "UTF8");
            out.write(filetext);
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveconffile(String LocationName, double latitude, double longitude, String timezone, double elevation, double cloffset)
    {
        String filetext="";
        filetext = LocationName + newline + latitude + newline + longitude + newline + timezone + newline + elevation + newline + cloffset ;

        try
        {
            FileOutputStream fos = new FileOutputStream("zmanim.conf");
            //Save in unicode
            Writer out = new OutputStreamWriter(fos, "UTF8");
            out.write(filetext);
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

//Gui variables, auto generated by NetBeans:
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables

    private void set_lang(String language) {
        if (language.compareTo("English") == 0)
            lang = "English";
        else
            lang = "Hebrew";
        changelang();
    }
}


