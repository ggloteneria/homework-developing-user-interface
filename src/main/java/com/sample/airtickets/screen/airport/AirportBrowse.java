package com.sample.airtickets.screen.airport;

import io.jmix.ui.Dialogs;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.ScreenFacet;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.sample.airtickets.entity.Airport;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Airport.browse")
@UiDescriptor("airport-browse.xml")
@LookupComponent("airportsTable")
public class AirportBrowse extends StandardLookup<Airport> {



}