
void loadTubeMap()
{
  pushStyle();
   fill(150);
   rect(width/2, height/2, width, height-200);
   pushMatrix();
    translate(width/2, height/2);
    rotate(Angle);
    image(img, 0, 0, Size, Size*0.692); 
   popMatrix();
   fill(180);
   rect(width/2, height-50, width, 100);
   textSize(24);
   textFont(fut);
   fill(0);
   text("DoubleTap to zoom in, tap here to zoom out", width/2, height-50);
  popStyle();
}
