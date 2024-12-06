package io.shiftmanager.you.controller.web;

import io.shiftmanager.you.model.Shift;
import io.shiftmanager.you.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftWebController {
    private final ShiftService shiftService;

    // シフト一覧画面表示
    @GetMapping
    public String showShiftList(Model model) {
        // Thymeleafテンプレートに渡すデータをmodelに追加
        return "shifts/list";
    }

    // シフト登録画面表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("shift", new Shift());
        return "shifts/register";
    }

    // シフト登録処理
    @PostMapping("/register")
    public String register(@ModelAttribute Shift shift) {
        shiftService.registerShift(shift);
        return "redirect:/shifts";
    }
}