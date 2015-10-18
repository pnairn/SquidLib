package squidpony.squidmath;

import org.junit.Test;
import squidpony.squidgrid.FOVCache;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.styled.TilesetType;

import static org.junit.Assert.*;

/**
 * Created by Tommy Ettinger on 10/8/2015.
 */
public class FOVCacheTest {
    @Test
    public void testCache()
    {
        int width = 60;
        int height = 60;
        for (long r = 0, seed = 0xCAB; r < 10; r++, seed ^= seed << 2) {
            StatefulRNG rng = new StatefulRNG(new LightRNG(seed));
            DungeonGenerator dungeonGenerator = new DungeonGenerator(width, height, rng);
            dungeonGenerator.addDoors(15, true);
            dungeonGenerator.addWater(25);
            //dungeonGenerator.addTraps(2);
            char[][] map = DungeonUtility.closeDoors(dungeonGenerator.generate(TilesetType.DEFAULT_DUNGEON));

            FOVCache cache = new FOVCache(map, 10, Radius.CIRCLE, 8);
            Coord walkable = dungeonGenerator.utility.randomFloor(map);
            //byte[][] seen = cache.slopeShadowFOV(walkable.x, walkable.y);
            //byte[][] gradient = CoordPacker.unpackMultiByte(cache.getCacheEntry(walkable.x, walkable.y), width, height);
            /*
            System.out.println("From the walkable square, outside range of the problem");
            for (int k = 0; k < height; k++) {
                for (int l = 0; l < width; l++) {
                    if((l == walkable.x && k == walkable.y))
                        System.out.print('@');
                    else if (gradient[l][k] > 0)
                        System.out.print((char) (gradient[l][k] + 65));
                    else
                        System.out.print(' ');
                    System.out.print(map[l][k]);
                }
                System.out.println();
            }
            System.out.println("Now from the one that couldn't see the distant square");
            byte[][] gradient2 = CoordPacker.unpackMultiByte(cache.getCacheEntry(44, 50), width, height);
            for (int k = 0; k < height; k++) {
                for (int l = 0; l < width; l++) {
                    if((l == 44 && k == 50))
                        System.out.print('@');
                    else if (gradient2[l][k] > 0)
                        System.out.print((char) (gradient2[l][k] + 65));
                    else
                        System.out.print(' ');
                    System.out.print(map[l][k]);
                }
                System.out.println();
            }

            for (int i = 0; i < seen.length; i++) {
                assertArrayEquals(seen[i], gradient[i]);
            }
            */
            cache.awaitCache();
            boolean[][] mutual = CoordPacker.unpack(cache.getCacheEntry(walkable.x, walkable.y)[0], width, height);
            for (int i = 0; i < mutual.length; i++) {
                for (int j = 0; j < mutual[i].length; j++) {
                    if(mutual[i][j] && map[i][j] != '#')
                    {
                        boolean sharing = cache.queryCache(10, i, j, walkable.x, walkable.y);
                        if(!sharing) {
                            /*
                            short[][] cacheIJ = cache.getCacheEntry(i, j);

                            for (int k = 0; k <cacheIJ.length; k++) {
                                System.out.println("Cache sub-array " + k);
                                for (int l = 0; l < cacheIJ[k].length; l++) {
                                    System.out.print(cacheIJ[k][l] + ", ");
                                }
                                System.out.println();
                            }
                            boolean[][] fromIJ = CoordPacker.unpack(cacheIJ[0], width, height);
                            for (int k = 0; k < height; k++) {
                                for (int l = 0; l < width; l++) {
                                    System.out.print(((l == i && k == j) || (l == walkable.x && k == walkable.y)
                                            ? "@"
                                            : (fromIJ[l][k] ? "!" : " ")) + map[l][k]);
                                }
                                System.out.println();
                            }
                            byte[][] grad = CoordPacker.unpackMultiByte(cacheIJ, width, height);
                            for (int k = 0; k < height; k++) {
                                for (int l = 0; l < width; l++) {
                                    if((l == i && k == j) || (l == walkable.x && k == walkable.y))
                                        System.out.print('@');
                                    else if (grad[l][k] > 0)
                                        System.out.print((char) (grad[l][k] + 65));
                                    else
                                        System.out.print(' ');
                                    System.out.print(map[l][k]);
                                }
                                System.out.println();
                            }*/
                            System.out.println("i: " + i + ", j: " + j + ", x: " + walkable.x + ", y: " + walkable.y);
                        }
                        assertTrue(sharing);
                    }
                }
            }



        }
    }
}
