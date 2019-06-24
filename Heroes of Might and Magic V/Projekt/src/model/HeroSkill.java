package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Patrick
 * Created: 03.06.2019
 * Purpose:
 */
public class HeroSkill {
    private String _skillCategory; // BasicSkillID | HERO_SKILL_NONE
    private String _heroType; // HeroClass | HERO_CLASS_NONE
    private List<List<HeroSkill>> _prerequisites;
}
