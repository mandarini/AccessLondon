package processing.test.trialtwo;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.ui.*; 
import ketai.sensors.*; 
import android.view.MotionEvent; 

import twitter4j.util.*; 
import twitter4j.*; 
import twitter4j.management.*; 
import twitter4j.api.*; 
import twitter4j.conf.*; 
import twitter4j.json.*; 
import twitter4j.auth.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TrialTwo extends PApplet {


 


ConfigurationBuilder cv = new ConfigurationBuilder();
Twitter twitterInstance;
Query queryForTwitter;

PFont or10, or12, or24, or48;

double longitude, latitude, accuracy, stax, stay; //stationx, stationy
KetaiLocation location;
Location st;

KetaiGesture gesture;
float Size = 2*width/3;;
float Angle = 0;
PImage img;

KetaiList selectionlist;
KetaiVibrate vibe;
ArrayList<String> modes = new ArrayList<String>();
HashMap data = new HashMap();
ArrayList<Float> gencount = new ArrayList<Float>();
ArrayList<Float> disab = new ArrayList<Float>();
ArrayList<Float> disabper = new ArrayList<Float>();
ArrayList tweets;

String [] rowsst;


String nam, sta, user, msg; //for twitter
float m, n, p, he;
int g;
int ch=0;
int x=0;

int backgroundcolor = color(180);

public void setup()
{
  //for the image
  gesture = new KetaiGesture(this);
  st = new Location("station");
  img = loadImage("TubeMap.png");
  rowsst = loadStrings("StationsAccLL.csv");
  orientation(PORTRAIT);
  textAlign(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  vibe = new KetaiVibrate(this);
  loadData(); 
  onResume();
  or10=loadFont("OratorStd-10.vlw");
  or12=loadFont("OratorStd-12.vlw");
  or24=loadFont("OratorStd-24.vlw");
  or48=loadFont("OratorStd-48.vlw");
  reset();
  startTwitter();
}

public void draw()
{
  if ((Size*0.692f)<height-200)
  {
    drawUI();
  }
  if (ch==4) showTweets();
}

//BUTTONS
public void mousePressed()
{
  if (mouseY < 100)
  {
    if (mouseX < width/3) KetaiKeyboard.toggle(this);
    else if (mouseX > width/3 && mouseX < width-(width/3))
      KetaiAlertDialog.popup(this, "Pop Up!", "this is a popup message box");
    else reset();
     // vibe.vibrate(1000);    
  }
  else if (((mouseY<height/2-10)&&(mouseY>height/2-110))&&ch==0) 
  {
    selectionlist = new KetaiList(this, modes);
    ch=1;
  }
  else if (((mouseY<height/2+110)&&(mouseY>height/2+10))&&ch==0) 
  {
    loadTubeMap();
    ch=2;
  }
  else if (((mouseY<height/2+230)&&(mouseY>height/2+130))&&ch==0) 
  {
    nearestStations();
    ch=3;
  }
  else if (((mouseY<height/2+350)&&(mouseY>height/2+250))&&ch==0) 
  {
    //twitter
    ch=4;
  }
}


//here the user chooses mode of transport to see users
public String onKetaiListSelection(KetaiList klist)
{
  String selection = klist.getSelection();
  fill(150);
  rect(width/2, height/2, width-100, height-200);
  fill(0);
  g=modes.indexOf(selection);
  m=gencount.get(g);
  n=disab.get(g);
  p=disabper.get(g);
  textFont(or24);
  text(selection+" carries "+m+"% of TfL users", width/2, height/2-150);
  text(n+"% of Disabled Pass holders use ", width/2, height/2-50);
  text(selection, width/2, height/2);
  text(p+"% of "+selection+" users", width/2, height/2+100);
  text("are Disabled Pass holders", width/2, height/2+150);
  return selection;
}


// loads in the station data
public void loadData()
{
 String [] rows = loadStrings("DisabledAndModes.csv"); // load CSV file
 for (int i = 1; i<rows.length; i++) // Iterate through rows. Don't start from 0 so that you don't load in headers
 {
  // Split rows using the comma as delimiter - and save as string array
  String [] thisRow = split(rows[i], ";");
  modes.add(thisRow[0]);
  gencount.add(PApplet.parseFloat(thisRow[5]));
  disab.add(PApplet.parseFloat(thisRow[4]));
  disabper.add(PApplet.parseFloat(thisRow[3]));
 }
}


public void mouseDragged()
{
 // he=mouseY;
}

public boolean surfaceTouchEvent(MotionEvent event) {

  //call to keep mouseX, mouseY, etc updated
  super.surfaceTouchEvent(event);

  //forward event to class for processing
  return gesture.surfaceTouchEvent(event);
}

public void drawButtons()
{
  noStroke();
  pushStyle();
   textFont(or48);
   //FirstButton 
   fill(149, 96, 117);
   rect(width/2, height/2-60, 2*width/3, 100);
   fill(0);
   text("Choose mode of transport", width/2, height/2-60);
   text("to see data", width/2, height/2-20);
   //secondButton
   fill(149, 96, 117);
   rect(width/2, height/2+60, 2*width/3, 100);
   fill(0);
   text("See tube map", width/2, height/2+80);
   //third button
   fill(149, 96, 117);
   rect(width/2, height/2+180, 2*width/3, 100);
   fill(0);
   text("Nearest stations", width/2, height/2+200);
   fill(149, 96, 117);
   rect(width/2, height/2+300, 2*width/3, 100);
   fill(0);
   text("Tweeter", width/2, height/2+320);
  popStyle();
}
public void onResume()
{
  location = new KetaiLocation(this);
  super.onResume();
}

public void onLocationEvent(Location _location)
{
  //print out the location object
  println("onLocation event: " + _location.toString());
  longitude = _location.getLongitude();
  latitude = _location.getLatitude();
  accuracy = _location.getAccuracy();
}
 public void nearestStations()
 {
  pushStyle();
   textFont(or48);
   fill(150);
   rect(width/2, height/2, width, height-200);
  if (location.getProvider() == "none")
   {
     fill(0);
     text("Location data is unavailable. \n" + "Please check your location settings.", 0, 0, width, height);
   }
  else
   {
      fill(0);
      int j=0;
      text("Nearest accessible station(s):", width/2, height/4-20);
      for (int i = 1; i<rowsst.length; i++)
      {
        String [] thisRow = split(rowsst[i], ";");
         stax=PApplet.parseFloat(thisRow[2]);
         stay=PApplet.parseFloat(thisRow[3]);
         //  println(stax);
         // println(stay);
         st.setLatitude(stax);
         st.setLongitude(stay);
         if ((location.getLocation().distanceTo(st))<1500)
         {
           j=j+100;
           text(thisRow[1], width/2, height/4+j);
         }  
      }
   } 
  popStyle();
}
public void reset()
{
  background(backgroundcolor);
  drawUI();
  drawButtons(); 
  Size = 2*width/3;
  Angle = 0;
  ch=0;
}


public void loadTubeMap()
{
  pushStyle();
   fill(150);
   rect(width/2, height/2, width, height-200);
   pushMatrix();
    translate(width/2, height/2);
    rotate(Angle);
    image(img, 0, 0, Size, Size*0.692f); 
   popMatrix();
   fill(180);
   rect(width/2, height-50, width, 100);
   textFont(or24);
   fill(0);
   text("DoubleTap to zoom in, tap here to zoom out", width/2, height-50);
  popStyle();
}

public void startTwitter()
{
  cv.setOAuthConsumerKey("vzw9pzLZeI2jOhiKCf10MmiA6");
  cv.setOAuthConsumerSecret("cr3saUry7JHBCZsnfnNQcSAygU5HXNujRS6eEEpA2EbBg6zKz0");
  cv.setOAuthAccessToken("2233287476-CAQpXur39RDc8orQTR0RuLaE4KgHiqslmqZOtKx");
  cv.setOAuthAccessTokenSecret("MbkziFuIsrjkMdBhyBL7o6jjbPA1BrvLBeifXiCqad8iR");

  twitterInstance = new TwitterFactory(cv.build()).getInstance();
  queryForTwitter = new Query("@TfLAccess"); 
}

public void FetchTweets()
{
  try 
  {
    QueryResult result = twitterInstance.search(queryForTwitter);
    User tfla=twitterInstance.showUser(1108726394);
   // println("@" + tfla.getScreenName() + " - " + tfla.getStatus().getText());
    nam=tfla.getScreenName();
    sta=tfla.getStatus().getText();
    tweets = (ArrayList) result.getTweets();  
  }
  catch(TwitterException te)
  {
    println("Couldn't connect: "+te);
  }
}

public void showTweets()
{
   pushStyle();
    textAlign(LEFT);  
    fill(150);
    rect(width/2, height/2, width, height-200);
    FetchTweets();
    fill(0);
    rect(width/2, height-50, width, 100);
    rect(width/3, 50, 2*width/3, 100);
    textFont(or48);
    text("@" + nam + " - " + sta+"\n", width/2, 180, width-80, 150); 
    for (int i=0; i<tweets.size(); i++) 
    {
      Status t= (Status) tweets.get(i);
      User theUser=t.getUser();
      user=theUser.getName();
      msg = t.getText(); 
      text(user +": "+msg+"\n", width/2, 360+i*180-mouseY, width-80, 150);
      println(user +": "+msg);
    }
   popStyle();
}



public void drawUI()
{
  pushStyle();
  rectMode(CORNER);
  textAlign(CENTER);
  fill(0);
  stroke(180);
  rect(2*width/3, 0, width/3, 100);
  fill(255);
  textFont(or48); 
  text("Home", 4*width/5, 50); 
  popStyle();
}
//For the map
public void onDoubleTap(float x, float y)
{
  if (ch==2)
  {
   if (Size<4000) 
   {
    Size=Size+200;
    loadTubeMap();
   }
  }
}

public void onTap(float x, float y)
{
  if (ch==2)
  {
   if (mouseY>height-100) 
   {
    Size=Size-200;
    loadTubeMap();
   }
  }
}
//For the map


}
