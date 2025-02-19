package bluedazzled.lucy_atmos.atmospherics.sim;

import bluedazzled.lucy_atmos.atmospherics.AtmosTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;

import static bluedazzled.lucy_atmos.atmospherics.ChunkTileList.getChunkActiveList;

//uh archived for now 'cause i'm preeeettyy fucking sure this is literally just for the air subsystem, which is run at startup, which we uh y'know don't need since we're updating constantly
public class air {
    //Deepest indentation of the reference file's function: 5
    //Our deepest indentation: tbd
    //Thanks DM for being an objectively easier language at times.
    public void setupTiles(LevelChunk chunk) {
        List<BlockPos> activeTiles = getChunkActiveList(chunk);
        /// Now we're gonna compare for differences
        /// Taking advantage of current cycle being set to negative before this run to do A->B B->A prevention
        for (BlockPos potential_diffPos  : activeTiles) { //You know, having both chunk list types use AtmosTileEntity would've been nice. Too bad Minecraft refuses to serialize it!
            AtmosTileEntity potential_diff = (AtmosTileEntity) chunk.getBlockEntity(potential_diffPos); //Cast our Pos to a Tile
            /// I can't use 0 here, so we're gonna do this instead. If it ever breaks I'll eat my shoe
            potential_diff.current_cycle = 1;
            for (Direction direction : Direction.values()){//Loops through all 6 directions to get potential enemies for enemy_tile
                if (potential_diff.doesAdjTileExist(direction)){ //Makes sure our enemy is a valid tile
                    AtmosTileEntity enemy_tile = (AtmosTileEntity) chunk.getBlockEntity(potential_diff.getAdjTilePos(direction)); //Cast our enemy_tile from the checked adjacent tile
                    /// If it's already been processed, then it's already talked to us
                    if (enemy_tile.current_cycle != -1) {
                        break;
                    }
                    //
                }
            }
        }
    }
}