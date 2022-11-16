import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;
import groovy.xml.MarkupBuilder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import groovy.json.JsonBuilder;
    
    def Message processData(Message message) {
       // println "You can print and see the result in the console!"
        //Body 
        def body = message.getBody(String);

        //Headers 
        def jsonSlurper = new JsonSlurper();
        def jsonData = jsonSlurper.parseText(body);
        def map = message.getHeaders();
        //Properties 
        map = message.getProperties();

    def builder = new JsonBuilder()
     def messageLog = messageLogFactory.getMessageLog(message);
    try{
        builder {
            'tracking_number' jsonData.order_event.payload.tracking_number
            'tracking_url' jsonData.order_event.payload.tracking_url
            'current_state' jsonData.order_event.payload.current_state
            'retailer_order_number' jsonData.order_event.payload.retailer_order_number
        }
        message.setBody(builder.toPrettyString())
    }catch(Exception e) {
        //if the payload doesnt have the JSON path order_event.payload.?????? - pass through the payload as is ... and log it and continue on as if mapping was not required
        messageLog.addAttachmentAsString("RequestPayload", body, "text/plain");
    }
    
    
    return message;    
    }




/********************************
SAMPLE PAYLOAD FOR ABOVE MAPPING
********************************
{

        "order_event": {

        "external_order_id": "DEV-ORDER-A004",

        "type": "shipping",

        "created": "2022-11-11T05:19:49Z",

        "payload": {

            "tracking_number": "PPXNrswokcWpd",

            "expected_delivery_date": null,

            "tracking_url": "https://app.staging.shippit.com/tracking/ppxnrswokcwpd",

            "current_state": "ready_for_pickup",

            "retailer_order_number": "DEV-ORDER-A004",

            "retailer_reference": null,

            "courier_name": "Allied Express",

            "courier_job_id": "SHP132086116",

            "delivery_address": "1 Union Street",

            "delivery_suburb": "Pyrmont",

            "delivery_postcode": "2009",

            "delivery_state": "NSW",

            "delivery_country_code": "AU",

            "merchant_url": "",

            "delivery_latitude": -33.869781,

            "delivery_longitude": 151.194633,

            "status_history": [{

                "status": "ready_for_pickup",

                "time": "2022-11-11T05:18:45+00:00"

            }, {

                "status": "despatch_in_progress",

                "time": "2022-11-01T12:22:23+00:00"

            }, {

                "status": "order_placed",

                "time": "2022-10-17T23:12:33+00:00"

            }],

            "products": [{

                "title": "SKIN PROUD Everything",

                "sku": "73230",

                "quantity": 2,

                "product_line_id": null

            }, {

                "title": "A BIT HIPPY Moisturiser",

                "sku": "75133",

                "quantity": 4,

                "product_line_id": null

            }]

        }

    }

}
**/
