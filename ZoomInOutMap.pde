//For the map
void onDoubleTap(float x, float y)
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

void onTap(float x, float y)
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

