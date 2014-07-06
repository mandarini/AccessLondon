
void startTwitter()
{
  cv.setOAuthConsumerKey("vzw9pzLZeI2jOhiKCf10MmiA6");
  cv.setOAuthConsumerSecret("cr3saUry7JHBCZsnfnNQcSAygU5HXNujRS6eEEpA2EbBg6zKz0");
  cv.setOAuthAccessToken("2233287476-CAQpXur39RDc8orQTR0RuLaE4KgHiqslmqZOtKx");
  cv.setOAuthAccessTokenSecret("MbkziFuIsrjkMdBhyBL7o6jjbPA1BrvLBeifXiCqad8iR");

  twitterInstance = new TwitterFactory(cv.build()).getInstance();
  queryForTwitter = new Query("@TfLAccess"); 
  queryTwitter2 = new Query("@FreedomPassLDN");
}

void FetchTweets()
{
  try 
  {
    QueryResult result = twitterInstance.search(queryForTwitter);
    QueryResult result2 = twitterInstance.search(queryTwitter2);
    User tfla=twitterInstance.showUser(1108726394);
    User freeldn=twitterInstance.showUser(551858001);
   // println("@" + tfla.getScreenName() + " - " + tfla.getStatus().getText());
    nam=tfla.getScreenName();
    sta=tfla.getStatus().getText();
    ttim=tfla.getStatus().getCreatedAt();
    tweets = (ArrayList) result.getTweets();  
    nam2=freeldn.getScreenName();
    sta2=freeldn.getStatus().getText();
    ttim2=freeldn.getStatus().getCreatedAt();
    ttime=tti.format(ttim);
    ttime2=tti2.format(ttim2);
    println(ttime); 
   // tweets = (ArrayList) result2.getTweets(); 
  }
  catch(TwitterException te)
  {
    println("Couldn't connect: "+te);
  }
}

void showTweets()
{
   pushStyle();
   // textAlign(LEFT, TOP);  
    fill(150);
    rect(width/2, height/2, width, height-200);
    FetchTweets();
    fill(0);
    rect(width/2, height-50, width, 100);
    rect(width/3, 50, 2*width/3, 100);
    textFont(fut50b);
    text("@" + nam + " " +ttime, width/2, 240, width-80, 250); 
    textFont(fut40);
    text("\n" + sta, width/2, 240, width-80, 250);
    textFont(fut50b);
    text("@" + nam2+ " "+ttime2, width/2, 470, width-20, 250);
    textFont(fut40);
    text("\n" + sta2, width/2, 470, width-80, 250);
    for (int i=0; i<tweets.size(); i++) 
    {
      Status t= (Status) tweets.get(i);
      User theUser=t.getUser();
      user=theUser.getName();
      ttim3=t.getCreatedAt();
      ttime3=tti3.format(ttim3);
      msg = t.getText(); 
      text(user+" "+ttime3 +" : "+msg, width/2, 720+i*220, width-80, 200);
    //  println(user +": "+msg);
    }
   popStyle();
}



