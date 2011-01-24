// Menu/trail.js
//
// Reference: "JavaScript and DHTML Cookbook" (O'Reilly, 2003)
//   Recipe 10.9, pages 292-296.
//
// Usage: 
// <SCRIPT>
// document.write (makeTrailMenu());
// </SCRIPT>


var trailMenu = new Object();
trailMenu["databases"] = {dir:"databases", lab:"Databases"};
trailMenu["wconnect"]  = {dir:"databases", lab:"Databases"};
trailMenu["infofiles"] = {dir:"InfoFiles", lab:"InfoFiles"};
trailMenu["jos"]       = {dir:"JOS",       lab:"Tools"};
trailMenu["jgff"]      = {dir:"JGFF",      lab:"JewishGen Family Finder"};
trailMenu["gedcom"]    = {dir:"gedcom",    lab:"Family Tree of the Jewish People"};
trailMenu["cure"]      = {dir:"CURE",      lab:"Login"};

trailMenu["austriaczech"] = {dir:"AustriaCzech", lab:"Austria-Czech"};
trailMenu["belarus"]      = {dir:"Belarus",   lab:"Belarus"};
trailMenu["cemetery"]     = {dir:"Cemetery",  lab:"JOWBR"};
trailMenu["germany"]      = {dir:"Germany",   lab:"Germany"};
trailMenu["holocaust"]    = {dir:"Holocaust", lab:"Holocaust"};
trailMenu["hungary"]      = {dir:"Hungary",   lab:"Hungary"};
trailMenu["israel"]       = {dir:"Israel",    lab:"Israel"};
trailMenu["latvia"]       = {dir:"Latvia",    lab:"Latvia"};
trailMenu["lithuania"]    = {dir:"Lithuania", lab:"Lithuania"};
trailMenu["poland"]       = {dir:"Poland",    lab:"Poland"};
trailMenu["romania"]      = {dir:"Romania",   lab:"Romania"};
trailMenu["safrica"]      = {dir:"SAfrica",   lab:"South Africa"};
trailMenu["scandinavia"]  = {dir:"Scandinavia", lab:"Scandinavia"};
trailMenu["uk"]           = {dir:"UK",        lab:"United Kingdom"};
trailMenu["ukraine"]      = {dir:"Ukraine",   lab:"Ukraine"};
trailMenu["usa"]          = {dir:"USA",       lab:"USA"};
trailMenu["vsia"]         = {dir:"vsia",      lab:"<I>Vsia Rossiia</I>"};

trailMenu["faq"]          = {dir:"FAQ",       lab:"Frequently Asked Questions"};
trailMenu["test"]         = {dir:"Test",      lab:"Test Directory"};
trailMenu["admin"]        = {dir:"Admin",     lab:"Administration"};
trailMenu["communities"]       = {dir:"Communities", lab:"JewishGen Communities Database"};
trailMenu["jewishgen-erosity"] = {dir:"JewishGen-erosity", lab:"JewishGen-erosity"};
trailMenu["listmanager"]       = {dir:"ListManager",       lab:"Mailing Lists"};
trailMenu["viewmate"]          = {dir:"ViewMate",   lab:"ViewMate"};
trailMenu["webmasters"]        = {dir:"Webmasters", lab:"Webmasters Info"};
trailMenu["jg"]                = {dir:"JewishGen",  lab:"JewishGen-eral Information"};
trailMenu["jewishgen"]         = {dir:"JewishGen",  lab:"JewishGen-eral Information"};
trailMenu["pr"]                = {dir:"PR", lab:"Press Releases"};
trailMenu["projects"]          = {dir:"projects/desc", lab:"JewishGen Internal: Projects"};
trailMenu["yizkor"]            = {dir:"Yizkor",  lab:"Yizkor Book Project"};


function makeTrailMenu()
{
  var output = "<span style='font-family:Arial; font-size:10px; color:#949C51;'>";
  var linkStyle = " style='color:#949C51'";
  var separator = "&nbsp;&raquo;&nbsp;"
  var re = /\\/g;
  var path = top.location.pathname;
  path = path.replace(re, "/");
  path = path.toLowerCase();
  var leaves = path.split("/");
  
  document.write ("<!-- " + path + "-->");
  
  // Special case for home page: Null output. 
  if ( path == "/" )
    { return ""; }


  var parseEnd;
  var leaf = leaves[leaves.length-1];
  if ( (leaf == "") || (leaf.indexOf("index.") == 0) )
    { parseEnd = leaves.length - 1; }
  else
    { parseEnd = leaves.length; }
  
  // var trail = location.protocol + "//" + location.hostname;
  var trail = "http://www.jewishgen.org"
 
  for (var i = 0; i < parseEnd; i++) 
    {
      if (i == 0) 
        {
          trail += "/" + leaves[i];
          output += "<a href='" + trail + "'" + linkStyle + ">";
          output += "Home" + "</a>";
        } 
      else if (i == parseEnd - 1) 
        {
          output += separator + "<NOBR>" + document.title + "</NOBR>";
        } 
      else  // middle case
        {
          if ( trailMenu[leaves[i]] == undefined )  
            { 
              trail += leaves[i] + "/";
              continue; 
            }
          trail += trailMenu[leaves[i]].dir + "/";
          output += separator + "<a href='" + trail + "'" + linkStyle + ">";
          output += trailMenu[leaves[i]].lab + "</a>";
        }
    }

  output += "</span>";
  return output;
}
