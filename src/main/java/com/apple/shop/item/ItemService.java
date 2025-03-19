package com.apple.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(String title, Integer price, String imageUrl) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setImageUrl(imageUrl);
        itemRepository.save(item);
    }

    public void editItem(Long id, String title, Integer price) {
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
    }
}
