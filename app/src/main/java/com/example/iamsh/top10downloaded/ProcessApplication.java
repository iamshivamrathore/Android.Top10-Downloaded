package com.example.iamsh.top10downloaded;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessApplication {
    private static final String TAG = "**************Shi";
    List<DataEntry> entries ;
    private String textvalue;

    public ProcessApplication(){
        entries = new ArrayList<DataEntry>();
    }

    List<DataEntry> getApplication(){
        return entries;
    }

    boolean parse(String xmlData){
        DataEntry record = null;
        boolean isEntry = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
        //    xpp.setInput(new StringReader(""));
            int eventType = xpp.getEventType();
            Log.e(TAG, "parse35: EventType : " +eventType );
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
       //         Log.e(TAG, "parse: Tag Name : " +tagName );
                switch (eventType){
                    case XmlPullParser.START_TAG :
                 //       Log.e(TAG, "parse: Start_Tag for : " +tagName);
                        if("entry".equalsIgnoreCase(tagName)){
              //              Log.e(TAG, "parse: Tagname equals entry, record creation");
                            isEntry = true;
                            record = new DataEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:

                        textvalue = xpp.getText();
          //              Log.e(TAG, "parse: .TEXT" +textvalue);
                        break;

                    case XmlPullParser.END_TAG:
             //           Log.e(TAG, "parse: END_TAG"+tagName );
                        if(isEntry){
                            if("entry".equalsIgnoreCase(tagName)){
              //                  Log.e(TAG, "parse: Entry equals tagname" );
                                entries.add(record);
                                isEntry = false;
                            }else if("im:name".equalsIgnoreCase(tagName)){
                                record.setName(textvalue);
                            }else if("im:artist".equalsIgnoreCase(tagName)){
                                record.setArtist(textvalue);
                            }else if("summary".equalsIgnoreCase(tagName)){
                                record.setSummary(textvalue);
                            }else if("releaseDate".equalsIgnoreCase(tagName)){
                                record.setReleaseDate(textvalue);
                            }else if("image".equalsIgnoreCase(tagName)){
                                record.setImageUrl(textvalue);
                            }
                        }
                        break;

                    default :

                }
                eventType = xpp.next();
            }

            for(DataEntry d : entries){
         //       Log.e(TAG, "******************" + d );
            }
            return true;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

}
