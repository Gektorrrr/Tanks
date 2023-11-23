package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utiltts.WallType;

public class Map {

    private class Cell {
        WallType type;
        int hp;

        public Cell(WallType type){
            this.type = type;
            this.hp = type.getMax_HP();
        }

        public void damage(){
            if(type.isDestructible()){
                hp--;
                if(hp <= 0)
                    type = WallType.NONE;
            }
        }

        public void changeType(WallType type){
            this.type = type;
            this.hp = type.getMax_HP();
        }
    }

    public static int getSize_X() {
        return Size_X * Cell_size;
    }

    public static int getSize_Y() {
        return Size_Y * Cell_size;
    }

    public static final int Size_X = 256;
    public static final int Size_Y = 36;
    public static final int Cell_size = 20;
    private final TextureRegion texture;
    private final TextureRegion[][] wallsTexture;
    private final TextureRegion button;
    private final Cell[][] cells;

    public Map(TextureAtlas atlas){
        this.button = atlas.findRegion("floorButton");
        this.texture = atlas.findRegion("grass40");
        this.wallsTexture = new TextureRegion(atlas.findRegion("obstacles")).split(Cell_size, Cell_size);
        this.cells = new Cell[Size_X][Size_Y];
        for (int i = 0; i < Size_X; i++)
            for (int j = 0; j < Size_Y; j++)
                cells[i][j] = new Cell(WallType.NONE);

        for (int i = 0; i < Size_X; i++){
            cells[i][0].changeType(WallType.INDESTUCTIBLE);
            cells[i][Size_Y - 1].changeType(WallType.INDESTUCTIBLE);
        }

        for (int i = 0; i < Size_Y; i++){
            cells[0][i].changeType(WallType.INDESTUCTIBLE);
            cells[Size_X - 1][i].changeType(WallType.INDESTUCTIBLE);
        }

        for (int i = Size_X - 10; i < Size_X - 1; i++)
            for (int j = Size_Y - 22; j < Size_Y - 14; j++)
                cells[i][j].changeType(WallType.BASE);

        for (int i = 10; i < Size_X - 10; i+=15) {
            for (int j = 8; j < Size_Y; j+=10) {
                if(MathUtils.random() <= 0.5)
                {
                    cells[i][j].changeType(WallType.HARD);
                    cells[i + 1][j + 1].changeType(WallType.HARD);
                    cells[i + 2][j + 2].changeType(WallType.HARD);
                    cells[i + 3][j + 3].changeType(WallType.HARD);
                    cells[i + 4][j + 4].changeType(WallType.HARD);
                }
                else{
                    cells[i][j].changeType(WallType.HARD);
                    cells[i - 1][j + 1].changeType(WallType.HARD);
                    cells[i - 2][j + 2].changeType(WallType.HARD);
                    cells[i - 3][j + 3].changeType(WallType.HARD);
                    cells[i - 4][j + 4].changeType(WallType.HARD);
                }


            }
        }

        for (int i = 5; i < Size_X - 10; i+=10) {
            for (int j = 7; j < Size_Y - 10; j+=16) {
                if(MathUtils.random() < 0.3){
                    cells[i][j].changeType(WallType.SOFT);
                    cells[i][j + 1].changeType(WallType.SOFT);
                    cells[i][j + 2].changeType(WallType.SOFT);
                    cells[i][j + 3].changeType(WallType.SOFT);
                    cells[i][j + 4].changeType(WallType.SOFT);
                    cells[i - 1][j].changeType(WallType.SOFT);
                    cells[i - 2][j].changeType(WallType.SOFT);
                    cells[i - 3][j].changeType(WallType.SOFT);
                    cells[i - 1][j + 4].changeType(WallType.SOFT);
                    cells[i - 2][j + 4].changeType(WallType.SOFT);
                    cells[i - 3][j + 4].changeType(WallType.SOFT);
                }

            }
        }

        for (int i = 10; i < Size_X - 30; i+=27) {
            if(MathUtils.random() < 0.5){
                cells[i - 3][22].changeType(WallType.WATER);
                cells[i - 2][21].changeType(WallType.WATER);
                cells[i - 1][20].changeType(WallType.WATER);
                cells[i][19].changeType(WallType.WATER);
                cells[i][18].changeType(WallType.WATER);
                cells[i - 1][17].changeType(WallType.WATER);
                cells[i - 2][16].changeType(WallType.WATER);
                cells[i - 3][15].changeType(WallType.WATER);
            }
        }

        for (int i = 20; i < 24; i++) {
            for (int j = 1; j < Size_Y - 1; j++) {
                cells[i][j].changeType(WallType.WATER);
            }
        }
        for (int i = 20; i < 24; i++) {
            for (int j = 15; j < 21; j++) {
                cells[i][j].changeType(WallType.NONE);
            }
        }

        for (int i = 100; i < 104; i++) {
            for (int j = 1; j < Size_Y - 1; j++) {
                cells[i][j].changeType(WallType.INDESTUCTIBLE);
            }
        }
    }

    public void checkWallsBulletsCollision(Bullet bullet){
        int cx = (int) (bullet.getPosition().x / Cell_size);
        int cy = (int) (bullet.getPosition().y / Cell_size);

        if(cx >= 0 && cy >= 0 && cx <= Size_X && cy <= Size_Y){
            if(!cells[cx][cy].type.isProjectilePassable()){
                cells[cx][cy].damage();
                bullet.deactivate();
            }
        }
    }

    public void update(Vector2 position){
        if(position.x > 98 * Cell_size && position.y > 2 * Cell_size && position.y < 4 * Cell_size)
            for (int i = 100; i < 104; i++) {
                for (int j = 0; j < Size_Y; j++) {
                    if (Size_Y / 2 == j) {
                        cells[i][j - 3].changeType(WallType.NONE);
                        cells[i][j - 2].changeType(WallType.NONE);
                        cells[i][j - 1].changeType(WallType.NONE);
                        cells[i][j].changeType(WallType.NONE);
                        cells[i][j + 1].changeType(WallType.NONE);
                        cells[i][j + 2].changeType(WallType.NONE);
                        cells[i][j + 3].changeType(WallType.NONE);
                    }
                }
            }
    }

    public boolean isAreaClear(float x, float y, float halfSize){
        int leftX = (int) ((x - halfSize) / Cell_size);
        int rightX = (int) ((x + halfSize) / Cell_size);
        int bottomY = (int) ((y - halfSize) / Cell_size);
        int topY = (int) ((y + halfSize) / Cell_size);

        if(leftX <= 0)
            leftX = 0;
        if(bottomY <= 0)
            bottomY = 0;
        if(rightX >= Size_X)
            rightX = Size_X  + 1;
        if(topY >= Size_Y)
            topY = Size_Y + 1;
        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if(!cells[i][j].type.isUnitPassable())
                    return false;
            }
        }

        return true;
    }

    public void render(SpriteBatch batch){

        for (int i = 0; i < Size_X * Cell_size/ 40; i++) {
            for (int j = 0; j < Size_Y * Cell_size/ 40; j++) {
                batch.draw(texture, i * 40, j * 40);
            }
        }
        batch.draw(this.button, 98 * Cell_size, 2 * Cell_size);
        for (int i = 0; i < Size_X; i++) {
            for (int j = 0; j < Size_Y; j++) {
                if(cells[i][j].type != WallType.NONE && cells[i][j].type != WallType.BASE)
                    batch.draw(wallsTexture[cells[i][j].type.getIndex()][cells[i][j].hp - 1], i * Cell_size, j * Cell_size);
            }
        }
    }
}
