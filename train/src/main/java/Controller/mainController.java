/*
 * package Controller;
 * 
 * import java.util.ArrayList; import java.util.HashMap; import
 * java.util.Iterator; import java.util.List; import java.util.Map;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody;
 * 
 * 
 * @Controller public class mainController {
 * 
 * private static final Map<String, List<Ticket>> sectionTicketsMap = new
 * HashMap<>();
 * 
 * @PostMapping("/purchase") public String submitPurchase(@RequestBody
 * TicketRequest request) { String section = request.getSection(); int
 * seatNumber = allocateSeat(section); Ticket ticket = new
 * Ticket(request.getFrom(), request.getTo(), request.getUser(),
 * request.getPricePaid(), seatNumber, section);
 * 
 * // Store ticket details by section sectionTicketsMap.computeIfAbsent(section,
 * k -> new ArrayList<>()).add(ticket);
 * 
 * return ticket.generateReceipt(); }
 * 
 * @GetMapping("/receipt/{email}") public String getReceiptDetails(@PathVariable
 * String email) { for (List<Ticket> tickets : sectionTicketsMap.values()) { for
 * (Ticket ticket : tickets) { if (ticket.getUser().getEmail().equals(email)) {
 * return ticket.generateReceipt(); } } } return
 * "Receipt not found for user with email: " + email; }
 * 
 * @GetMapping("/users/{section}") public List<UserSeatDetails>
 * getUsersAndSeats(@PathVariable String section) { List<UserSeatDetails>
 * userSeatDetailsList = new ArrayList<>(); List<Ticket> tickets =
 * sectionTicketsMap.get(section); if (tickets != null) { for (Ticket ticket :
 * tickets) { userSeatDetailsList.add(new UserSeatDetails(ticket.getUser(),
 * ticket.getSeatNumber())); } } return userSeatDetailsList; }
 * 
 * @DeleteMapping("/remove/{email}") public String
 * removeUserFromTrain(@PathVariable String email) { for (List<Ticket> tickets :
 * sectionTicketsMap.values()) { Iterator<Ticket> iterator = tickets.iterator();
 * while (iterator.hasNext()) { Ticket ticket = iterator.next(); if
 * (ticket.getUser().getEmail().equals(email)) { iterator.remove(); return
 * "User with email " + email + " has been removed from the train."; } } }
 * return "User with email " + email + " not found in any section.";
 * 
 * } }
 */