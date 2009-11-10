/*
*  ZmanimGui - Simple Gui for the Zmanim API.
*  
*  Created by Moshe Wagner (moshe.wagner@gmail.com)
*
*  ZmanimGui, and the Zmanim API programs are free software:
*  you can redistribute it and/or modify  them under the terms of the GNU General Public License
*  as published by the Free Software Foundation, either version 2 of the License, or
*  (at your option) any later version.

*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.

*  You should have received a copy of the GNU General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*
*  The Hebrew Date is produced using the Taarich class by Dr. Zvi Har’El,
*  See "Taarich.java", "Gauss.java", and "Alef.java"  for details.
*
*   The icon provided with ZmanimGUI is from the "Oxygen" icon package,
*   Licensed under the  LGPL license. see "www.oxygen-icons.org" for further details.
*/

package zmanimgui;

public class ZmanimMain
{
    public static void main(String[] args)
    {
        ZmanimGui z;
        //Defualt language is Hebrew.
        // If "English" is sent to the program, English is used instead.
        if ((args.length > 0) && (args[0].compareTo("English") == 0))
        {
            z = new ZmanimGui("English");
        }
        else
             z = new ZmanimGui("Hebrew");
        z.setVisible(true);
    }
}