package model;

import parsing.xml.XMLDocument;
import parsing.xml.XMLTag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static lib.XML.getChild;
import static lib.XML.getData;

/**
 * Creator: Patrick
 * Created: 28.05.2019
 * Purpose:
 */
public class Creature {
    private transient final XMLTag _xml;
    private final String name;
    private final Faction faction;
    private final int attack;
    private final int defense;
    private final int shots;
    private final int range;
    private final int minDamage;
    private final int maxDamage;
    private final int speed;
    private final int initiative;
    private final boolean flying;
    private final int health;
    private final int exp;
    private final int weeklyGrowth;
    private final int power;
    private final int creatureTier;
    private final int gold;
    private final int resource;

    public static Creature parse(String name, XMLDocument xml, Faction faction) {
        try {
            return new Creature(name, xml, faction);
        } catch (RuntimeException ex) {
            System.out.println("ignored: " + name);
            return null;
        }
    }

    public Creature(String name, XMLDocument xml, Faction faction) {
        _xml = xml.getRoot();
        this.name = name;
        this.faction = faction;

        attack  = parseInt(getData(_xml, "AttackSkill"));
        defense = parseInt(getData(_xml, "DefenceSkill"));
        shots   = parseInt(getData(_xml, "Shots"));
        minDamage = parseInt(getData(_xml, "MinDamage"));
        maxDamage = parseInt(getData(_xml, "MaxDamage"));
        speed = parseInt(getData(_xml, "Speed"));
        initiative = parseInt(getData(_xml, "Initiative"));
        flying = parseBoolean(getData(_xml, "Flying"));
        health = parseInt(getData(_xml, "Health"));
        exp = parseInt(getData(_xml, "Exp"));
        power = parseInt(getData(_xml, "Power"));
        creatureTier = parseInt(getData(_xml, "CreatureTier"));
        range = parseInt(getData(_xml, "Range"));
        weeklyGrowth = parseInt(getData(_xml, "WeeklyGrowth"));

        int resources = 0;
        int gold = 0;

        var costNode = getChild(_xml, "Cost");
        for (XMLTag resource : costNode.children()) {
            if (resource.getName().equals("Gold")) {
                gold = parseInt(getData(resource));
            } else {
                resources += parseInt(getData(resource));
            }
        }

        resource = resources;
        this.gold = gold;
    }

    public static Field[] getFields() {
        return Stream.of(Creature.class.getDeclaredFields())
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .toArray(Field[]::new);
    }

    public static String[] getFieldNames() {
        return Stream.of(Creature.class.getDeclaredFields())
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public Object[] getValues() {
        Field[] fields = getFields();
        Object[] values = new Object[fields.length];

        try {
            int i = 0;
            for (Field field : fields) {
                Object value = field.get(this);
                values[i] = value;
                ++i;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return values;
    }

}
