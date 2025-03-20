package com.apple.shop.item;

import com.apple.shop.comment.Comment;
import com.apple.shop.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final S3Service s3Service;
    private final CommentRepository commentRepository;

    @GetMapping("/list")
    String list(Model model) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(0, 5));
        model.addAttribute("items", result.getContent());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", result.getTotalPages());
        return "list.html";
    }

    @GetMapping("/list/page/{num}")
    String getListPage(Model model, @PathVariable Integer num) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(num - 1, 5));
        model.addAttribute("items", result.getContent());
        model.addAttribute("currentPage", num);
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
        if (result.isPresent()) {
            model.addAttribute("data", result.get());

            List<Comment> comments = commentRepository.findAllByParentId(id);
            model.addAttribute("comments", comments);

            return "detail.html";
        } else {
            return "redirect:/list";
        }
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("data", result.get());
            return "edit.html";
        } else {
            return "redirect:/list";
        }
    }

    @PostMapping("/edit")
    String editItem(Long id, String title, Integer price) {
        itemService.editItem(id, title, price);
        return "redirect:/list";
    }

    @DeleteMapping("/item")
    ResponseEntity<String> deleteItem(@RequestParam Long id) {
        itemRepository.deleteById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        String result = s3Service.createPresignedUrl("test/" + filename);
        return result;
    }
}
