void drawButtons()
{
  noStroke();
  pushStyle();
   textAlign(CENTER);
   rectMode(CENTER);
   imageMode(CENTER);
   fill(0);
   rect(width/4+16, height/4-32, 400, 400);
   image(b5, width/4+16, height/4-32); 
   rect(3*width/4-16, height/4-32, 400, 400);
   image(b4, 3*width/4-16, height/4-32);
   rect(width/4+16, height/2, 400, 400);   
   image(b3, width/4+16, height/2);
   rect(3*width/4-16, height/2, 400, 400);
   image(b6, 3*width/4-16, height/2);
   rect(width/4+16, 3*height/4+32, 400, 400);
   image(b7, width/4+16, 3*height/4+32);
   rect(3*width/4-16, 3*height/4+32, 400, 400);
   if (j%2==0) lo=b1; else lo=b2;
   image(lo, 3*width/4-16, 3*height/4+32);
  popStyle();
}
