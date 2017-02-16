/*
 * $Id: Alef.java,v 1.30 2003/08/16 08:05:46 rl Exp $
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
 *    “This product includes software developed by
 *     Dr. Zvi Har’El (http://www.math.technion.ac.il/~rl/).”
 *     Alternately, this acknowledgment may appear in the software itself,
 *     if and wherever such third-party acknowledgments normally appear.
 *  
 *  This software is provided ‘as-is’, without any express or implied
 *  warranty.  In no event will the author be held liable for any
 *  damages arising from the use of this software.
 *
 *  Author:
 *  Dr. Zvi Har’El,
 *    Deptartment of Mathematics,
 *    Technion, Israel Institue of Technology,
 *    Haifa 32000, Israel.
 *    E-Mail: rl@math.technion.ac.il
 **********************************************************
 */

package zmanimgui;

/**
 * Hebrew numerals.
 *
 * @author  <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: Alef.java,v 1.30 2003/08/16 08:05:46 rl Exp $
 * @see     <a href="Alef.java">Class source code</a>
 */
public class Alef {
    public static final String digits[][] = {//LRO‭
  { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט" },
  { "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ" },
  { "ק", "ר", "ש", "ת", "תק", "תר", "תש", "תת", "תתק" }
    };
    public static final String special[] = { "טו", "טז"};
    private StringBuffer s;
    public Alef(int n) {
  int i = 0;

  s = new StringBuffer();
  while (n != 0) {
      if (i == 3) {i = 0; s.insert(0, '׳'); }
      if (i == 0 && (n%100 == 15 || n%100 == 16)) {
    s.insert(0, special[n%100 - 15]);
    n /= 100;
    i = 2;
      } else {
    if (n%10 != 0) s.insert(0, digits[i][n%10 - 1]);
    n /= 10;
    i++;
      }
  }
    }
    public String toString() {
  return s.toString();
    }
}
