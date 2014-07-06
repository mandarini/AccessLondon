import ketai.ui.*;
import ketai.sensors.*; 
import android.view.MotionEvent;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

ConfigurationBuilder cv = new ConfigurationBuilder();
Twitter twitterInstance;
Query queryForTwitter, queryTwitter2;

PFont fut, futme, futbo, fut70, fut40, fut40b, fut50b;

double longitude, latitude, accuracy, stax, stay, sx, sy; //stationx, stationy
KetaiLocation location;
Location st;

DateFormat tti = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
DateFormat tti2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
DateFormat tti3 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
Date ttim, ttim2, ttim3;
String ttime, ttime2, ttime3;

KetaiGesture gesture;
float Size = 2*width/3;;
float Angle = 0;
PImage img, b1, b2, b3, b4, b5, b6, b7, lo;

KetaiList selectionlist, tubes, busstop;
KetaiVibrate vibe;
HashMap data = new HashMap();
ArrayList<String> modes = new ArrayList<String>();
ArrayList<String> tubesta = new ArrayList<String>();
ArrayList<String> busesstop = new ArrayList<String>();
ArrayList<Double> tubex = new ArrayList<Double>();
ArrayList<Double> tubey = new ArrayList<Double>();
ArrayList<Double> busx = new ArrayList<Double>();
ArrayList<Double> busy = new ArrayList<Double>();
ArrayList<Float> gencount = new ArrayList<Float>();
ArrayList<Float> disab = new ArrayList<Float>();
ArrayList<Float> disabper = new ArrayList<Float>();
ArrayList tweets;

String [] rowsst, buses;


String nam, sta, user, msg, nam2, sta2; //for twitter
float m, n, p, he;
int g, j;
int ch=0;
int x=0;

color backgroundcolor = color(180);

void setup()
{
  //for the image
  gesture = new KetaiGesture(this);
  st = new Location("station");
  img = loadImage("TubeMap.png");
  b1 = loadImage("1VisualImp.png");
  b2 = loadImage("2WheelChair.png");
  b3 = loadImage("3Twitter.png");
  b4 = loadImage("4Bus.png");
  b5 = loadImage("Metro.png");
  b6 = loadImage("Tube.png");
  b7 = loadImage("barchart.png");
  rowsst = loadStrings("StationsAccLL.csv");
  buses= loadStrings("BusStops.csv");
  orientation(PORTRAIT);
  textAlign(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  vibe = new KetaiVibrate(this);
  loadData(); 
  onResume();
  fut=loadFont("FuturaBT-Book.vlw");
  futme=loadFont("FuturaBT-Medium.vlw");
  futbo=loadFont("FuturaBT-Bold.vlw");
  fut70=loadFont("Futura70.vlw");
  fut40=loadFont("FuturaBT-Book-40.vlw");
  fut40b=loadFont("FuturaBT-Bold-40.vlw");
  fut50b=loadFont("FuturaBT-Bold-50.vlw");
  reset();
  startTwitter();
}

void draw()
{
  if ((Size*0.692)<height-200)
  {
    drawUI();
  }
  if (ch==4) showTweets();
}

//BUTTONS
void mousePressed()
{
  if (mouseY < 100)
  { 
    reset();
    vibe.vibrate(200);    
  }
  else if (mouseX<width/2)
  {
   if (((mouseY<height/4+200)&&(mouseY>height/4-200))&&ch==0) 
   {
     nearestStations();
     tubes = new KetaiList (this, tubesta);
     ch=3;
   }
   else if (((mouseY<height/2+200)&&(mouseY>height/2-200))&&ch==0) 
   {
     //twitter
     ch=4;
   }
   else if (((mouseY<3*height/4+200)&&(mouseY>3*height/4-200))&&ch==0) 
   {
     selectionlist = new KetaiList(this, modes);
     ch=1;
   }
  }
  else if (mouseX>width/2)
  {
   if (((mouseY<height/4+200)&&(mouseY>height/4-200))&&ch==0)
   {   
     ch=5;
     nearestStops();
     busstop = new KetaiList (this, busesstop);
   }
   else if (((mouseY<height/2+200)&&(mouseY>height/2-200))&&ch==0) 
   {
     loadTubeMap();
     ch=2;
   }
    else if (((mouseY<3*height/4+200)&&(mouseY>3*height/4-200))&&ch==0)  
   {
     j=j+1;
     ch=6;
   }
  }
}


//here the user chooses mode of transport to see users
String onKetaiListSelection(KetaiList klist)
{
  String selection = klist.getSelection();
  fill(150);
  rect(width/2, height/2, width-100, height-200);
  fill(0);
  if (ch==1)
  {  
   g=modes.indexOf(selection);
   m=gencount.get(g);
   n=disab.get(g);
   p=disabper.get(g);
   textFont(fut50b);
   text(selection, width/2, height/2-200);
   textFont(fut40);
   text("carries "+m+"% of TfL users", width/2, height/2-150);
   text(n+"% of Disabled Pass holders use ", width/2, height/2-50);
   text(selection, width/2, height/2);
   text(p+"% of "+selection+" users", width/2, height/2+100);
   text("are Disabled Pass holders", width/2, height/2+150);
  }
  else if (ch==3)
  {
    g=tubesta.indexOf(selection);
    sx=tubex.get(g);
    sy=tubey.get(g);
    link("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+sx+","+sy);
  }
  else if (ch==5)
  {
    g=busesstop.indexOf(selection);
    println(g+selection);
    sx=busx.get(g);
    sy=busy.get(g);
    link("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+sx+","+sy);
  }
   return selection;
}


// loads in the station data
void loadData()
{
 String [] rows = loadStrings("DisabledAndModes.csv"); // load CSV file
 for (int i = 1; i<rows.length; i++) // Iterate through rows. Don't start from 0 so that you don't load in headers
 {
  // Split rows using the comma as delimiter - and save as string array
  String [] thisRow = split(rows[i], ";");
  modes.add(thisRow[0]);
  gencount.add(float(thisRow[5]));
  disab.add(float(thisRow[4]));
  disabper.add(float(thisRow[3]));
 }
}


void mouseDragged()
{
 // he=mouseY;
}

public boolean surfaceTouchEvent(MotionEvent event) {

  //call to keep mouseX, mouseY, etc updated
  super.surfaceTouchEvent(event);

  //forward event to class for processing
  return gesture.surfaceTouchEvent(event);
}

