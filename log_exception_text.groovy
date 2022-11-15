import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
   
    def body = message.getBody(Reader);
    def messageLog = messageLogFactory.getMessageLog(message);

    // get a map of properties
    def map = message.getProperties();
               
    // get an exception
    def ex = map.get("CamelExceptionCaught");
   
    if (ex!=null) {
        message.setProperty("Stock_Position_Exception",ex.getMessage());
    }
   
    messageLog.setStringProperty("Stock_Position_Exception", message.getBody(java.lang.String) as String );
    return message;
   
}