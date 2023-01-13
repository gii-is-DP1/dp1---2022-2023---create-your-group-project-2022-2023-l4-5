package org.springframework.samples.nt4h.statistic;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.nt4h.exceptions.NotFoundException;
import org.springframework.samples.nt4h.message.Advise;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticServiceTest {
    @Autowired
    StatisticService statisticService;
    private Integer idStatistic;

    @MockBean
    private Advise advise;

    @BeforeEach
    void setUp() {
        Statistic statistic= Statistic.createStatistic();
        statisticService.saveStatistic(statistic);
        idStatistic = statistic.getId();
    }

    @AfterEach
    void tearDown() {
        statisticService.deleteStatisticById(idStatistic);
    }


    @Test
    public void findByIdTrue(){
        Statistic stat = statisticService.getStatisticById(idStatistic);
        assertNotNull(stat);
        assertEquals(0, stat.getDamageDealt());
    }

    @Test
    public void findAll(){
        assertEquals(11,statisticService.getAllStatistics().size());
    }

    @Test
    public void shouldUpdateStatistic(){
        Statistic stat = statisticService.getStatisticById(idStatistic);
        Integer newGold= stat.getGold()+1;
        stat.setGold(newGold);
        statisticService.saveStatistic(stat);
        assertEquals(newGold,statisticService.getStatisticById(idStatistic).getGold());
    }


}
