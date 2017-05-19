package com.example.android.quakereport;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.JoiningGroup.E;
import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Leo on 2017-5-17.
 */

public class DataUtil {

    public static ArrayList<EarthQuake> getData(){
        ArrayList<EarthQuake> retList = new ArrayList<EarthQuake>();
        retList.add(new EarthQuake(3.23,"88km N of Yelizovo, Russia",1454124312220L,"http://www.baidu.com"));
        retList.add(new EarthQuake(6.113,"94km SSE of Taron, Papua New Guinea",1453777820750L,"http://www.baidu.com"));
        retList.add(new EarthQuake(6.345,"50km NNE of Al Hoceima, Morocco",1453695722730L,"http://www.baidu.com"));
        retList.add(new EarthQuake(7.13,"86km E of Old Iliamna, Alaska",1453631430230L,"http://www.baidu.com"));
        retList.add(new EarthQuake(9.08,"Pacific-Antarctic Ridge",1451986454620L,"http://www.baidu.com"));
        return retList;
    }

}
