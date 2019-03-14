package com.midterm.group1.herolist;

import java.io.Serializable;

public class Hero implements Serializable {
    private String imageIcon;
    private String image;
    private String name;
    private String location;
    private String type;
    private int iconid;
    private int viability;
    private int attack;
    private int skill;
    private int difficulty;
    private String story;

    public Hero(String imageIcon, String image, String name, String location, String type, int id, int viability, int attack, int skill, int difficulty, String story){
        this.imageIcon = imageIcon;
        this.image = image;
        this.name = name;
        this.location = location;
        this.type = type;
        this.iconid = id;
        this.viability = viability;
        this.attack = attack;
        this.skill = skill;
        this.difficulty = difficulty;
        this.story = story;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public int getIconid(){
        return iconid;
    }

    public void setImageIcon(String icon){
        this.imageIcon = icon;
    }

    public void setImage(String img){
        this.image = img;
    }

    public void setName(String nm){
        this.name = nm;
    }

    public void setLocation(String loc){
        this.location = loc;
    }

    public void setType(String ty){
        this.type = ty;
    }

    public void setIconid(int id){
        this.iconid = id;
    }

    public int getViability() {
        return viability;
    }

    public void setViability(int viability) {
        this.viability = viability;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getSkill() {
        return skill;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}

