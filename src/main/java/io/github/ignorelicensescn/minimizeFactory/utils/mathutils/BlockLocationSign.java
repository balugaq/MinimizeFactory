package io.github.ignorelicensescn.minimizeFactory.utils.mathutils;

public enum BlockLocationSign {

    NEGATIVE_NEGATIVE_NEGATIVE(-1,-1,-1),
    NEGATIVE_NEGATIVE_ZERO(-1,-1,0),
    NEGATIVE_NEGATIVE_POSITIVE(-1,-1,1),
    NEGATIVE_ZERO_NEGATIVE(-1,0,-1),
    NEGATIVE_ZERO_ZERO(-1,0,0),
    NEGATIVE_ZERO_POSITIVE(-1,0,1),
    NEGATIVE_POSITIVE_NEGATIVE(-1,1,-1),
    NEGATIVE_POSITIVE_ZERO(-1,1,0),
    NEGATIVE_POSITIVE_POSITIVE(-1,1,1),
    ZERO_NEGATIVE_NEGATIVE(0,-1,-1),
    ZERO_NEGATIVE_ZERO(0,-1,0),
    ZERO_NEGATIVE_POSITIVE(0,-1,1),
    ZERO_ZERO_NEGATIVE(0,0,-1),
    ZERO_ZERO_ZERO(0,0,0),
    ZERO_ZERO_POSITIVE(0,0,1),
    ZERO_POSITIVE_NEGATIVE(0,1,-1),
    ZERO_POSITIVE_ZERO(0,1,0),
    ZERO_POSITIVE_POSITIVE(0,1,1),
    POSITIVE_NEGATIVE_NEGATIVE(1,-1,-1),
    POSITIVE_NEGATIVE_ZERO(1,-1,0),
    POSITIVE_NEGATIVE_POSITIVE(1,-1,1),
    POSITIVE_ZERO_NEGATIVE(1,0,-1),
    POSITIVE_ZERO_ZERO(1,0,0),
    POSITIVE_ZERO_POSITIVE(1,0,1),
    POSITIVE_POSITIVE_NEGATIVE(1,1,-1),
    POSITIVE_POSITIVE_ZERO(1,1,0),
    POSITIVE_POSITIVE_POSITIVE(1,1,1);
    public final int x;
    public final int y;
    public final int z;
    public final BlockLocation location;
    public final BlockLocation[] moveDirections;
    BlockLocationSign(int x, int y, int z) {
        this.x=x;
        this.y=y;
        this.z=z;
        location = new BlockLocation(x,y,z);
        if (x < 0){
            if (y < 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else if (y > 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else{
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
        }
        else if (x > 0){
            if (y < 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else if (y > 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else{
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
        }
        else{
            if (y < 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else if (y > 0){
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
            else{
                if (z < 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,-1),};
                }
                else if (z > 0){
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),};
                }
                else{
                    moveDirections = new BlockLocation[]{new BlockLocation(1,0,0),new BlockLocation(-1,0,0),new BlockLocation(0,1,0),new BlockLocation(0,-1,0),new BlockLocation(0,0,1),new BlockLocation(0,0,-1),};
                }
            }
        }
    }

    public BlockLocation toBlockLocation(){
        return location;
    }

    public BlockLocation[] getMoveDirections(){
        return moveDirections;
    }
    public static BlockLocationSign extractSign(long x,long y,long z){
        if (x < 0){
            if (y < 0){
                if (z < 0){return NEGATIVE_NEGATIVE_NEGATIVE;}
                else if (z > 0){return NEGATIVE_NEGATIVE_POSITIVE;}
                return NEGATIVE_NEGATIVE_ZERO;
            }
            else if (y > 0){
                if (z < 0){return NEGATIVE_POSITIVE_NEGATIVE;}
                else if (z > 0){return NEGATIVE_POSITIVE_POSITIVE;}
                return NEGATIVE_POSITIVE_ZERO;
            }
            else{
                if (z < 0){return NEGATIVE_ZERO_NEGATIVE;}
                else if (z > 0){return NEGATIVE_ZERO_POSITIVE;}
                return NEGATIVE_ZERO_ZERO;
            }
        }
        else if (x > 0){
            if (y < 0){
                if (z < 0){return POSITIVE_NEGATIVE_NEGATIVE;}
                else if (z > 0){return POSITIVE_NEGATIVE_POSITIVE;}
                return POSITIVE_NEGATIVE_ZERO;
            }
            else if (y > 0){
                if (z < 0){return POSITIVE_POSITIVE_NEGATIVE;}
                else if (z > 0){return POSITIVE_POSITIVE_POSITIVE;}
                return POSITIVE_POSITIVE_ZERO;
            }
            else{
                if (z < 0){return POSITIVE_ZERO_NEGATIVE;}
                else if (z > 0){return POSITIVE_ZERO_POSITIVE;}
                return POSITIVE_ZERO_ZERO;
            }
        }
        else{
            if (y < 0){
                if (z < 0){return ZERO_NEGATIVE_NEGATIVE;}
                else if (z > 0){return ZERO_NEGATIVE_POSITIVE;}
                return ZERO_NEGATIVE_ZERO;
            }
            else if (y > 0){
                if (z < 0){return ZERO_POSITIVE_NEGATIVE;}
                else if (z > 0){return ZERO_POSITIVE_POSITIVE;}
                return ZERO_POSITIVE_ZERO;
            }
            else{
                if (z < 0){return ZERO_ZERO_NEGATIVE;}
                else if (z > 0){return ZERO_ZERO_POSITIVE;}
                return ZERO_ZERO_ZERO;
            }
        }

    }
}
