package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jhe.question_bank.repository.resultSet.GetUnitGradingResultSet;

import lombok.Getter;

@Getter
public class UnitGradingResultVO {
    private Integer unitId;
    private String unitName;
    private Integer correctCount;
    private Boolean passed;

    private static final Map<Integer, Integer> UNIT_CUTLINE_MAP = Map.of(
        1, 4,
        2, 4,
        3, 12
    );

    private UnitGradingResultVO(GetUnitGradingResultSet resultSet, Boolean passed) {
        this.unitId = resultSet.getUnitId();
        this.unitName = resultSet.getUnitName();
        this.correctCount = resultSet.getCorrectCount();
        this.passed = passed;
    }

    public static List<UnitGradingResultVO> getList(List<GetUnitGradingResultSet> resultSets) {
        List<UnitGradingResultVO> list = new ArrayList<>();

        for (GetUnitGradingResultSet resultSet: resultSets) {
            Integer unitId = resultSet.getUnitId();
            Integer correctCount = resultSet.getCorrectCount();
            
            Integer cutline = UNIT_CUTLINE_MAP.get(unitId);
            boolean passed = correctCount >= cutline;

            UnitGradingResultVO vo = new UnitGradingResultVO(resultSet, passed);
            list.add(vo);
        }

        return list;
    }

}
