package com.project.charforge.service.impl.items;

import com.project.charforge.dao.interfaces.ItemDao;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.items.IItemService;

import java.util.List;

public class ItemService implements IItemService {
    private final ItemDao itemDao;

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.findAll();
    }
}
