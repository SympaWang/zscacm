package com.example.zscacm.entity;

import com.google.common.hash.HashCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CfProblemIds implements Serializable {

    private Integer firstId;

    private Integer secondId;

    private Integer thirdId;

    @Override
    public int hashCode() {
        int h = 0;
        h = 31 * h + firstId;
        h = 31 * h + secondId;
        h = 31 * h + thirdId;
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CfProblemIds)) {
            return false;
        }
        CfProblemIds ids = (CfProblemIds) obj;
        if(this == ids) {
            return true;
        }

        if(ids.firstId.equals(this.getFirstId()) && ids.secondId.equals(this.getSecondId()) && ids.thirdId.equals(this.getThirdId())) {
            return true;
        } else {
            return false;
        }
    }


}
