package com.servlet.api;

import com.bean.HotelBean;
import com.dao.HotelDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/hotels")
public class ApiHotelsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HotelDao hotelDao;

    private static class HotelMetadata {
        final String category;
        final String description;
        final String locationOverview;
        final String[] images;
        final String[] amenities;

        HotelMetadata(String category, String description, String locationOverview, String[] images, String[] amenities) {
            this.category = category;
            this.description = description;
            this.locationOverview = locationOverview;
            this.images = images;
            this.amenities = amenities;
        }
    }

    private static final Map<Integer, HotelMetadata> METADATA = new HashMap<>();
    private static final HotelMetadata DEFAULT_METADATA;

    static {
        METADATA.put(1, new HotelMetadata(
            "city",
            "Located in the heart of the city with luxury amenities and scenic views.",
            "The Grand Palace Hotel is situated in the bustling city center, providing easy access to shopping, entertainment, and historical landmarks.",
            new String[]{"images/hotel1.jpg", "images/room1.jpg", "images/lobby1.jpg"},
            new String[]{"Free WiFi", "Swimming Pool", "Spa & Wellness", "Fine Dining", "Fitness Center", "24/7 Room Service"}
        ));

        METADATA.put(2, new HotelMetadata(
            "beach",
            "Enjoy your stay by the beach with relaxing sunbeds and beautiful sunsets.",
            "Situated in the prestigious luxury district, the hotel offers proximity to high-end shopping, fine dining, and exclusive nightlife.",
            new String[]{"images/hotel2.jpg", "images/room2.jpg", "images/dining2.jpg"},
            new String[]{"Private Beach", "Infinity Pool", "Ocean View Dining", "Water Sports", "Beach Bar", "Concierge Service"}
        ));

        METADATA.put(3, new HotelMetadata(
            "mountain",
            "Get away from it all and relax in the mountains surrounded by nature.",
            "Perched in serene mountain trails, surrounded by pine forests and fresh alpine breezes.",
            new String[]{"images/hotel3.jpg", "images/room3.jpg", "images/mountainview.jpg"},
            new String[]{"Fireplace Suites", "Mountain View", "Hiking Tours", "Sauna", "Organic Restaurant", "Heated Pool"}
        ));

        METADATA.put(4, new HotelMetadata(
            "mountain",
            "Perfect for business travelers looking for convenience and comfort in the city.",
            "Nestled in Alpine Valley with panoramic hill views and direct access to outdoor adventure spots.",
            new String[]{"images/hotel4.jpg", "images/room4.jpg", "images/skyview.jpg"},
            new String[]{"Free WiFi", "Business Lounge", "Breakfast Included", "Express Check-in", "Gym", "Free Parking"}
        ));

        METADATA.put(5, new HotelMetadata(
            "lake",
            "A peaceful retreat by the lake with stunning views and tranquil surroundings.",
            "Located on the crystal-clear shores of Lake Emerald, offering serene boat rides and dockside dinners.",
            new String[]{"images/hotel5.jpg", "images/room5.jpg", "images/sparoom.jpg"},
            new String[]{"Lake View", "Kayaking & Boating", "Full Spa", "Barbecue Area", "Sunset Deck", "Free Breakfast"}
        ));

        METADATA.put(6, new HotelMetadata(
            "luxury",
            "Experience royal treatment in our luxurious and elegant hotel.",
            "Situated on the prestigious Grand Boulevard near designer boutiques and historic opera houses.",
            new String[]{"images/hotel6.jpg", "images/room7.jpg", "images/dining2.jpg"},
            new String[]{"Personal Butler", "Michelin Star Dining", "Rooftop Infinity Pool", "Chauffeur Service", "Luxury Spa"}
        ));

        METADATA.put(7, new HotelMetadata(
            "city",
            "A modern hotel situated in the vibrant downtown area with easy access to attractions.",
            "In the pulse of Downtown Square, moments away from financial districts, theaters, and metro hubs.",
            new String[]{"images/hotel7.jpg", "images/room8.jpg", "images/gym.jpg"},
            new String[]{"City Skyline View", "Conference Rooms", "24/7 Gym", "Cocktail Bar", "Valet Parking"}
        ));

        METADATA.put(8, new HotelMetadata(
            "mountain",
            "Relax in our mountain-view hotel with access to hiking trails and scenic landscapes.",
            "Elevated high on Sunset Heights overlooking endless green valley horizons and golden twilights.",
            new String[]{"images/hotel8.jpg", "images/room9.jpg", "images/game.jpg"},
            new String[]{"Panoramics Deck", "Gaming Arcade", "Trekking Guides", "Fire Pit Lounge", "Open Air Pool"}
        ));

        METADATA.put(9, new HotelMetadata(
            "beach",
            "Enjoy breathtaking ocean views from every room and world-class dining options.",
            "Direct beachside access at Coral Cove with turquoise waters and white sand stretching for miles.",
            new String[]{"images/hotel9.jpg", "images/room10.jpg", "images/beach_view.jpg"},
            new String[]{"Private Balcony", "Seafood Grill", "Scuba Diving", "Beach Hammocks", "Sunset Cruises"}
        ));

        METADATA.put(10, new HotelMetadata(
            "luxury",
            "Stay in the epitome of luxury and sophistication, with exclusive services and amenities.",
            "Iconic flagship property located in the exclusive Royal Enclave, setting world benchmarks for elegance.",
            new String[]{"images/hotel10.jpg", "images/room1.jpg", "images/lobby1.jpg"},
            new String[]{"5-Star Luxury Service", "Private Plunge Pool", "Helipad Access", "Sommelier Wine Cellar", "Art Gallery"}
        ));

        METADATA.put(11, new HotelMetadata(
            "luxury",
            "Immerse yourself in royal Rajasthani grandeur with courtyard dining, marble pavilions, and regal suites.",
            "Nestled in the historic Pink City near Amber Fort and City Palace, offering authentic cultural immersion.",
            new String[]{"images/hotel6.jpg", "images/lobby1.jpg", "images/dining2.jpg"},
            new String[]{"Heritage Courtyard", "Royal Spa", "Folk Performances", "Rooftop Dining", "Swimming Pool", "Valet Parking"}
        ));

        METADATA.put(12, new HotelMetadata(
            "city",
            "Modern high-tech luxury retreat tailored for business pioneers and leisure travelers in India's tech capital.",
            "Centrally positioned in Bengaluru's prime business district with seamless connectivity to tech parks and upscale bistros.",
            new String[]{"images/hotel7.jpg", "images/room8.jpg", "images/skyview.jpg"},
            new String[]{"High-Speed Fiber WiFi", "Executive Club Lounge", "24/7 Smart Gym", "Infinity Lap Pool", "Fine Dining", "Conference Hub"}
        ));

        METADATA.put(13, new HotelMetadata(
            "lake",
            "Tranquil waterfront sanctuary nestled among emerald backwaters, swaying palms, and holistic Ayurvedic therapies.",
            "Set along the scenic waterways of Kumarakom and Alleppey, featuring private boat docks and serene sunset vistas.",
            new String[]{"images/hotel5.jpg", "images/sparoom.jpg", "images/beach_view.jpg"},
            new String[]{"Ayurvedic Wellness Spa", "Private Houseboat Tours", "Waterfront Dining", "Yoga Pavilion", "Organic Kitchen", "Infinity Pool"}
        ));

        METADATA.put(14, new HotelMetadata(
            "luxury",
            "Grand Rajput-styled retreat near the Aravalli hills featuring carved stone archways, tranquil gardens, and authentic royal hospitality.",
            "Set along the historic Amber corridor, offering sweeping views of rugged hills and easy access to ancient forts and markets.",
            new String[]{"images/hotel1.jpg", "images/mountainview.jpg", "images/dining2.jpg"},
            new String[]{"Heritage Garden Pool", "Royal Dining Hall", "Ayurvedic Spa", "Cultural Dance Evenings", "Free WiFi", "Valet Parking"}
        ));

        METADATA.put(15, new HotelMetadata(
            "city",
            "An eco-luxe urban oasis surrounded by lush canopies, combining sustainable architecture with cutting-edge business luxury.",
            "Ideally nestled near Lavelle Road and Cubbon Park, steps away from Bengaluru's premier dining avenues and financial centers.",
            new String[]{"images/hotel4.jpg", "images/room4.jpg", "images/gym.jpg"},
            new String[]{"Lush Green Atrium", "Executive Business Hub", "24/7 Fitness Center", "Rooftop Craft Lounge", "High-Speed Fiber", "Airport Shuttles"}
        ));

        METADATA.put(16, new HotelMetadata(
            "beach",
            "Coastal eco-sanctuary positioned where coconut groves meet the Arabian Sea, featuring coastal dining and open-air sea view pavilions.",
            "Prime beachside location along the pristine Malabar coastline with private beach access and scenic coastal cliff walking trails.",
            new String[]{"images/hotel2.jpg", "images/beach_view.jpg", "images/pool10.jpg"},
            new String[]{"Private Oceanfront Beach", "Seafood Specialty Restaurant", "Infinity Plunge Pool", "Sunrise Yoga Deck", "Ayurvedic Center", "Complimentary Breakfast"}
        ));

        DEFAULT_METADATA = new HotelMetadata(
            "city",
            "Experience world-class hospitality, comfortable accommodation, and convenient amenities.",
            "Conveniently situated with seamless access to key transit routes and attractions.",
            new String[]{"images/hotel1.jpg", "images/room1.jpg", "images/lobby1.jpg"},
            new String[]{"Free WiFi", "24/7 Room Service", "Air Conditioning", "Housekeeping"}
        );
    }

    @Override
    public void init() {
        hotelDao = new HotelDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String hotelIdParam = request.getParameter("id");
        if (hotelIdParam != null && !hotelIdParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(hotelIdParam.trim());
                HotelBean hotel = hotelDao.getHotelById(id);
                if (hotel != null) {
                    out.write(toJson(hotel));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Hotel not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Invalid hotel ID format\"}");
            }
        } else {
            List<HotelBean> hotels = hotelDao.getAllHotels();
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < hotels.size(); i++) {
                sb.append("  ").append(toJson(hotels.get(i)));
                if (i < hotels.size() - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
            sb.append("]");
            out.write(sb.toString());
        }
    }

    private String toJson(HotelBean hotel) {
        HotelMetadata meta = METADATA.getOrDefault(hotel.getId(), DEFAULT_METADATA);
        int backendHotelId = (hotel.getLegacyId() != null) ? hotel.getLegacyId() : hotel.getId();

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"id\": ").append(hotel.getId()).append(",\n");
        sb.append("    \"backendHotelId\": ").append(backendHotelId).append(",\n");
        sb.append("    \"name\": \"").append(escapeJson(hotel.getName())).append("\",\n");
        sb.append("    \"category\": \"").append(escapeJson(meta.category)).append("\",\n");
        sb.append("    \"price\": ").append(Math.round(hotel.getPricePerNight())).append(",\n");
        sb.append("    \"priceMin\": ").append(Math.round(hotel.getMinPrice())).append(",\n");
        sb.append("    \"priceMax\": ").append(Math.round(hotel.getMaxPrice())).append(",\n");
        sb.append("    \"rating\": ").append(hotel.getRating()).append(",\n");
        sb.append("    \"location\": \"").append(escapeJson(hotel.getLocation())).append("\",\n");
        sb.append("    \"description\": \"").append(escapeJson(meta.description)).append("\",\n");
        sb.append("    \"locationOverview\": \"").append(escapeJson(meta.locationOverview)).append("\",\n");

        sb.append("    \"images\": [");
        for (int i = 0; i < meta.images.length; i++) {
            sb.append("\"").append(escapeJson(meta.images[i])).append("\"");
            if (i < meta.images.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("],\n");

        sb.append("    \"amenities\": [");
        for (int i = 0; i < meta.amenities.length; i++) {
            sb.append("\"").append(escapeJson(meta.amenities[i])).append("\"");
            if (i < meta.amenities.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]\n");
        sb.append("  }");
        return sb.toString();
    }

    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
