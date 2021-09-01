package academy.learnprogramming.kotlintop10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

private const val TAG = "ParseApplications"

class ParseApplications {
    val applications=ArrayList<FeedEntry>()
    fun parse(xmlData:String):Boolean{
//        Log.d(TAG, "parse called with $xmlData")
        var status=true
        var inEntry=false
        var gotImage=false
        var textValue=""

        try {
            val factory=XmlPullParserFactory.newInstance()
            factory.isNamespaceAware=true
            val xpp=factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType=xpp.eventType
            var currentRecord=FeedEntry()
            while (eventType!=XmlPullParser.END_DOCUMENT){
                val tagName=xpp.name?.toLowerCase()
                when(eventType){

                    XmlPullParser.START_TAG-> {
//                        Log.d(LOG_TAG, "parse: Ending tag for $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT->textValue=xpp.text
                    XmlPullParser.END_TAG->{
                        if (inEntry){
                            when(tagName){
                                "entry"->{
                                    applications.add(currentRecord)
                                    inEntry=false
                                    currentRecord= FeedEntry()
                                }
                                "name"->currentRecord.name=textValue
                                "artist"->currentRecord.artist=textValue
                                "releasedate"->currentRecord.releaseDate=textValue
                                "summary"->currentRecord.summary=textValue
                                "image"->{
                                    if (gotImage){
                                        currentRecord.imageURL=textValue
                                    }
                                }
                            }
                        }
                    }
                }
                eventType=xpp.next()
//                for (app in applications){
//                    Log.d(LOG_TAG,app.toString())
//                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            status=false
        }
        return status
    }
}
