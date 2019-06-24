package io;

import parsing.xml.XMLDocument;
import parsing.xml.XMLTag;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Patrick
 * Created: 03.06.2019
 * Purpose:
 */
public class SkillData {
    public static final Path SKILLS_FILE = Path.of("Original GameMechanics", "RefTables", "Skills.xdb");

    public static List<String> loadSkillNames() {
        var skills = new ArrayList<String>();
        try {
            var xmlDocument = XMLDocument.ofFile(SKILLS_FILE);
            var root = xmlDocument.getRoot();
            var items = root.getTags("objects").get(0).getTags("Item");

            for (XMLTag item : items) {
                List<XMLTag> id = item.getTags("ID");

                if (!id.isEmpty()) {
                    skills.add(id.get(0).toString());
                }
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return skills;
    }


    public static void main(String[] args) {
        List<String> skillNames = loadSkillNames();
        skillNames.forEach(System.out::println);
    }

    /*
        <ID>HERO_SKILL_NONE</ID>
        <ID>HERO_SKILL_LOGISTICS</ID>
        <ID>HERO_SKILL_WAR_MACHINES</ID>
        <ID>HERO_SKILL_LEARNING</ID>
        <ID>HERO_SKILL_LEADERSHIP</ID>
        <ID>HERO_SKILL_LUCK</ID>
        <ID>HERO_SKILL_OFFENCE</ID>
        <ID>HERO_SKILL_DEFENCE</ID>
        <ID>HERO_SKILL_SORCERY</ID>
        <ID>HERO_SKILL_DESTRUCTIVE_MAGIC</ID>
        <ID>HERO_SKILL_DARK_MAGIC</ID>
        <ID>HERO_SKILL_LIGHT_MAGIC</ID>
        <ID>HERO_SKILL_SUMMONING_MAGIC</ID>
        <ID>HERO_SKILL_TRAINING</ID>
        <ID>HERO_SKILL_GATING</ID>
        <ID>HERO_SKILL_NECROMANCY</ID>
        <ID>HERO_SKILL_AVENGER</ID>
        <ID>HERO_SKILL_ARTIFICIER</ID>
        <ID>HERO_SKILL_INVOCATION</ID>
        <ID>HERO_SKILL_PATHFINDING</ID>
        <ID>HERO_SKILL_SCOUTING</ID>
        <ID>HERO_SKILL_NAVIGATION</ID>
        <ID>HERO_SKILL_FIRST_AID</ID>
        <ID>HERO_SKILL_BALLISTA</ID>
        <ID>HERO_SKILL_CATAPULT</ID>
        <ID>HERO_SKILL_INTELLIGENCE</ID>
        <ID>HERO_SKILL_SCHOLAR</ID>
        <ID>HERO_SKILL_EAGLE_EYE</ID>
        <ID>HERO_SKILL_RECRUITMENT</ID>
        <ID>HERO_SKILL_ESTATES</ID>
        <ID>HERO_SKILL_DIPLOMACY</ID>
        <ID>HERO_SKILL_RESISTANCE</ID>
        <ID>HERO_SKILL_LUCKY_STRIKE</ID>
        <ID>HERO_SKILL_FORTUNATE_ADVENTURER</ID>
        <ID>HERO_SKILL_TACTICS</ID>
        <ID>HERO_SKILL_ARCHERY</ID>
        <ID>HERO_SKILL_FRENZY</ID>
        <ID>HERO_SKILL_PROTECTION</ID>
        <ID>HERO_SKILL_EVASION</ID>
        <ID>HERO_SKILL_TOUGHNESS</ID>
        <ID>HERO_SKILL_MYSTICISM</ID>
        <ID>HERO_SKILL_WISDOM</ID>
        <ID>HERO_SKILL_ARCANE_TRAINING</ID>
        <ID>HERO_SKILL_MASTER_OF_ICE</ID>
        <ID>HERO_SKILL_MASTER_OF_FIRE</ID>
        <ID>HERO_SKILL_MASTER_OF_LIGHTNINGS</ID>
        <ID>HERO_SKILL_MASTER_OF_CURSES</ID>
        <ID>HERO_SKILL_MASTER_OF_MIND</ID>
        <ID>HERO_SKILL_MASTER_OF_SICKNESS</ID>
        <ID>HERO_SKILL_MASTER_OF_BLESSING</ID>
        <ID>HERO_SKILL_MASTER_OF_ABJURATION</ID>
        <ID>HERO_SKILL_MASTER_OF_WRATH</ID>
        <ID>HERO_SKILL_MASTER_OF_QUAKES</ID>
        <ID>HERO_SKILL_MASTER_OF_CREATURES</ID>
        <ID>HERO_SKILL_MASTER_OF_ANIMATION</ID>
        <ID>HERO_SKILL_HOLY_CHARGE</ID>
        <ID>HERO_SKILL_PRAYER</ID>
        <ID>HERO_SKILL_EXPERT_TRAINER</ID>
        <ID>HERO_SKILL_CONSUME_CORPSE</ID>
        <ID>HERO_SKILL_DEMONIC_FIRE</ID>
        <ID>HERO_SKILL_DEMONIC_STRIKE</ID>
        <ID>HERO_SKILL_RAISE_ARCHERS</ID>
        <ID>HERO_SKILL_NO_REST_FOR_THE_WICKED</ID>
        <ID>HERO_SKILL_DEATH_SCREAM</ID>
        <ID>HERO_SKILL_MULTISHOT</ID>
        <ID>HERO_SKILL_SNIPE_DEAD</ID>
        <ID>HERO_SKILL_IMBUE_ARROW</ID>
        <ID>HERO_SKILL_MAGIC_BOND</ID>
        <ID>HERO_SKILL_MELT_ARTIFACT</ID>
        <ID>HERO_SKILL_MAGIC_MIRROR</ID>
        <ID>HERO_SKILL_EMPOWERED_SPELLS</ID>
        <ID>HERO_SKILL_DARK_RITUAL</ID>
        <ID>HERO_SKILL_ELEMENTAL_VISION</ID>
        <ID>HERO_SKILL_ROAD_HOME</ID>
        <ID>HERO_SKILL_TRIPLE_BALLISTA</ID>
        <ID>HERO_SKILL_ENCOURAGE</ID>
        <ID>HERO_SKILL_RETRIBUTION</ID>
        <ID>HERO_SKILL_HOLD_GROUND</ID>
        <ID>HERO_SKILL_GUARDIAN_ANGEL</ID>
        <ID>HERO_SKILL_STUDENT_AWARD</ID>
        <ID>HERO_SKILL_GRAIL_VISION</ID>
        <ID>HERO_SKILL_CASTER_CERTIFICATE</ID>
        <ID>HERO_SKILL_ANCIENT_SMITHY</ID>
        <ID>HERO_SKILL_PARIAH</ID> // Gefallener Ritter
        <ID>HERO_SKILL_ELEMENTAL_BALANCE</ID>
        <ID>HERO_SKILL_ABSOLUTE_CHARGE</ID>
        <ID>HERO_SKILL_QUICK_GATING</ID>
        <ID>HERO_SKILL_MASTER_OF_SECRETS</ID>
        <ID>HERO_SKILL_TRIPLE_CATAPULT</ID>
        <ID>HERO_SKILL_GATING_MASTERY</ID>
        <ID>HERO_SKILL_CRITICAL_GATING</ID>
        <ID>HERO_SKILL_CRITICAL_STRIKE</ID>
        <ID>HERO_SKILL_DEMONIC_RETALIATION</ID>
        <ID>HERO_SKILL_EXPLODING_CORPSES</ID>
        <ID>HERO_SKILL_DEMONIC_FLAME</ID>
        <ID>HERO_SKILL_WEAKENING_STRIKE</ID>
        <ID>HERO_SKILL_FIRE_PROTECTION</ID>
        <ID>HERO_SKILL_FIRE_AFFINITY</ID>
        <ID>HERO_SKILL_ABSOLUTE_GATING</ID>
        <ID>HERO_SKILL_DEATH_TREAD</ID>
        <ID>HERO_SKILL_LAST_AID</ID>
        <ID>HERO_SKILL_LORD_OF_UNDEAD</ID>
        <ID>HERO_SKILL_HERALD_OF_DEATH</ID>
        <ID>HERO_SKILL_DEAD_LUCK</ID>
        <ID>HERO_SKILL_CHILLING_STEEL</ID>
        <ID>HERO_SKILL_CHILLING_BONES</ID>
        <ID>HERO_SKILL_SPELLPROOF_BONES</ID>
        <ID>HERO_SKILL_DEADLY_COLD</ID>
        <ID>HERO_SKILL_SPIRIT_LINK</ID>
        <ID>HERO_SKILL_TWILIGHT</ID>
        <ID>HERO_SKILL_HAUNT_MINE</ID>
        <ID>HERO_SKILL_ABSOLUTE_FEAR</ID>
        <ID>HERO_SKILL_DISGUISE_AND_RECKON</ID>
        <ID>HERO_SKILL_IMBUE_BALLISTA</ID>
        <ID>HERO_SKILL_CUNNING_OF_THE_WOODS</ID>
        <ID>HERO_SKILL_FOREST_GUARD_EMBLEM</ID>
        <ID>HERO_SKILL_ELVEN_LUCK</ID>
        <ID>HERO_SKILL_FOREST_RAGE</ID>
        <ID>HERO_SKILL_LAST_STAND</ID>
        <ID>HERO_SKILL_INSIGHTS</ID>
        <ID>HERO_SKILL_SUN_FIRE</ID>
        <ID>HERO_SKILL_SOIL_BURN</ID>
        <ID>HERO_SKILL_STORM_WIND</ID>
        <ID>HERO_SKILL_FOG_VEIL</ID>
        <ID>HERO_SKILL_ABSOLUTE_LUCK</ID>
        <ID>HERO_SKILL_MARCH_OF_THE_MACHINES</ID>
        <ID>HERO_SKILL_REMOTE_CONTROL</ID>
        <ID>HERO_SKILL_ACADEMY_AWARD</ID>
        <ID>HERO_SKILL_ARTIFICIAL_GLORY</ID>
        <ID>HERO_SKILL_SPOILS_OF_WAR</ID>
        <ID>HERO_SKILL_WILDFIRE</ID> // Flaming Arrows
        <ID>HERO_SKILL_SEAL_OF_PROTECTION</ID>
        <ID>HERO_SKILL_COUNTERSPELL</ID>
        <ID>HERO_SKILL_MAGIC_CUSHION</ID>
        <ID>HERO_SKILL_SUPRESS_DARK</ID>
        <ID>HERO_SKILL_SUPRESS_LIGHT</ID>
        <ID>HERO_SKILL_UNSUMMON</ID>
        <ID>HERO_SKILL_ABSOLUTE_WIZARDY</ID>
        <ID>HERO_SKILL_TELEPORT_ASSAULT</ID>
        <ID>HERO_SKILL_SHAKE_GROUND</ID>
        <ID>HERO_SKILL_DARK_REVELATION</ID>
        <ID>HERO_SKILL_FAST_AND_FURIOUS</ID>
        <ID>HERO_SKILL_LUCKY_SPELLS</ID>
        <ID>HERO_SKILL_POWER_OF_HASTE</ID>
        <ID>HERO_SKILL_POWER_OF_STONE</ID>
        <ID>HERO_SKILL_CHAOTIC_SPELLS</ID>
        <ID>HERO_SKILL_SECRETS_OF_DESTRUCTION</ID>
        <ID>HERO_SKILL_PAYBACK</ID>
        <ID>HERO_SKILL_ELITE_CASTERS</ID>
        <ID>HERO_SKILL_ELEMENTAL_OVERKILL</ID>
        <ID>HERO_SKILL_ABSOLUTE_CHAINS</ID>
        <ID>HERO_SKILL_RUNELORE</ID>
        <ID>HERO_SKILL_REFRESH_RUNE</ID>
        <ID>HERO_SKILL_STRONG_RUNE</ID>
        <ID>HERO_SKILL_FINE_RUNE</ID>
        <ID>HERO_SKILL_QUICKNESS_OF_MIND</ID>
        <ID>HERO_SKILL_RUNIC_MACHINES</ID>
        <ID>HERO_SKILL_TAP_RUNES</ID>
        <ID>HERO_SKILL_RUNIC_ATTUNEMENT</ID>
        <ID>HERO_SKILL_DWARVEN_LUCK</ID>
        <ID>HERO_SKILL_OFFENSIVE_FORMATION</ID>
        <ID>HERO_SKILL_DEFENSIVE_FORMATION</ID>
        <ID>HERO_SKILL_DISTRACT</ID>
        <ID>HERO_SKILL_SET_AFIRE</ID>
        <ID>HERO_SKILL_SHRUG_DARKNESS</ID>
        <ID>HERO_SKILL_ETERNAL_LIGHT</ID>
        <ID>HERO_SKILL_RUNIC_ARMOR</ID>
        <ID>HERO_SKILL_ABSOLUTE_PROTECTION</ID>
        <ID>HERO_SKILL_SNATCH</ID>
        <ID>HERO_SKILL_MENTORING</ID>
        <ID>HERO_SKILL_EMPATHY</ID>
        <ID>HERO_SKILL_PREPARATION</ID>
        <ID>HERO_SKILL_DEMONIC_RAGE</ID>
        <ID>HERO_SKILL_MIGHT_OVER_MAGIC</ID>
        <ID>HERO_SKILL_MEMORY_OF_OUR_BLOOD</ID>
        <ID>HERO_SKILL_POWERFULL_BLOW</ID>
        <ID>HERO_SKILL_ABSOLUTE_RAGE</ID>
        <ID>HERO_SKILL_PATH_OF_WAR</ID>
        <ID>HERO_SKILL_BATTLE_ELATION</ID>
        <ID>HERO_SKILL_LUCK_OF_THE_BARBARIAN</ID>
        <ID>HERO_SKILL_STUNNING_BLOW</ID>
        <ID>HERO_SKILL_DEFEND_US_ALL</ID>
        <ID>HERO_SKILL_GOBLIN_SUPPORT</ID>
        <ID>HERO_SKILL_BARBARIAN_LEARNING</ID>
        <ID>HERO_SKILL_POWER_OF_BLOOD</ID>
        <ID>HERO_SKILL_WARCRY_LEARNING</ID>
        <ID>HERO_SKILL_BODYBUILDING</ID>
        <ID>HERO_SKILL_VOICE</ID>
        <ID>HERO_SKILL_VOICE_TRAINING</ID>
        <ID>HERO_SKILL_MIGHTY_VOICE</ID>
        <ID>HERO_SKILL_VOICE_OF_RAGE</ID>
        <ID>HERO_SKILL_SHATTER_DESTRUCTIVE_MAGIC</ID>
        <ID>HERO_SKILL_CORRUPT_DESTRUCTIVE</ID>
        <ID>HERO_SKILL_WEAKEN_DESTRUCTIVE</ID>
        <ID>HERO_SKILL_DETAIN_DESTRUCTIVE</ID>
        <ID>HERO_SKILL_SHATTER_DARK_MAGIC</ID>
        <ID>HERO_SKILL_CORRUPT_DARK</ID>
        <ID>HERO_SKILL_WEAKEN_DARK</ID>
        <ID>HERO_SKILL_DETAIN_DARK</ID>
        <ID>HERO_SKILL_SHATTER_LIGHT_MAGIC</ID>
        <ID>HERO_SKILL_CORRUPT_LIGHT</ID>
        <ID>HERO_SKILL_WEAKEN_LIGHT</ID>
        <ID>HERO_SKILL_DETAIN_LIGHT</ID>
        <ID>HERO_SKILL_SHATTER_SUMMONING_MAGIC</ID>
        <ID>HERO_SKILL_CORRUPT_SUMMONING</ID>
        <ID>HERO_SKILL_WEAKEN_SUMMONING</ID>
        <ID>HERO_SKILL_DETAIN_SUMMONING</ID>
        <ID>HERO_SKILL_DEATH_TO_NONEXISTENT</ID>
        <ID>HERO_SKILL_BARBARIAN_ANCIENT_SMITHY</ID>
        <ID>HERO_SKILL_BARBARIAN_WEAKENING_STRIKE</ID>
        <ID>HERO_SKILL_BARBARIAN_SOIL_BURN</ID>
        <ID>HERO_SKILL_BARBARIAN_FOG_VEIL</ID>
        <ID>HERO_SKILL_BARBARIAN_INTELLIGENCE</ID>
        <ID>HERO_SKILL_BARBARIAN_MYSTICISM</ID>
        <ID>HERO_SKILL_BARBARIAN_ELITE_CASTERS</ID>
        <ID>HERO_SKILL_BARBARIAN_STORM_WIND</ID>
        <ID>HERO_SKILL_BARBARIAN_FIRE_PROTECTION</ID>
        <ID>HERO_SKILL_BARBARIAN_SUN_FIRE</ID>
        <ID>HERO_SKILL_BARBARIAN_DISTRACT</ID>
        <ID>HERO_SKILL_BARBARIAN_DARK_REVELATION</ID>
        <ID>HERO_SKILL_BARBARIAN_MENTORING</ID>

     */

}