package com.homespliced.encounterbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keith on 12/12/2014.
 */
public class Monster {
    public double cr;
    public String size;
    public String type;
    public int experience;
    public int speed;

    private ArrayList<Double> stats = new ArrayList<Double>(6);
    public int strength;
    public int constitution;
    public int dexterity;
    public int intelligence;
    public int wisdom;
    public int charisma;

    public Monster(double cr, String type) {
        this.cr = cr;
        this.type = type;
        GetSize();
        GetStats();
        GetXP();
    }

    private void GetSize() {
        double prob = Math.random() * 100 + 1;
        if (prob  > 110 - cr * 2) {
            size = "Huge";
        } else if (prob > 80 - cr) {
            size = "Large";
        } else if (prob > 40 - cr) {
            size = "Medium";
        } else {
            size = "Small";
        }
    }

    private void GetStats() {
        GenerateStats();
        double finalStats[] = {0,0,0,0,0,0};
        ArrayList<Double> highStats = new ArrayList<Double>(3);
        ArrayList<Double> lowStats = new ArrayList<Double>(3);

        for(int i = 0; i < 3; i++) {
            highStats.add(Collections.max(stats));
            stats.remove(Collections.max(stats));
            lowStats.add(Collections.min(stats));
            stats.remove(Collections.min(stats));
        }

        //Bruisers have very high Strength and Constitution and low AC.
        if (type.equals("Bruiser")) {
            finalStats[2] = Collections.min(highStats); //Medium Dexterity
            highStats.remove(Collections.min(highStats));

            Collections.shuffle(highStats);
            Collections.shuffle(lowStats);

            finalStats[0] = highStats.get(0) + 2; //Really High Strength
            finalStats[1] = highStats.get(1) + 2; //Really High Constitution

            finalStats[3] = lowStats.get(0);
            finalStats[4] = lowStats.get(1);
            finalStats[5] = lowStats.get(2);
        }
        else if (type.equals("Artillery")) {
            finalStats[2] = Collections.max(highStats); //High Dexterity
            highStats.remove(Collections.max(highStats));

            highStats.add(Collections.max(lowStats));
            lowStats.remove(Collections.max(lowStats));

            Collections.shuffle(highStats);
            Collections.shuffle(lowStats);

            finalStats[1] = highStats.get(0); //Potentially high Constitution
            finalStats[3] = highStats.get(1); //Potentially high Intelligence
            finalStats[4] = lowStats.get(0); //Potentially high Wisdom

            finalStats[0] = lowStats.get(1); //Low Strength
            finalStats[5] = lowStats.get(2); //Low Charisma
        }
        //TODO- Cover the rest of the types with specific ability distributions.


        strength = Math.round((float)finalStats[0]);
        constitution = Math.round((float)finalStats[1]);
        dexterity = Math.round((float)finalStats[2]);
        intelligence = Math.round((float)finalStats[3]);
        wisdom = Math.round((float)finalStats[4]);
        charisma = Math.round((float)finalStats[5]);
    }

    //To be clear- We get the average stat by cr, then we create a point pool of remaining points after
    //every stat has the average. We go through the array of stats, giving each stat the average floor.
    //Then we add back on a random number
    private void GenerateStats() {
        double AverageStat = 0.5 * cr + 10.5;
        double pointPool = (AverageStat * 6) - ((AverageStat - 5) * 6);
        double statVar;

        pointPool += Math.random() * 3 - 1.5;

        //Give stats average floor of abilities by level
        for (int i = 0; i < 6; i++) {
            stats.set(i, AverageStat - 5);
        }

        // Go through adding remaining points randomly to stats. These will be shuffled!
        while (pointPool > 0) {
            for (int i = 0; i < 6; i++) {
                if (stats.get(i) < AverageStat + 5) {
                    statVar = Math.random() * 7 - 1;
                    if (pointPool - statVar > 0) {
                        stats.set(i, stats.get(i) + statVar);
                        pointPool -= statVar;
                    } else {
                        stats.set(i, stats.get(i) + pointPool);
                        pointPool = 0;
                        break;
                    }
                }
            }
        }
    }

    private void GetXP() {
        Map<Double, Integer> xpConversion = new HashMap<Double, Integer>();
        xpConversion.put(2.0, 450);
        xpConversion.put(3.0, 700);
        xpConversion.put(4.0, 1100);
        xpConversion.put(5.0, 1700);
        xpConversion.put(6.0, 2300);
        xpConversion.put(7.0, 3000);
        xpConversion.put(8.0, 3900);
        xpConversion.put(9.0, 4800);
        xpConversion.put(10.0, 5900);


        if (cr <= (1/8)) {
            experience = 25;
        } else if (cr <= (1/4)) {
            experience = 50;
        } else if (cr <= (1/2)) {
            experience = 100;
        } else if (cr <= 1) {
            experience = 200;
        } else {
          experience = xpConversion.get(cr);
        }
    }

    private void GetSpeed() {
        Map<String, Integer> spdConversion = new HashMap<String, Integer>();
        spdConversion.put("Tiny", 10);
        spdConversion.put("Small", 20);
        spdConversion.put("Medium", 30);
        spdConversion.put("Large", 40);
        spdConversion.put("Huge", 50);
        spdConversion.put("Gargantuan", 60);
    }

}