// CreateLoginLink.js
//
// /JG/Scripts/CreateLoginLink.js
// Function called by /JG/header.txt
//
// Warren Blatt, May 2010


function CreateLoginLink()
{
  var jgcurecookie = getcookie ("jgcure");
  var jgid = getsubcookie(jgcurecookie, "jgid");
  var retstr = "";

  if (jgid == "")
    { 
      var mypath = window.location.pathname.toLowerCase()
      // Special case for the Login page:
      //   Don't display an additional login link.
      if (mypath == "/cure/index.asp" || mypath == "/cure/")
        {
          retstr = "";
        }
      else
        {
          // Set the login return page to be the current page.
          // But special case for the Logout page:
          //   Set login return page to the Home page, to allow re-login. 
          if (mypath == "/cure/logout.asp")
            { setcookie ("login", "http://www.jewishgen.org", "", "/", ".jewishgen.org"); }
          else
            { setcookie ("login", window.location.href, "", "/", ".jewishgen.org"); }
          
          retstr = "<a href id='loginlink' rel='loginbox' rev='/JG/LoginDropdown.asp'>Login to JewishGen</A>";
        }
    }
  else
    {
      var contact = getsubcookie(jgcurecookie, "contact");
      contact = contact.replace(/\+/g, " ");
      
      retstr = "Logged in: " + contact + "<BR>";
      retstr += "<SPAN CLASS='smalltext'>";
      retstr += "<A HREF='http://www.jewishgen.org/CURE/MyProfile.asp'>My Profile</A> | ";
      retstr += "<A HREF='http://www.jewishgen.org/CURE/logout.asp'>Logout</A></SPAN>";
    }

  return (retstr);
}
