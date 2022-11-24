import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;
import groovy.xml.MarkupBuilder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import groovy.json.JsonBuilder;
import groovy.xml.MarkupBuilder
    
    def Message processData(Message message) {

        def body = message.getBody(String);

        //remove \n and other control characters from payload
       // body = body.replaceAll("[^a-zA-Z0-9 ]+","")

        //Headers 
        def jsonSlurper = new JsonSlurper();
        def jsonData = jsonSlurper.parseText(body);
        def map = message.getHeaders();
        //Properties 
        map = message.getProperties();

    def builder = new JsonBuilder()
    def messageLog = messageLogFactory.getMessageLog(message);


    def outbound_message = ""
    def ITR = 0
    def xmlWriter = new StringWriter();
    def xmlMarkup = new MarkupBuilder(xmlWriter);
    xmlMarkup.setDoubleQuotes(true);
    xmlMarkup.mkp.xmlDeclaration(version: '1.0', encoding:'utf-8');
    
    /************************************************************
     Loop through product nodes from
     SkuSteam and Map to BATCH input ODATA
     Structure

     *** try catch for missing data nodes - not 100% on how this 
         will behave need some testing
     ************************************************************/ 
    
    try{
        xmlMarkup.'batchParts' {        
        while (jsonData.EAN[ITR]) {
           'batchChangeSet'{
               'batchChangeSetPart'{
                   'method'("POST")
                   'Products'{
                       'Product'{
                            try{ 
                                integrationKey(jsonData.EAN[ITR]) //real mapping
                            }catch(Exception ex){integrationKey("")}
                            try{
                                tgaid(jsonData."SKUvantageClassification/1.0/FMCGGeneral.austrnumber"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){tgaid("")}
                            try{
                                dosageform(jsonData."SKUvantageClassification/1.0/FMCGInstructions.usageadvice"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){dosageform("")}
                            try{
                                height("") //real mapping
                            }catch(Exception ex){height("")}
                            /** Start whatyouneedtoknow ******/
                            try{
                                def value = "";
                                try{
                                    value = jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet1"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet2"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet3"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{                                
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet4"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{                                
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet5"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{                                
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.bullet6"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                try{                                
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGFeatures.claims"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-") + " | "
                                }catch(Exception ex){}
                                whatelseyouneedtoknow(value) //real mapping
                            }catch(Exception ex){whatelseyouneedtoknow("")}
                            /*** end whatyouneedtoknow ***/
                            try{
                                keywords("") //real mapping
                            }catch(Exception ex){keywords("")}
                            try{
                                dosagedescription(jsonData."SKUvantageClassification/1.0/FMCGInstructions.usageadvice"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){dosagedescription("")}
                            try{
                                color(jsonData."SKUvantageClassification/1.0/FMCGGeneral.colour"[ITR])  //real mapping
                            }catch(Exception ex){color("")}
                            try{
                                swatchColor(jsonData."SKUvantageClassification/1.0/FMCGGeneral.colour"[ITR]) //real mapping
                            }catch(Exception ex){swatchColor("")}
                            try{
                                width("") //real mapping
                            }catch(Exception ex){width("")}
                            try{
                                weight("") //real mapping
                            }catch(Exception ex){weight("")}
                            try{
                                warnings(jsonData."SKUvantageClassification/1.0/FMCGSafety.productwarnings"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){warnings("")}
                            try{
                                instructions(jsonData."SKUvantageClassification/1.0/FMCGMaintenance.storageanddisposal"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){instructions("")}
                            try{
                                whatitdoes(jsonData.LongCopy[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){whatitdoes("")}
                            try{
                                formulation(jsonData."SKUvantageClassification/1.0/FMCGGeneral.productform"[ITR]) //real mapping
                            }catch(Exception ex){formulation("")}
                            try{
                                name(jsonData.Title[ITR]) //real mapping
                            }catch(Exception ex){Title("")}
                            try{
                                SAPean(jsonData.EAN[ITR]) //real mapping
                            }catch(Exception ex){SAPean("")}
                            try{
                                length("") //real mapping
                            }catch(Exception ex){length("")}
                            try{
                                suitableFor(jsonData."SKUvantageClassification/1.0/FMCGGeneral.suitablefor"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")) //real mapping
                            }catch(Exception ex){suitableFor("")}
                            /**** Start ingredients ****/
                            try{
                                def value = "";
                                try{//active ingredients
                                    value = jsonData."SKUvantageClassification/1.0/FMCGIngredients.activeingredients"[ITR]
                                }catch(Exception ex){}
                                try{//other ingredients
                                    value = value + jsonData."SKUvantageClassification/1.0/FMCGIngredients.contains"[ITR]
                                }catch(Exception ex){}
                                ingredients(value) //real mapping
                            }catch(Exception ex){ingredients("")}
                            /**** END Ingredients ****/
                            try{
                                brand(jsonData."SKUvantageClassification/1.0/SKULibDM.brand"[ITR]) //real mapping
                            }catch(Exception ex){brand("")}
                            try{
                                metaTags("") //real mapping
                            }catch(Exception ex){metaTags("")}
                            try{
                                whatitis(jsonData.ShortCopy[ITR]) //real mapping
                            }catch(Exception ex){whatitis("")}
                       }
                   }
               }
           }
        ITR = ITR + 1
        }
         
        }
    }catch(Exception e) {
        //Catch exceptiopn log inbound and outbound in the hopes it can be solved.
        messageLog.addAttachmentAsString("RequestPayload", body, "text/plain");
        messageLog.addAttachmentAsString("Result before exception Payload", xmlWriter.toString(), "text/plain");
    }

    outbound_message = xmlWriter.toString();
    
    message.setBody(outbound_message)
    //message.setBody(builder.toPrettyString())
    
    return message;    
    }

