/*
 * $Id: Gauss.java,v 1.25 2003/08/16 08:06:10 rl Exp $
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
 *    Dr. Zvi Har’El,
 *    Deptartment of Mathematics,
 *    Technion, Israel Institue of Technology,
 *    Haifa 32000, Israel.
 *    E-Mail: rl@math.technion.ac.il
 **********************************************************
 */
package zmanimgui;

/**
 * March date and day-of-the-week for Passover by Gauss formula.
 *
 * @author  <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: Gauss.java,v 1.25 2003/08/16 08:06:10 rl Exp $
 * @see     <a href="Gauss.java">Class source code</a>
 */
public class Gauss {
    private static final double T = 33. + 14. / 24.;
    private static final double L = (1. + 485. / 1080.) / 24. / 19.;
    private static final double K = (29. + (12. + 793. / 1080.) / 24. )/ 19.;
    private static final double m0 = T - 10. * K + L + 14.;
    private static final double m1 = (21. + 589. / 1080.) / 24.;
    private static final double m2 = (15. + 204. / 1080.) / 24.;
    public int day, date;
    public Gauss(int year, boolean g) {
  int a,b,c,M;
  double m;

  a = (12 * year + 17) % 19;
  b = year % 4;
  m = m0 + K * a + b / 4. - L * year;
  if (m < 0) m--;
  M = (int) m;
  if (m < 0) m++;
  m -= M;
  switch (c = (M + 3 * year + 5 * b + 5) % 7) {
  case 0:
      if (a <= 11 || m < m1) break;
      c = 1; M++; break;
  case 1:
      if (a <= 6 || m < m2) break;
      c = 3; M += 2; break;
  case 2:
      c = 3; M++; break;
  case 4:
      c = 5; M++; break;
  case 6:
      c = 0; M++; break;
  }
  day = c;
  if (g) // Gregorian Calendar
      M += (year - 3760) / 100 - (year-3760) / 400 - 2;
  date = M;
    }
}
