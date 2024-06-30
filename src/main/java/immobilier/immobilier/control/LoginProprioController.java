package immobilier.immobilier.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import immobilier.immobilier.model.Utilisateur;
import immobilier.immobilier.model.Login;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logProprio")
public class LoginProprioController {

    @GetMapping("/")
    public String actLog(Model model) {
        return "loginProprio";
    }

    @PostMapping("/login")
    public String login(RedirectAttributes red, @RequestParam String username, HttpServletRequest request,
            Model model) {
        try {
            Utilisateur equi = new Utilisateur(username);
            Login login = equi.log();

            
            request.getSession().setAttribute("user", login);
            model.addAttribute("user", login);

            return "redirect:/proprio/";
        } catch (Exception e) {
            e.printStackTrace();
            red.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";

    }

    @GetMapping("/error403")
    public String unauthorized() {
        return "error403";
    }
}