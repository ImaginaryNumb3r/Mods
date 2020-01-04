package spell;

import lombok.Data;
import misc.FieldHandle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: Patrick
 * Created: 01.05.2019
 * Purpose:
 */
@SuppressWarnings("unused") // Reflective Access
@Data
public class Spell {
    public String SPELL_NAME;
    public Integer SP_NORMAL;
    public Integer SP_EXPERT;
    public Integer SP_MASTER;
    public Integer SP_GRANDMASTER;
    public Integer DELAY_NORMAL;
    public Integer DELAY_EXPERT;
    public Integer DELAY_MASTER;
    public Integer DELAY_GRANDMASTER;
    public Integer DAMAGE_ADD;
    public Integer DAMAGE_DICE;
    public Mastery MASTERY = Mastery.NORMAL;
    public School SCHOOL = School.AIR;

    private Spell() { }

    public static Spell parse(String line) {
        String[] parts = line.split("\t");
        Object[] objects = Arrays.stream(parts).map(Spell::parseItem).toArray();

        return FieldHandle.setFields(objects, Spell::new);
    }

    public int damage(int level) {
        int simpleDamage = simpleDamage(level);
        int scalar = 1;

        // Fire Spike: max 6, max 8, max 10

        Mastery mastery = Mastery.parseLevel(level);
        switch (SPELL_NAME) {
            case "Poison Spray":
                switch (mastery) {
                    case NORMAL: scalar = 1; break;
                    case EXPERT: scalar = 3; break;
                    case MASTER: scalar = 5; break;
                    case GRANDMASTER: scalar = 7; break;
                } break;
            case "Meteor Shower":
                switch (mastery) {
                    case MASTER: scalar = 16; break;
                    case GRANDMASTER: scalar = 20; break;
                    default: scalar = 0; break;
                } break;
            case "Starburst":
                switch (mastery) {
                    case MASTER: scalar = 16; break;
                    case GRANDMASTER: scalar = 20; break;
                    default: scalar = 0;
                } break;
            case "Shrapmetal":
                switch (mastery) {
                    case NORMAL: scalar = 3; break;
                    case EXPERT: scalar = 5; break;
                    case MASTER: scalar = 7; break;
                    case GRANDMASTER: scalar = 9; break;
                } break;
            case "Sparks":
                switch (mastery) {
                    case NORMAL: scalar = 3; break;
                    case EXPERT: scalar = 5; break;
                    case MASTER: scalar = 7; break;
                    case GRANDMASTER: scalar = 9; break;
                } break;
        }

        return scalar * simpleDamage;
    }

    private int simpleDamage(int level) {
        int scalableDamage = (DAMAGE_DICE + 1) / 2;

        return scalableDamage * level + DAMAGE_ADD;
    }

    public int spellCost(int level) {
        Mastery mastery = Mastery.parseLevel(level);
        switch (mastery) {
            case GRANDMASTER: return SP_GRANDMASTER;
            case MASTER: return SP_MASTER;
            case EXPERT: return SP_EXPERT;
            case NORMAL: default: return SP_NORMAL;
        }
    }

    public int recoveryTime(int level) {
        Mastery mastery = Mastery.parseLevel(level);
        switch (mastery) {
            case GRANDMASTER: return DELAY_GRANDMASTER;
            case MASTER: return DELAY_MASTER;
            case EXPERT: return DELAY_EXPERT;
            case NORMAL: default: return DELAY_NORMAL;
        }
    }

    @Override
    public String toString() {
        var fields = FieldHandle.getFields(this);
        List<String> fieldNames = fields.stream().map(Object::toString).collect(Collectors.toList());

        return String.join(", ", fieldNames);
    }

    private static Object parseItem(String item) {
        try  {
            return Integer.valueOf(item);
        } catch (NumberFormatException ex) {
            return item;
        }
    }
}
