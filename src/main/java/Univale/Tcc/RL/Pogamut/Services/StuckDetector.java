/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Univale.Tcc.RL.Pogamut.Services;

import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.IUT2004Navigation;

/**
 *
 * @author winicius_2
 */
public class StuckDetector {

    private final int threshold;

    private final IUT2004Navigation navigation;

    private int notMoving = 0;

    private final AgentInfo info;

    public StuckDetector(int threshold, IUT2004Navigation navigation, AgentInfo info) {
        this.threshold = threshold;
        this.navigation = navigation;
        this.info = info;
    }

    public boolean check() {


        boolean result = false;
        if (!info.isMoving()) {
            notMoving++;
            if (notMoving > threshold) {
                notMoving = 0;
                result = true;
            }
        } else {
            notMoving = 0;
        }
        return result;
    }

}
