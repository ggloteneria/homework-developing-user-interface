package com.sample.airtickets.screen.flight;

import com.sample.airtickets.entity.Airline;
import io.jmix.core.DataManager;
import io.jmix.ui.screen.*;
import com.sample.airtickets.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@UiController("Flight.edit")
@UiDescriptor("flight-edit.xml")
@EditedEntityContainer("flightDc")
public class FlightEdit extends StandardEditor<Flight> {
    @Autowired
    private DataManager dataManager;

    @Install(to = "airlineField", subject = "searchExecutor")
    private List<Airline> airlineFieldSearchExecutor(String searchString, Map<String, Object> searchParams) {
        return dataManager.load(Airline.class)
                .query("e.name like ?1 order by e.name", "(?i)%" + searchString + "%")
                .list();
    }
}