package com.starwars.desafiob2w;

import com.starwars.desafiob2w.controllers.PlanetController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class Desafiob2wApplicationTests {

    @Autowired
    private PlanetController controller;

    @Test
    //sanity check
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
