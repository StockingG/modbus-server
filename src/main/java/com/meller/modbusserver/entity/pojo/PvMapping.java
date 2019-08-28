package com.meller.modbusserver.entity.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenleijun
 * @Date 2018/7/9
 */
@Data
public class PvMapping {
    private String sn;

    @JsonProperty(value = "device_code")
    private String deviceCode;

    @JsonProperty(value = "plant_code")
    private String plantCode;

    @JsonProperty(value = "data_source_mapping_id")
    private String dataSourceMappingId;
}
