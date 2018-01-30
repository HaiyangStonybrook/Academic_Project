/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

/**
 *
 * @author hy
 */
public class Team  <E extends Comparable<E>> implements Comparable<E>{
    private String name;
    private String color;
    private String textColor;
    private String link;

    public Team(String name, String color, String textColor, String link) {
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int compareTo(E newTeam) {
        return getName().toString().compareTo(((Team)newTeam).getName().toString());
    }
    
    
}
