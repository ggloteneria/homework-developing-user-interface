package com.sample.airtickets.screen.airline;

import io.jmix.ui.UiComponents;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import com.sample.airtickets.entity.Airline;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Airline.browse")
@UiDescriptor("airline-browse.xml")
@LookupComponent("table")
public class AirlineBrowse extends MasterDetailScreen<Airline> {
    @Autowired
    private TextField<String> iataCodeField;
    @Autowired
    private CollectionContainer<Airline> airlinesDc;
    @Autowired
    private UiComponents uiComponents;

    @Subscribe
    public void onInit(InitEvent event) {
//        iataCodeField.setValue(iataCodeField.getRawValue().toUpperCase());
//        airlinesDc.getItem().setIataCode(iataCodeField.getRawValue().toUpperCase());
    }


    @Install(to = "iataCodeField", subject = "formatter")
    private String iataCodeFieldFormatter(String value) {
        return null;
    }
}