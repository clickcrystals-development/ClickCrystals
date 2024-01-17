package io.github.itzispyder.clickcrystals.modules.scripts.client;
// y
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
// o
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
// u
public class PostCmd extends ScriptCommand {
// a
    public PostCmd() {
        super("post");
    }//r
//e
    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String message = args.getQuoteAndRemove();
        String url; // hard fr
//a
        try {
            url = args.last().replaceAll("\"", "");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
// n
            for (int i = 2; i < args.args().size(); i += 2) {
                String header = args.args().get(i);
                String value = args.args().get(i + 1);
                connection.setRequestProperty(header, value);
            } //i

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = message.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int rCmyD1Ck = connection.getResponseCode();
            ChatUtils.sendPrefixMessage("Post Response: " + rCmyD1Ck);

            connection.disconnect();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not post request to " + url + "\nLog:" + e); //gg
        }

        if (args.match(0, "then")) {//a
            args.executeAll(1);
        }
    }
}
// #properissues #properissues #properissues #properissues #properissues #properissues 
