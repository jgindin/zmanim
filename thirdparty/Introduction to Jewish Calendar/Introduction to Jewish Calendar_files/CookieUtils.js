// CookieUtils.js

// Utility function called by getcookie():
function getCookieVal(offset)
{
  var endstr = document.cookie.indexOf (";", offset);
  if (endstr == -1)
    { endstr = document.cookie.length; }
  return decodeURI(document.cookie.substring(offset, endstr));
}

// Function to retrieve cookie by name:
function getcookie(name)
{
  var arg = name + "=";
  var alen = arg.length;
  var clen = document.cookie.length;

  var i = 0;
  while (i < clen)
  {
    var j = i + alen;
    if (document.cookie.substring(i,j) == arg)
    {
      return getCookieVal(j);
    }
    i = document.cookie.indexOf(" ",i) + 1;
    if (i==0) break;
  }
  return "";
}

function getsubcookie(xcookie, name)
{
  var search = name + "="
  var returnvalue = "";

  // debug
  // alert ("Cookie is: '" + xcookie + "'");

  if (xcookie.length > 0)
    {
      offset = xcookie.indexOf(search)
      // if subcookie exists
      if (offset != -1)
        {
          offset += search.length
          // Set index of beginning of value.
          end = xcookie.indexOf("&", offset);
          // Set index of end of value
          if (end == -1) end = xcookie.length;

          returnvalue = decodeURIComponent(xcookie.substring(offset, end));
        }
    }
  return returnvalue;
}

// Store cookie value, with optional details as needed.
function setcookie(name, value, expires, path, domain, secure)
{
  document.cookie = name + "=" + encodeURIComponent(value) +
	((expires) ? "; expires=" + expires : "") +
	((path) ? "; path=" + path : "") +
	((domain) ? "; domain=" + domain : "") +
	((secure) ? "; secure=" + secure : "");
}