 void nearestStations()
 {
  pushStyle();
   textSize(48);
   textFont(fut);
   fill(150);
   rect(width/2, height/2, width, height-200);
  if (location.getProvider() == "none")
   {
     fill(0);
     text("Location data is unavailable. \n" + "Please check your location settings.", 0, 0, width, height);
   }
  else
   {
      for (int i = 1; i<rowsst.length; i++)
      {
        String [] thisRow = split(rowsst[i], ";");
         stax=float(thisRow[2]);
         stay=float(thisRow[3]);
         st.setLatitude(stax);
         st.setLongitude(stay);
         if ((location.getLocation().distanceTo(st))<1500)
         {
          tubesta.add(thisRow[1]);
          tubex.add(stax);
          tubey.add(stay);
         }  
      }
   } 
  popStyle();
}


void nearestStops()
{
  pushStyle();
   textSize(48);
   textFont(fut);
   fill(150);
   rect(width/2, height/2, width, height-200);
  if (location.getProvider() == "none")
   {
     fill(0);
     text("Location data is unavailable. \n" + "Please check your location settings.", 0, 0, width, height);
   }
  else
   {
      for (int i = 1; i<buses.length; i++)
      {
        String [] thisRow = split(buses[i], ";");
         stax=float(thisRow[3]);
         stay=float(thisRow[4]);
         st.setLatitude(stax);
         st.setLongitude(stay);
         if ((location.getLocation().distanceTo(st))<500)
         {
          busesstop.add(thisRow[0]);
          busx.add(stax);
          busy.add(stay);
         }  
      }
   } 
  popStyle();
}
