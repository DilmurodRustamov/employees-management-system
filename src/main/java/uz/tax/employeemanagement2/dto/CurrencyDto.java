package uz.tax.employeemanagement2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDto {

    @JsonProperty("code")
    private String Code;

    @JsonProperty("ccy")
    private String Ccy;

    @JsonProperty("ccy_nm_ru")
    private String CcyNm_RU;

    @JsonProperty("ccy_nm_uz")
    private String CcyNm_UZ;

    @JsonProperty("ccy_nm_uzc")
    private String CcyNm_UZC;

    @JsonProperty("ccy_nm_en")
    private String CcyNm_EN;

    @JsonProperty("nominal")
    private String Nominal;

    @JsonProperty("rate")
    private String Rate;

    @JsonProperty("diff")
    private String Diff;

    @JsonProperty("date")
    private String Date;

}
