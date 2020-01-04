# Baseline
If you know what you are doing, the difficulty in Might and Magic can be laughably low. With a good party, a bit of grinding and getting some important spells early on can make your playthrough trivial.  
I intend to mod the game to make it a challenging experience. However, since the game features about 260 monsters you cannot do this by hand.  
And even if you undertook the painstaking work of adjusting each monster individually, how to quantify the strength of a monster on a case by case basis?
On my attempt to achieve higher difficulty, I hope to find a satisfying solution.  
 
# The basics
A first and easy approach is to simply scale the health (and other attributes) of enemies by a fixed number.   
However, while this does lead to an increased difficulty early on, the player progression is so immense that the player inevitably exceeds the scaled difficulty.  
The result is a very similar experience that has longer lasting encounters, but not more challenging ones. The old tactics to defeat an enemy are more or less the same and there is no new mechanic in terms of counterplay.

# First Attempt

Very uneven distribution of difficulty
Exponential difficulty increase

Young Behemoth has level 45, a normal one is level 60 and an "Ancient Behemoth" is level 85.

Not fun and hopeless. Teeth grinding experience that demanded an exhausting playstyle.

# Second Attempt
Much better, wored until the late-game. Some aspects are neglected:
    - Some spells of enemies were devastating, others trivial
    - HP scaled well, but damage was too low (linear scaling proved insufficient)
    - Good structure, but more polish necessary.
    - Having done a playthrough, I have the knowledge to elevate the interesting mechanics and encounters. 
      On top, some difficult encounters lead to frustration and cheesy tactics. I can now put forth mechanics to prevent this. 

# Mechanics in Might and Magic
## Monster HP
How much HP each monster has is decided completely deterministically. As one might suspect, HP in M&M increases exponentially, where a monster of level N has X more HP than a monster of level N-1.  
This variable "X" (the "HP per level") is in creased by 1 for all 5 levels on average.

HP Caclulated: HP(N) = HP(N - 1) + HP_ADD(N / 5)
Where for HP(1) = 3 and HP_ADD(1) = 3.

As a side note, this calculation is a bit simplified because the frequency by which HP_ADD increases by one alternates between 6 and 4 (averaging out on 5).
However, since the difference is so marginal, I will do my calculations in straight steps of 5. 

## Monster EXP

Similar to HP, but this can be expressed as a more simple formula: EXP(N) = Sum(1 to n) of (11 + n * 2)
