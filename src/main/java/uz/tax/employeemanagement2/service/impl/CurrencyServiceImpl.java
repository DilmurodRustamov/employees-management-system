package uz.tax.employeemanagement2.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.tax.employeemanagement2.dto.CurrencyDto;
import uz.tax.employeemanagement2.entity.Currency;
import uz.tax.employeemanagement2.repository.CurrencyRepository;
import uz.tax.employeemanagement2.service.CurrencyService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    RestTemplate restTemplate = new RestTemplate();
    private String URI = "http://cbu.uz/uz/arkhiv-kursov-valyut/json/all/";

    @Override
    public DataTablesOutput<Currency> getAll(DataTablesInput input) throws ParseException {
        return currencyRepository.findAll(input);
    }

    @Override
    public void saveCurrencyByDate() throws ParseException {
        List<Currency> currencyEntities = new ArrayList<>();
        LocalDate now = LocalDate.now();
        Date realTime = new SimpleDateFormat("yyyy-MM-dd").parse(now.toString());
        System.out.println("realTime: " + realTime);
        boolean existRealTime = currencyRepository.existsAllByRealTime(realTime);
        String forObject = restTemplate.getForObject(URI + now + "/", String.class);
        if (!existRealTime) {
            for (int i = 0; i <= 74; i++) {
                Gson g = new Gson();
                CurrencyDto[] currencies = g.fromJson(forObject, CurrencyDto[].class);
                Currency currency = new Currency();
                currency.setCode(currencies[i].getCode());
                currency.setCcy(currencies[i].getCcy());
                currency.setCcyNm_RU(currencies[i].getCcyNm_RU());
                currency.setCcyNm_UZ(currencies[i].getCcyNm_UZ());
                currency.setCcyNm_UZC(currencies[i].getCcyNm_UZC());
                currency.setCcyNm_EN(currencies[i].getCcyNm_EN());
                currency.setNominal(currencies[i].getNominal());
                currency.setRate(currencies[i].getRate());
                currency.setDiff(currencies[i].getDiff());
                currency.setDate(currencies[i].getDate());
                currency.setRealTime(realTime);
                currencyEntities.add(currency);
                currencyRepository.saveAll(currencyEntities);
            }
        }
    }

    //This method checks every 5 seconds because exchange rates change very quickly
    @Scheduled(initialDelay = 2000L, fixedDelay = 60000L)
    public void exchangeRateCheck() throws ParseException {
        saveCurrencyByDate();
    }

//    @Scheduled(cron = "1 1 0 * * *")
//    public void startCron() throws ParseException {
//        saveCurrencyByDate();
//    }

}
