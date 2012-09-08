package com.kos.reader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class CommentsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		WebView tv = (WebView) findViewById(R.id.textView1);
		String stringExtra = 
				"<style type=\"text/css\">\n" + 
				"div.crs\n" + 
				"{ display: none; }\n"+ 
				"</style> \n"
				//+js
				;
		stringExtra += getIntent().getStringExtra("comments");
		Log.d("com", stringExtra);
		tv.loadDataWithBaseURL("http://www.dailykos.com", stringExtra,
				"text/html", "UTF-8", "about:blank");
		
	}
	final static String js = "<style type=\\\"text/javascript\\\"    >// ==UserScript==\n" + 
			"// @name           Daily Kos Comment Highlighter / Filters\n" + 
			"// @namespace      gmonkeyfilter\n" + 
			"// @description    Highlights/Filters comments on Daily Kos stories\n" + 
			"// @include        http://www.dailykos.com/story/*\n" + 
			"// @require	  http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.js\n" + 
			"// ==/UserScript==\n" + 
			"\n" + 
			"var defaultShowTop25=false;\n" + 
			"var defaultShowTop10=false;\n" + 
			"var defaultShowTop5=false;\n" + 
			"\n" + 
			"var $=window.jQuery;\n" + 
			"window.jQuery.noConflict();\n" + 
			"$(document).ready(function(){\n" + 
			"\n" + 
			"        var arRecs=new Array();\n" + 
			"				$(\".cx span a\").each(function(){\n" + 
			"        var recs=$(this).text().split('+')[0];\n" + 
			"        arRecs.push(parseInt(recs));\n" + 
			"				});\n" + 
			"        arRecs.sort(function (a, b) {return a - b;});\n" + 
			"        var top25=arRecs[Math.floor(arRecs.length*.75)]\n" + 
			"        var top10=arRecs[Math.floor(arRecs.length*.9)]\n" + 
			"        var top5=arRecs[Math.floor(arRecs.length*.95)]\n" + 
			"        var top2=arRecs[Math.floor(arRecs.length*.98)]\n" + 
			"        \n" + 
			"        $(\".cx span a\").each(function(){\n" + 
			"        var recs=$(this).text().split('+')[0];\n" + 
			"        var numrecs=parseInt(recs);\n" + 
			"          var newbgcolor='';\n" + 
			"          if(numrecs>=top25)newbgcolor='#FFFFcc';\n" + 
			"          if(numrecs>=top10)newbgcolor='#FFFF99';\n" + 
			"          if(numrecs>=top5)newbgcolor='#FFFF66';\n" + 
			"          if(numrecs>=top2)newbgcolor='#FFDD66';\n" + 
			"          if(newbgcolor!=''){\n" + 
			"            $(this).parents(\"div.cx\").css({backgroundColor: newbgcolor});\n" + 
			"            }\n" + 
			"        });\n" + 
			"\n" + 
			"        //add filter links to top of comments section\n" + 
			"        function hideBelow(threshold,showprompt){\n" + 
			"           \n" + 
			"						$(\".cx span a\").each(function(){\n" + 
			"        			var recs=$(this).text().split('+')[0];\n" + 
			"        			var numrecs=parseInt(recs);\n" + 
			"        			if(numrecs<threshold){\n" + 
			"								$(this).parents(\"div.cx\").hide();\n" + 
			"							}\n" + 
			"						})\n" + 
			"						$(\"#filterstatusdiv\").html(\"*Filtered*\");\n" + 
			"				}\n" + 
			"        var filterlink=document.createElement(\"a\");\n" + 
			"        filterlink.href=\"javascript:void(0)\";\n" + 
			"        $(\"#cDForm\").append(filterlink);\n" + 
			"        $(filterlink).click(function(){hideBelow(top25,true)});\n" + 
			"        $(filterlink).html(\"Top 25% \");\n" + 
			"        \n" + 
			"        filterlink=document.createElement(\"a\");\n" + 
			"        filterlink.href=\"javascript:void(0)\";\n" + 
			"        $(\"#cDForm\").append(filterlink);\n" + 
			"        $(filterlink).click(function(){hideBelow(top10,true)});\n" + 
			"        $(filterlink).html(\"Top 10% \");\n" + 
			"        \n" + 
			"        filterlink=document.createElement(\"a\");\n" + 
			"        filterlink.href=\"javascript:void(0)\";\n" + 
			"        $(\"#cDForm\").append(filterlink);\n" + 
			"        $(filterlink).click(function(){hideBelow(top5,true)});\n" + 
			"        $(filterlink).html(\"Top 5% \");\n" + 
			"\n" + 
			"        filterlink=document.createElement(\"a\");\n" + 
			"        filterlink.href=\"javascript:void(0)\";\n" + 
			"        $(\"#cDForm\").append(filterlink);\n" + 
			"        $(filterlink).click(function(){$(\".cx span a\").each(function(){\n" + 
			"	         $(this).parents(\"div.cx\").show();\n" + 
			"				})\n" + 
			"				$(\"#filterstatusdiv\").html(\"\");\n" + 
			"				});\n" + 
			"        $(filterlink).html(\"Show All \");\n" + 
			"        \n" + 
			"        filterstatusdiv=document.createElement(\"div\");\n" + 
			"        filterstatusdiv.id=\"filterstatusdiv\";\n" + 
			"        $(\"#cDForm\").append(filterstatusdiv);\n" + 
			"				\n" + 
			"				if(defaultShowTop5)hideBelow(top5);\n" + 
			"				if(defaultShowTop10)hideBelow(top10);\n" + 
			"				if(defaultShowTop25)hideBelow(top25);\n" + 
			"\n" + 
			"}); </script>";
}
