package immobilier.immobilier.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import immobilier.immobilier.model.Admin;
import immobilier.immobilier.model.Login;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/logAdmin")
public class LoginAdminController {

    @GetMapping("/")
    public String actLog(Model model) {
        return "loginAdmin";
    }

    @PostMapping("/login")
    public String login(RedirectAttributes red,@RequestParam String username, @RequestParam String password, HttpServletRequest request,
            Model model){
        try {
            
            Admin admin = new Admin(username, password);
            Login login = admin.logAdmin();

            request.getSession().setAttribute("user", login);
            model.addAttribute("user", login);

            return "redirect:/admin/";
        } catch (Exception e) {
            e.printStackTrace();
            red.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/logAdmin/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";

    }

}   