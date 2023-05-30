package ma.emsi.cinema.cinema.Web;
import jakarta.transaction.Transactional;
import lombok.Data;
import ma.emsi.cinema.cinema.dao.FilmRepository;
import ma.emsi.cinema.cinema.dao.TicketRepository;
import ma.emsi.cinema.cinema.entities.Film;
import ma.emsi.cinema.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @GetMapping(path="/imageFilm/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable (name = "id")Long id) throws IOException {
        Film f= filmRepository.findById(id).get();
        String photoName=f.getPhoto();
        File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
   }

   @PostMapping("/payerTickets")
   @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm) {
        List<Ticket> ticketList = new ArrayList<>();
        ticketForm.getTickets().forEach(id -> {
            Ticket ticket = ticketRepository.findById(id).get();
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setReserve(true);
            ticket.setCodePayement(ticketForm.getCodePayement());
            ticketRepository.save(ticket);
            ticketList.add(ticket);
        });
        return ticketList;
    }
}

@Data
class TicketForm{
    private String nomClient;
    private int codePayement;
    private List<Long> tickets = new ArrayList<>();
}