package vn.devpro.devprokidorigin.models.entity.game.latbai;

/**
 * Created by admin on 3/31/2018.
 */

public class BoardCauhinh {
    private  static final  int _4 = 4 ;
    private  static final  int _6 = 6 ;
    private  static final  int _8 = 8 ;
    private  static final  int _10 = 10 ;
    private  static final  int _12 = 12 ;
    private  static final  int _16 = 16 ;

    public final int difficulty;
    public final int numTiles;
    public final int numTilesInRow;
    public final int numRows;
    public final int time;

    public BoardCauhinh(int difficulty) {
        this.difficulty = difficulty;
      switch (difficulty)
      {
          case 1: numTiles =_4 ;
          numTilesInRow =  2 ;
          numRows = 2;
          time = 40 ;
          break;
          case 2: numTiles =_6 ;
              numTilesInRow =  3 ;
              numRows = 2;
              time =50 ;
              break;
          case 3: numTiles =_8 ;
              numTilesInRow =  4 ;
              numRows = 2;
              time =60 ;
              break;
          case 4: numTiles =_10 ;
              numTilesInRow =  5 ;
              numRows = 2;
              time =80 ;
              break;
          case 5: numTiles =_12 ;
              numTilesInRow =  4 ;
              numRows = 3;
              time =90 ;
              break;
          case 6: numTiles =_16 ;
              numTilesInRow =  4 ;
              numRows = 4;
              time =100 ;
              break;
          default:
//               chọn 1 trong các kisck thước đã được lựa chọn
//               SET THỜI GIAN , HÀNG  CỘT ,
//               được chọn độ khó => đề bài thay đổi độ khó , đề nghị đổi lại
              throw new IllegalArgumentException("Select one of predefined sizes");
      }
    }
}
