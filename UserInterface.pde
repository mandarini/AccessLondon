void drawUI()
{
  //HOME button
  pushStyle();
   rectMode(CORNER);
   textAlign(CENTER);
   fill(0);
   stroke(0);
   rect(0, 0, width, 100);
   fill(255);
   textFont(fut70); 
   text("HOME", 5*width/6-5, 75); 
  popStyle();
}
