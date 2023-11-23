package com.mygdx.game.utiltts;

public enum WallType{
    HARD(0, 5, true, false, false),
    SOFT(1, 3, true, false, false),
    INDESTUCTIBLE(2, 1, false, false, false),
    WATER(3, 1, false, false, true),
    NONE(0, 0, false, true, true),
    BASE(0, 25, true, false, true);

    private final int index;
    private final int Max_HP;
    private final boolean isUnitPassable;
    private final boolean isProjectilePassable;
    private final boolean destructible;

    public int getIndex() {
        return index;
    }

    public int getMax_HP() {
        return Max_HP;
    }

    public boolean isUnitPassable() {
        return isUnitPassable;
    }

    public boolean isProjectilePassable() {
        return isProjectilePassable;
    }

    public boolean isDestructible() {
        return destructible;
    }

    WallType(int index, int max_HP, boolean destructible, boolean UnitPassable, boolean ProjectilePassable) {
        this.index = index;
        this.Max_HP = max_HP;
        this.destructible = destructible;
        this.isUnitPassable = UnitPassable;
        this.isProjectilePassable = ProjectilePassable;
    }
}