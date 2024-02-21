package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * import org.springframework.boot.SpringApplication; import
 * org.springframework.boot.autoconfigure.SpringBootApplication;
 * 
 * @SpringBootApplication public class train { public static void main(String[]
 * args) { SpringApplication.run(train.class, args); }
 * 
 * }
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class train {

    private static final Map<String, List<Ticket>> sectionTicketsMap = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(train.class, args);
    }

    @PostMapping("/purchase")
    public String submitPurchase(@RequestBody TicketRequest request) {
        String section = request.getSection();
        int seatNumber = allocateSeat(section);
        Ticket ticket = new Ticket(request.getFrom(), request.getTo(), request.getUser(), request.getPricePaid(), seatNumber, section);

        // Store ticket details by section
        sectionTicketsMap.computeIfAbsent(section, k -> new ArrayList<>()).add(ticket);

        return ticket.generateReceipt();
    }

    @GetMapping("/receipt/{email}")
    public String getReceiptDetails(@PathVariable String email) {
        for (List<Ticket> tickets : sectionTicketsMap.values()) {
            for (Ticket ticket : tickets) {
                if (ticket.getUser().getEmail().equals(email)) {
                    return ticket.generateReceipt();
                }
            }
        }
        return "Receipt not found for user with email: " + email;
    }

    @GetMapping("/users/{section}")
    public List<UserSeatDetails> getUsersAndSeats(@PathVariable String section) {
        List<UserSeatDetails> userSeatDetailsList = new ArrayList<>();
        List<Ticket> tickets = sectionTicketsMap.get(section);
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                userSeatDetailsList.add(new UserSeatDetails(ticket.getUser(), ticket.getSeatNumber()));
            }
        }
        return userSeatDetailsList;
    }

    @DeleteMapping("/remove/{email}")
    public String removeUserFromTrain(@PathVariable String email) {
        for (List<Ticket> tickets : sectionTicketsMap.values()) {
            Iterator<Ticket> iterator = tickets.iterator();
            while (iterator.hasNext()) {
                Ticket ticket = iterator.next();
                if (ticket.getUser().getEmail().equals(email)) {
                    iterator.remove();
                    return "User with email " + email + " has been removed from the train.";
                }
            }
        }
        return "User with email " + email + " not found in any section.";
    }

    @PutMapping("/modify/{email}")
    public String modifyUserSeat(@PathVariable String email, @RequestBody SeatModificationRequest request) {
        for (List<Ticket> tickets : sectionTicketsMap.values()) {
            for (Ticket ticket : tickets) {
                if (ticket.getUser().getEmail().equals(email)) {
                    ticket.setSeatNumber(request.getSeatNumber());
                    return "Seat modified successfully for user with email: " + email;
                }
            }
        }
        return "User with email " + email + " not found in any section.";
    }

    private synchronized int allocateSeat(String section) {
        if (!sectionTicketsMap.containsKey(section)) {
            sectionTicketsMap.put(section, new ArrayList<>());
        }
        List<Ticket> tickets = sectionTicketsMap.get(section);
        int nextAvailableSeat = tickets.size() + 1;
        return nextAvailableSeat;
    }
}

class Ticket {
    private String from;
    private String to;
    private User user;
    private double pricePaid;
    private int seatNumber;
    private String section;

    // Constructor, getters, setters, and generateReceipt method omitted for brevity

    public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getPricePaid() {
		return pricePaid;
	}

	public void setPricePaid(double pricePaid) {
		this.pricePaid = pricePaid;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	// Constructor
    public Ticket(String from, String to, User user, double pricePaid, int seatNumber, String section) {
        this.from = from;
        this.to = to;
        this.user = user;
        this.pricePaid = pricePaid;
        this.seatNumber = seatNumber;
        this.section = section;
    }

    // Method to generate receipt
    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt:\n");
        receipt.append("From: ").append(from).append("\n");
        receipt.append("To: ").append(to).append("\n");
        receipt.append("User: ").append(user.getFullName()).append("\n");
        receipt.append("Email: ").append(user.getEmail()).append("\n");
        receipt.append("Price Paid: $").append(pricePaid).append("\n");
        receipt.append("Seat Number: ").append(section).append(seatNumber).append("\n");
        return receipt.toString();
    }

    // Getters and setters omitted for brevity
}

class User {
    private String firstName;
    private String lastName;
    private String email;

    // Constructor, getters, setters omitted for brevity

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Getters and setters
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }
}

class TicketRequest {
    private String from;
   
	private String to;
    private User user;
    private double pricePaid;
    private String section;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getPricePaid() {
		return pricePaid;
	}
	public void setPricePaid(double pricePaid) {
		this.pricePaid = pricePaid;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}

    // Getters and setters omitted for brevity
}

class SeatModificationRequest {
    private int seatNumber;

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	

    // Getters and setters omitted for brevity
}

class UserSeatDetails {
    private User user;
    private int seatNumber;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	
	//Constructor
	public UserSeatDetails(User user, int seatNumber) {
        this.user = user;
        this.seatNumber = seatNumber;
    }
}

    // Constructor, getters, setters omitted for brevity
