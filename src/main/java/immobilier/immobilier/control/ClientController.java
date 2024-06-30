package immobilier.immobilier.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {


    @GetMapping("/")
    public String actB(Model model) {
        model.addAttribute("page", "insert");
        return "index";
    }
}