package com.project.charforge.service.impl.characters;

import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.dao.interfaces.RaceDao;
import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.service.interfaces.characters.ICharacterService;

import java.util.List;

public class CharacterService implements ICharacterService {
    private static final int TEMP_CHARACTER_ID = 0;

    private final CharacterDao characterDao;
    private final InventoryDao inventoryDao;
    private final RaceDao raceDao;
    private final CharClassDao classDao;

    public CharacterService(
            CharacterDao characterDao,
            InventoryDao inventoryDao,
            RaceDao raceDao,
            CharClassDao classDao)
    {
        this.characterDao = characterDao;
        this.inventoryDao = inventoryDao;
        this.raceDao = raceDao;
        this.classDao = classDao;
    }

    @Override
    public PlayerCharacter createCharacter(String name, Gender gender, Race race, CharClass charClass) {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setId(TEMP_CHARACTER_ID);
        pc.setName(name);
        pc.setGender(gender);
        pc.setRace(race);
        pc.setCharClass(charClass);

        return pc;
    }

    @Override
    public void saveCharacterToDB(PlayerCharacter character) {
        int id = characterDao.save(character);
        if (id == -1) throw new RuntimeException("Failed to save character");
        character.setId(id);

        for (InventoryItem item : character.getInventory()) {
            inventoryDao.addItemToCharacter(id, item.getItem().getId(), item.getGridIndex());
        }
    }

    @Override
    public List<Race> getAllRaces() {
        return raceDao.findAll();
    }

    @Override
    public List<CharClass> getAllClasses() {
        return classDao.findAll();
    }

    @Override
    public List<PlayerCharacter> findAllCharacters() {
        return characterDao.findAll();
    }

    @Override
    public boolean deleteCharacter(int characterId){
        return characterDao.delete(characterId);
    }
}
