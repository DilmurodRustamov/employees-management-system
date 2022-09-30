package uz.tax.employeemanagement2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer currencyId;

    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "ccy")
    private String ccy;

    @Column(name = "ccy_nm_ru")
    private String ccyNm_RU;

    @Column(name = "ccy_nm_uz")
    private String ccyNm_UZ;

    @Column(name = "ccy_nm_uzc")
    private String ccyNm_UZC;

    @Column(name = "ccy_nm_en")
    private String ccyNm_EN;

    @Column(name = "nominal")
    private String nominal;

    @Column(name = "rate")
    private String rate;

    @Column(name = "diff")
    private String diff;

    @Column(name = "date")
    private String date;

    @Column(name = "real_time")
    private Date realTime;
}
