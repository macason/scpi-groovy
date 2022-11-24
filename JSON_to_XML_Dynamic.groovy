import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;
import groovy.xml.MarkupBuilder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import groovy.json.JsonBuilder;
import groovy.xml.MarkupBuilder

def jsonData;

    def Message processData(Message message) {

        def body = message.getBody(String);

        //remove \n and other control characters from payload
       // body = body.replaceAll("[^a-zA-Z0-9 ]+","")

        //Headers 
        def jsonSlurper = new JsonSlurper();
        jsonData = jsonSlurper.parseText(body);
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
                            integrationKey(getValue("EAN", ITR))
                            tgaid(getValue("SKUvantageClassification/1.0/FMCGGeneral.austrnumber", ITR))
                            dosageform(getValue("SKUvantageClassification/1.0/FMCGInstructions.usageadvice", ITR)) 
                            height("") //real mapping
                            whatelseyouneedtoknow(getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet1", ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet2", ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet3", ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet4" ,ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet5", ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.bullet6", ITR) + " | " +
                                    getValue("SKUvantageClassification/1.0/FMCGFeatures.claims" , ITR) 
                            )
                            keywords("") //real mapping
                            dosagedescription(getValue("SKUvantageClassification/1.0/FMCGInstructions.usageadvice", ITR)) //real mapping
                            color(getValue("SKUvantageClassification/1.0/FMCGGeneral.colour", ITR))  //real mapping
                            swatchColor(getValue("SKUvantageClassification/1.0/FMCGGeneral.colour", ITR))  //real mapping
                            width("") //real mapping
                            weight("") //real mapping
                            warnings(getValue("SKUvantageClassification/1.0/FMCGSafety.productwarnings", ITR)) //real mapping
                            instructions(getValue("SKUvantageClassification/1.0/FMCGMaintenance.storageanddisposal", ITR)) //real mapping
                            whatitdoes(getValue("LongCopy", ITR)) //real mapping
                            formulation(getValue("SKUvantageClassification/1.0/FMCGGeneral.productform", ITR)) //real mapping
                            name(getValue("Title", ITR)) //real mapping
                            SAPean(getValue("EAN", ITR)) //real mapping
                            length("") //real mapping
                            suitableFor(getValue("SKUvantageClassification/1.0/FMCGGeneral.suitablefor", ITR)) //real mapping
                            ingredients(getValue("SKUvantageClassification/1.0/FMCGIngredients.activeingredients", ITR) + " | " + getValue("SKUvantageClassification/1.0/FMCGIngredients.contains", ITR))
                            brand(getValue("SKUvantageClassification/1.0/SKULibDM.brand", ITR)) //real mapping
                            metaTags("") //real mapping
                            whatitis(getValue("ShortCopy", ITR)) //real mapping
                         
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

    /** dynamic query to dig through JSON and get values **/
    def getValue(def queryString, ITR){
        def value
        try{
            value = jsonData."${queryString}"[ITR].replaceAll("[^a-zA-Z0-9 ]+","-")
        }catch(Exception ex){
            value = ' '
        }
        println(value)
        return value
    }
