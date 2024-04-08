package sangwon.wead.controller.util;

import org.springframework.ui.Model;

public class AlertPageRedirector {

    public static String redirectAlertPage(String message, String redirectUrl, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("redirectUrl", redirectUrl);
        return "alert";
    }


}
