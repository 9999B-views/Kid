package vn.devpro.devprokidorigin.models.xephinh;

import java.util.HashMap;

public class PuzzleData {
    PuzzleData instance = null;
    private PuzzleData(){}
    public PuzzleData getInstance(){
        if (instance != null){
            return instance;
        }else {
            instance = new PuzzleData();
            return instance;
        }
    }

    public  static HashMap<Integer,String> imgnames = new HashMap<>();
    public  static HashMap<String,Integer> bgrColors = new HashMap<>();
    static {
        imgnames.put(1,"puzzle_01");
        imgnames.put(2,"puzzle_02");
        imgnames.put(3,"puzzle_03");
        imgnames.put(4,"puzzle_04");
        imgnames.put(5,"puzzle_05");
        bgrColors.put("puzzle_01",0xffffcccc);
        bgrColors.put("puzzle_02",0xffbbfdff);
        bgrColors.put("puzzle_03",0xff1f1f54);
        bgrColors.put("puzzle_04",0xffffe655);
        bgrColors.put("puzzle_05",0xffffd9ab);
    }
}
