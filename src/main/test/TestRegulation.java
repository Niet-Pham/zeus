import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class TestRegulation {

    @Data
    @Accessors(chain = true)
    class Asset {
        private int id;
        private int regulationId;
        private long regulationTime;
        private long extraTime;
    }

    @Test
    public void name() {
        List<Asset> a = new ArrayList<>();
        a.add(new Asset()
                .setId(2)
                .setRegulationId(6)
                .setRegulationTime(10)
                .setExtraTime(1));
        a.add(new Asset()
                .setId(1)
                .setRegulationId(6)
                .setRegulationTime(10)
                .setExtraTime(2));
        a.add(new Asset()
                .setId(3)
                .setRegulationId(7)
                .setRegulationTime(11)
                .setExtraTime(9));

        a.add(new Asset()
                .setId(4)
                .setRegulationId(9)
                .setRegulationTime(12)
                .setExtraTime(5));


        Collections.sort(a, Comparator.comparing(Asset::getId)
                .thenComparing(Asset::getRegulationId));

        List<Asset> b = new ArrayList<>();
        for(int i=0; i< a.size();i++){
            for(int j = i+1;j<a.size();j++){

                Asset a1 = a.get(i);
                Asset a2 = a.get(j);
                log.info("Duyệt vòng lặp 2 của asset_id thứ i = {} với regulation_id {}, asset_id {} - regulation_id {}", a1.getId(), a1.getRegulationId(), a2.getId(), a2.getRegulationId());
                if(a1.getRegulationId() == a2.getRegulationId()){
                    a2.setRegulationTime(a1.extraTime+a1.getRegulationTime());
                    b.add(a2);
                }
                else{
                    break;
                }
            }
        }

        b.forEach(System.out::println);
    }

    public static void main(String[] args) {
        long maxPriceBargain = 10000000000000L;
        System.out.println(String.format("%.2f", (double)maxPriceBargain));
    }
}
