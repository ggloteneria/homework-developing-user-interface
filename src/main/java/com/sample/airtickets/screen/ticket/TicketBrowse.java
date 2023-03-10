package com.sample.airtickets.screen.ticket;

import io.jmix.core.common.util.Preconditions;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlIdSerializer;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import com.sample.airtickets.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route("tickets")
@UiController("Ticket.browse")
@UiDescriptor("ticket-browse.xml")
@LookupComponent("ticketsTable")
public class TicketBrowse extends StandardLookup<Ticket> {
    @Autowired
    private UrlRouting urlRouting;

    private UUID ticketId;


    public void setTicketId(UUID ticketId) {
        Preconditions.checkNotNullArgument(ticketId);

        this.ticketId = ticketId;
    }
}