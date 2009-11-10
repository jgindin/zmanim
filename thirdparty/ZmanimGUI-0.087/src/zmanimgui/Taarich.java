/*
 * $Id: Taarich.java,v 1.39 2003/08/16 08:06:17 rl Exp $
 **********************************************************
 * gauss
 *
 *  Hebrew calendar calculations using Gauss formula for Passover.
 *  Copyright © 1998-2003 Dr. Zvi Har’El <rl@math.technion.ac.il>
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *  
 *  3. The end-user documentation included with the redistribution,
 *     if any, must include the following acknowledgment:
 *  	“This product includes software developed by
 *  	 Dr. Zvi Har’El (http://www.math.technion.ac.il/~rl/).”
 *     Alternately, this acknowledgment may appear in the software itself,
 *     if and wherever such third-party acknowledgments normally appear.
 *  
 *  This software is provided ‘as-is’, without any express or implied
 *  warranty.  In no event will the author be held liable for any
 *  damages arising from the use of this software.
 *
 *  Author:
 *	Dr. Zvi Har’El,
 *	Deptartment of Mathematics,
 *	Technion, Israel Institue of Technology,
 *	Haifa 32000, Israel.
 *	E-Mail: rl@math.technion.ac.il
 * 
 *  Minor changes (for usages in ZmanimGui) by Moshe Wagner 
 **********************************************************
 */

package zmanimgui;

import java.util.Calendar;



/**
 * An instance of the class Taarich represents an Hebrew date.
 *
 * @author  <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: Taarich.java,v 1.39 2003/08/16 08:06:17 rl Exp $
 * @see	    <a href="Taarich.java">Class source code</a>
 */
public class Taarich {

    
    public static String GetHebrewDate(Calendar c, String lang)
    {
        int day,month,year;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);

        Taarich taarich = new Taarich(day, month, year);
        
        if(lang.compareTo("Hebrew") == 0)
            return taarich.toHebrew();
        else
            return taarich.toString();
    }
    /**
    * Hebrew day-of-the-month.
    */
    private int d;

    /**
    * Hebrew month. The months are counted from Nisan, Adar being the
    * twelveth. In a leap year, we denote the two Adars by 13 and 14.
    */
    private int m;

    /**
    * Hebrew year.
    */
    private int y;

    /**
    * Create a Taarich object representing the Hebrew equivalent of a given
    * Gregorian date. Dates before 15 October 1582 are considered Julean.
    *
    * @param	d   the Gregorian day-of-the-month
    * @param	m   the Gregorian month
    * @param	y   the Gregorian year. The year Y BC is referred to as -Y+1.
    */
    public Taarich(int d, int m, int y) {
	int pesach, kvia, hy;
	boolean g;


	if (d <= 0 || d > 31 || m <=0 || m > 12)
	    throw new IllegalArgumentException();
	g = (y > 1582 || y == 1582 && m > 10 || y == 1582 && m == 10 &&
	    d >= 15);
	if ((m -= 2) <= 0) { //January or February
	    m += 12;
	    y--;
	}

        
	d += 7*m/12 + 30*(m - 1); //day in March
	pesach = new Gauss(hy = y + 3760, g).date;
	if (d <= pesach - 15) {//before 1 Nisan
	    kvia = pesach;
	    d += 365;
	    if (y%4 == 0 && (!g || y%100 != 0) || g && y%400 == 0)
		d++;
	    y--;
	    pesach = new Gauss(--hy, g).date;
	} else kvia = new Gauss(hy + 1, g).date;
	d -= pesach - 15; //day in Nisan
	kvia -= pesach - 12; //length of hebrew year minus 353: 0,1,2,30,31,32
	y++;
	if (y%4 == 0 && (!g || y%100 != 0) || g && y%400 == 0) kvia++;
	for (m = 0; m < 11; m++) {
	    int days;
	    if (m == 7 && kvia%30 == 2) days = 30; //Heshvan
	    else if (m == 8 && kvia%30 == 0) days = 29; //Kislev
	    else days = 30 - m%2;
	    if (d <= days) break;
	    d -= days;
	}
	if (m == 11 && kvia >= 30) {//Adar in a leap year
	    m++;
	    if (d > 30) {
		d -= 30;
		m++;
	    }
	}
	if (m >= 6) hy++; //after Rosh Hashana
	this.d = d;
	this.m = m;
	this.y = hy;
    }

    /**
    * The Hebrew months names, as UNICODE Hebrew character strings.
    */
    public static final String hmonths[] = {//LRO‭
	"ניסן", "איר", "סיון", "תמוז", "אב", "אלול",
	"תשרי", "חשון", "כסלו", "טבת", "שבט", "אדר", "אדר א", "אדר ב"
    };

    /**
    * The Hebrew month names in Latin transliteration.
    */
    public static final String months[] = {
	"Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri", "Heshvan",
	"Kislev", "Tevet", "Shevat", "Adar", "Adar I", "Adar II"
    };

    /**
    * Return a Hebrew date as a UNICODE Hebrew character string.
    */
    public String toHebrew() {
	return new Alef(d) + " " + hmonths[m] + " " + new Alef(y);
    }

    /**
    * Return a Hebrew date in Latin transliteration as a character string.
    */
    @Override
    public String toString() {
	return d + " " + months[m] + " " + y;
    }
}
