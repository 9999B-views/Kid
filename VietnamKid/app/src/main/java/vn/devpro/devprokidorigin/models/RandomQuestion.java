package vn.devpro.devprokidorigin.models;

import java.util.Random;

/**
 * Created by NamQuoc on 3/9/2018.
 */

public class RandomQuestion {
    private int soA;
    private int soB;
    private String phepTinh;
    private int dapAn;

    public RandomQuestion(int max, int min) {
        Random r = new Random();
        soA = r.nextInt(max - min + 1) + 1;
        soB = r.nextInt(max - min + 1) + 1;

        if (soA < soB) {
            dapAn = soA + soB;
            phepTinh = "+";
        } else {
            int phepTinh1 = r.nextInt(2 - 1 + 1) + 1;
            if (phepTinh1 == 1) {
                dapAn = soA + soB;
                phepTinh = "+";
            } else {
                dapAn = soA - soB;
                phepTinh = "-";
            }
        }

    }

    public int getSoA() {
        return soA;
    }

    public int getSoB() {
        return soB;
    }

    public String getPhepTinh() {
        return phepTinh;
    }

    public int getDapAn() {
        return dapAn;
    }
}
