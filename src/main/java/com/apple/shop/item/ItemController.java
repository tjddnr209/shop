package com.apple.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final S3Service s3Service;

    @GetMapping({"/list", "/list/page/{num}"})
    String getListPage(Model model, @PathVariable(required = false) Integer num) {
        int pageNum = (num == null) ? 1 : num;
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(pageNum - 1, 5));
        model.addAttribute("items", result.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", result.getTotalPages());
        return "list.html";
    }

    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    @PostMapping("/add")
    String writePost(@RequestParam String title,
                     @RequestParam Integer price,
                     @RequestParam(required = false) String imageUrl) {
        itemService.saveItem(title, price, imageUrl);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        result.ifPresent(item -> model.addAttribute("data", item));
        return result.isPresent() ? "detail.html" : "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        result.ifPresent(item -> model.addAttribute("data", item));
        return result.isPresent() ? "edit.html" : "redirect:/list";
    }

    @PostMapping("/edit")
    String editItem(Long id, String title, Integer price) {
        itemService.editItem(id, title, price);
        return "redirect:/list";
    }

    @DeleteMapping("/item")
    ResponseEntity<String> deleteItem(@RequestParam Long id) {
        itemRepository.deleteById(id);
        return ResponseEntity.ok("삭제완료");
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        return s3Service.createPresignedUrl("test/" + filename);
    }
}
