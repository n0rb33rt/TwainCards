package com.norbert.notification.util;

public class EmailBuilder {
    public static String buildEmailConfirmationMessage(BuildingEmailMessageRequest request){
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c;background-color:#f6f8fa;padding:20px\">\n" +
                "\n" +
                "  <div style=\"font-size:28px;font-weight:bold;color:#ffffff;text-align:center;margin-bottom:10px;background-color:#000000;height:50px;line-height:50px\">\n" +
                "    <span style=\"color:#ffffff;\">Confirm your account</span>\n" +
                "  </div>\n" +
                "\n" +
                "  <div style=\"font-size:19px;line-height:25px;color:#0b0c0c;margin-bottom:20px\">\n" +
                "    Hi, " + request.name() + ".<br><br>\n" +
                "    Thank you for registering. Please click on the below link to activate your account:<br><br>\n" +
                "    <a href=\"" + request.link() + "\" style=\"background-color:#1D70B8;color:#ffffff;font-size:19px;padding:10px 20px;text-decoration:none;border-radius:4px\">Activate Now</a><br><br>\n" +
                "    Link will expire in 24 hours.<br>\n" +
                "    See you soon!\n" +
                "  </div>\n" +
                "\n" +
                "</div>";
    }
}
