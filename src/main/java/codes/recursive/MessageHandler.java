package codes.recursive;

import com.github.palindromicity.syslog.SyslogParser;
import com.github.palindromicity.syslog.SyslogParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MessageHandler implements Runnable {
    private final Socket clientSocket;
    private final SyslogParser parser = new SyslogParserBuilder().build();
    private final Logger logger = LoggerFactory.getLogger(Main.class);

    public MessageHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String incomingMsg;
        try{
            while( (incomingMsg = reader.readLine()) != null ) {
                //String syslog = "<11>1 2020-06-15T14:46:35Z runner-00001700e5f9 app_id=ocid1.fnapp.oc1.phx...,fn_id=ocid1.fnfunc.oc1.phx... 11 app_id=ocid1.fnapp.oc1.phx...,fn_id=ocid1.fnfunc.oc1.phx... - 01JVJ12A411BT071RZJ000B7EP - root - INFO - Inside Python Hello World function";

                //String regex = "^<(?<pri>\\d{1,3})>(?<version>\\d{1,2}) (?<timestamp>[^ ]+) (?<hostName>[^ ]+) (?<appName>[^ ]+) (?<procId>[^ ]+) (?<msgId>[^ ]+) (?<structuredData>(\\-|\\[.*?\\]))(?: (?<msg>.*))?$";
                String regex = "^<(?<pri>\\d{1,3})>(?<version>\\d{1,2}) (?<timestamp>[^ ]+) (?<hostName>[^ ]+) (?<appName>[^ ]+) (?<procId>[^ ]+) app_id=(?<appId>[^ ]+),fn_id=(?<fnId>[^ ]+) (?<structuredData>(\\-|\\[.*?\\]))(?: (?<msg>.*))?$";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(incomingMsg);

                if (matcher.matches()) {
                    String pri = matcher.group("pri");
                    String version = matcher.group("version");
                    String timestamp = matcher.group("timestamp");
                    String hostName = matcher.group("hostName");
                    String appName = matcher.group("appName");
                    String procId = matcher.group("procId");
                    //String msgId = matcher.group("msgId");
                    String fnId = matcher.group("fnId");
                    String structuredData = matcher.group("structuredData");
                    String msg = matcher.group("msg");

                    logger.info(timestamp + " " + fnId + " " + msg);
                }

                //Map<String, Object> result = parser.parseLine(incomingMsg);
            /*
                gives us a nicely formatted Map
                containing lots of information.
                for example:
                {
                    "syslog.header.appName": "app_id=ocid1.fnapp.oc1.phx...,fn_id=ocid1.fnfunc.oc1.phx...",
                    "syslog.header.version": "1",
                    "syslog.header.hostName": "runner-00001700e5f9",
                    "syslog.header.facility": "1",
                    "syslog.header.msgId": "app_id=ocid1.fnapp.oc1.phx...,fn_id=ocid1.fnfunc.oc1.phx...",
                    "syslog.header.timestamp": "2020-06-15T14:46:35Z",
                    "syslog.message": "Error in function: ReferenceError: foo is not defined",
                    "syslog.header.pri": "11",
                    "syslog.header.procId": "8",
                    "syslog.header.severity": "3"
                }
            */
                //logger.info( result.get("syslog.message").toString() );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
