package processing.test.trialone;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.ui.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TrialOne extends PApplet {



PFont or10, or12, or24, or48;

KetaiList selectionlist;
KetaiVibrate vibe;
ArrayList<String> modes = new ArrayList<String>();
HashMap data = new HashMap();
//StringList modes = new StringList();
//FloatList gencount = new FloatList
ArrayList<Float> gencount = new ArrayList<Float>();
ArrayList<Float> disab = new ArrayList<Float>();
ArrayList<Float> disabper = new ArrayList<Float>();

//String ma;
float m, n, p;
//char lala;
int g;

int backgroundcolor = color(180);

public void setup()
{
  orientation(PORTRAIT);
  textAlign(CENTER);
  rectMode(CENTER);
  vibe = new KetaiVibrate(this);
  loadData(); 
  or10=loadFont("OratorStd-10.vlw");
  or12=loadFont("OratorStd-12.vlw");
  or24=loadFont("OratorStd-24.vlw");
  or48=loadFont("OratorStd-48.vlw");
  reset();
}

public void reset()
{
  background(backgroundcolor);
  drawUI();
  fill(0);
  textFont(or48);
  text("Choose mode of transport", width/2, height/2-20);
  text("to see data", width/2, height/2+20);
}

public void draw()
{

}


public void mousePressed()
{
  if (mouseY < 100)
  {
    if (mouseX < width/3)
      KetaiKeyboard.toggle(this);
    else if (mouseX > width/3 && mouseX < width-(width/3))
      KetaiAlertDialog.popup(this, "Pop Up!", "this is a popup message box");
    else
     // vibe.vibrate(1000);
      reset();
  }
  else
    selectionlist = new KetaiList(this, modes);
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


public void drawUI()
{
  pushStyle();
  rectMode(CORNER);
  textAlign(CENTER);
  fill(0);
  stroke(180);
  //rect(0, 0, width/3, 100);
  //rect(width/3, 0, width/3, 100);
  rect(2*width/3, 0, width/3, 100);
  fill(255);
  //text("Keyboard", 5, 60); 
 // text("PopUp", width/3 + 5, 60);
  textFont(or48); 
  text("Home", 4*width/5, 50); 
  popStyle();
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


}
